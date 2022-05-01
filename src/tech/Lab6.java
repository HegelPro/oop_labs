package tech;

import tech.previusLabs.*;
import tech.previusLabs.exceptions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;


// Task1
class Thread1 extends Thread {
    private Vehicle v;

    Thread1(Vehicle v) {
        super();
        this.v = v;
    }
    @Override
    public void run() {
        for (double price : this.v.getArrayOfPrices()) {
            System.out.println("Print price: " + price);
        }
    }
}

class Thread2 extends Thread {
    private Vehicle v;

    Thread2(Vehicle v) {
        super();
        this.v = v;
    }
    @Override
    public void run() {
        for (String name : this.v.getArrayOfNames()) {
            System.out.println("Print name: " + name);
        }
    }
}

// Task2
class Thread3 implements Runnable {
    TransportSynchronizer t;

    Thread3(TransportSynchronizer t) {
        this.t = t;
    }

    @Override
    public void run() {
        try {
            this.t.printModel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    boolean canRun() {
        return this.t.canPrintModel();
    }
}

class Thread4 implements Runnable {
    TransportSynchronizer t;

    Thread4(TransportSynchronizer t) {
        this.t = t;
    }

    @Override
    public void run() {
        try {
            this.t.printPrice();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    boolean canRun() {
        return this.t.canPrintPrice();
    }
}

class TransportSynchronizer {
    private Vehicle v;
    private volatile int current = 0;
    private Object lock = new Object();
    private boolean set = false;

    public TransportSynchronizer(Vehicle v) {
        this.v = v;
    }

    public double printPrice() throws InterruptedException {
        double val;
        synchronized(lock) {
            double [] p = v.getArrayOfPrices();
            if (!canPrintPrice()) throw new InterruptedException();
            while (!set)
                lock.wait();
            val = p[current++];
            System.out.println("Print price: " + val);
            set = false;
            lock.notifyAll();
        }
        return val;
    }

    public void printModel() throws InterruptedException {
        synchronized(lock) {
            String [] s = v.getArrayOfNames();
            if (!canPrintModel()) throw new InterruptedException();
            while (set)
                lock.wait();
            System.out.println("Print model: " + s[current]);
            set = true;
            lock.notifyAll();
        }
    }

    public boolean canPrintPrice() {
        return current < v.getSize();
    }

    public boolean canPrintModel() {
        return (!set && current < v.getSize()) || (set && current < v.getSize() - 1);
    }
}

// Task3
class Thread5 implements Runnable {
    private Vehicle v;
    private ReentrantLock rl;

    Thread5(Vehicle v, ReentrantLock rl) {
        this.v = v;
        this.rl = rl;
    }
    @Override
    public void run() {
        for (double price : this.v.getArrayOfPrices()) {
            System.out.println("Print price: " + price);
        }
        try {
            this.rl.unlock();
        } catch (IllegalMonitorStateException e) {}
    }
}

class Thread6 implements Runnable {
    private Vehicle v;
    private ReentrantLock rl;

    Thread6(Vehicle v, ReentrantLock rl) {
        this.v = v;
        this.rl = rl;
    }
    @Override
    public void run() {
        this.rl.tryLock();
        for (String name : this.v.getArrayOfNames()) {
            System.out.println("Print name: " + name);
        }
    }
}
// task4
class Thread7 implements Runnable {
    private Vehicle v;

    Thread7(Vehicle v) {
        this.v = v;
    }
    @Override
    public void run() {
        System.out.println(this.v.getManufacturer());
    }
}

// task5
class Thread8 implements Runnable {
    ArrayBlockingQueue<Vehicle> abq;
    String fn;
    Thread8 (String fn, ArrayBlockingQueue abq) {
        this.fn = fn;
        this.abq = abq;
    }

    @Override
    public void run() {
        Path fileName = Path.of(this.fn);
        try {
            String name = Files.readString(fileName);
            Moped moped = new Moped(name);
            this.abq.put(moped);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Lab6 {
    public static void main(String[] args) throws DuplicateModelNameException, NoSuchModelNameException, InterruptedException {
//        task1();
//        task2();
//        task3();
        task4();
        task5();
    }

    static void task1() throws DuplicateModelNameException, NoSuchModelNameException {
        Moped moped = new Moped("Moped1");
        moped.addModel("model1", 10);
        moped.addModel("model2", 20);

        Thread1 thread1 = new Thread1(moped);
        thread1.setPriority(1);
        thread1.start();

        Thread2 thread2 = new Thread2(moped);
        thread2.setPriority(10);
        thread2.start();
    }

    static void task2() throws DuplicateModelNameException, NoSuchModelNameException {
        Moped moped = new Moped("moped");
        moped.addModel("model1", 10);
        moped.addModel("model2", 20);

        TransportSynchronizer transportSynchronizer = new TransportSynchronizer(moped);
        Thread3 thread3 = new Thread3(transportSynchronizer);
        Thread4 thread4 = new Thread4(transportSynchronizer);

        while (thread3.canRun() && thread4.canRun()) {
            thread3.run();
            thread4.run();
        }
    }

    static void task3() throws DuplicateModelNameException, NoSuchModelNameException {
        Moped moped = new Moped("moped");
        moped.addModel("model1", 10);
        moped.addModel("model2", 20);

        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();

        Thread thread5 = new Thread(new Thread5(moped, reentrantLock));
        thread5.start();

        Thread thread6 = new Thread(new Thread6(moped, reentrantLock));
        thread6.start();
    }

    static void task4() {
        Moped moped1 = new Moped("Moped1");
        Moped moped2 = new Moped("Moped2");
        Moped moped3 = new Moped("Moped3");
        Moped moped4 = new Moped("Moped4");

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        executor.execute(new Thread(new Thread7(moped1)));
        executor.execute(new Thread(new Thread7(moped2)));
        executor.execute(new Thread(new Thread7(moped3)));
        executor.execute(new Thread(new Thread7(moped4)));
    }

    // TODO
    static void task5() throws InterruptedException {
        ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue<Vehicle>(5);

        Thread thread1 = new Thread(new Thread8("input/oop/one", arrayBlockingQueue));
        Thread thread2 = new Thread(new Thread8("input/oop/two", arrayBlockingQueue));
        Thread thread3 = new Thread(new Thread8("input/oop/three", arrayBlockingQueue));
        Thread thread4 = new Thread(new Thread8("input/oop/four", arrayBlockingQueue));
        Thread thread5 = new Thread(new Thread8("input/oop/five", arrayBlockingQueue));

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();

        System.out.println(arrayBlockingQueue.take());
        System.out.println(arrayBlockingQueue.take());
        System.out.println(arrayBlockingQueue.take());
        System.out.println(arrayBlockingQueue.take());
        System.out.println(arrayBlockingQueue.take());
    }
}
