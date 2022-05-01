package tech;

import tech.previusLabs.exceptions.*;
import tech.previusLabs.*;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Scanner;

public class Lab5 {
    static private Car car = new Car("manufacturer", 2);

    public static void main(String[] args) throws DuplicateModelNameException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException, NoSuchModelNameException {
        car.addModel("model1", 10);
        car.addModel("model2", 20);

        task1();
        task2();
        task3();
        task4();
        task5();
        task6();
        task7();
    }

    public static void reflect(String[] args) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Class c = Class.forName(args[0]);
        Method m = c.getMethod(args[1], String.class, Double.TYPE);

        String str = args[2];
        Double val = Double.valueOf(args[3]);
        m.invoke(car, str, val);
    }

    public static void task1() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println("Before modify:");
        print(car);

        reflect("tech.previusLabs.Car modifyPriceByName model1 30".split(" "));

        System.out.println("After modify:");
        print(car);
    }

    public static Object createFromInstance(String manufacturer, Integer sizeArrayModels, Vehicle vehicle) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if(manufacturer == null || sizeArrayModels == null || vehicle == null) {
            return null;
        }
        Constructor<?> constructor = vehicle.getClass().getConstructor(String.class, Integer.TYPE);
        return constructor.newInstance(manufacturer, sizeArrayModels);
    }

    public static void task2() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Motorcycle motorcycle = new Motorcycle("manufacturer");

        Object newVehicle = createFromInstance("newManufacturer", 1, motorcycle);
        Method m = newVehicle.getClass().getMethod("addModel", String.class, Double.TYPE);
        m.invoke(newVehicle, "newModel1", 10);
        m.invoke(newVehicle, "newModel2", 20);
        print(newVehicle);
    }

    public static void task3() throws DuplicateModelNameException, NoSuchModelNameException {
        Scooter scooter = new Scooter("scooter");
        scooter.addModel("scooterModel1", 2);
        scooter.addModel("scooterModel2", 2);
        print(scooter);
    }
    public static void task4() throws DuplicateModelNameException, NoSuchModelNameException {
        QuadBike quadBike = new QuadBike("quadBike");
        quadBike.addModel("quadBikeModel1", 2);
        quadBike.addModel("quadBikeModel2", 2);
        print(quadBike);
    }
    public static void task5() throws DuplicateModelNameException, NoSuchModelNameException {
        Moped moped = new Moped("moped");
        moped.addModel("mopedModel1", 2);
        moped.addModel("mopedModel2", 2);
        print(moped);
    }

    public static double getAveragePrice(Vehicle vehicle) {
        return Arrays
                .stream(vehicle.getArrayOfPrices())
                .reduce((acc, cur) -> acc + cur)
                .getAsDouble() / vehicle.getSize();
    }
    public static void task6() {
        Vehicle[] vehicles = new Vehicle[3];
        vehicles[0] = car;
        vehicles[1] = car;
        vehicles[2] = car;
        double[] values = Arrays
                .stream(vehicles)
                .map(vehicle -> getAveragePrice(vehicle))
                .mapToDouble(Double::doubleValue)
                .toArray();
        print(Arrays.toString(values));
    }

    static String printFormat = "%s\n\n";
    static void print(Object... args) {
        System.out.printf(printFormat, args);
    }

    static void scanForUpdateCarModels(Scanner scanner) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
//        System.out.println("ClassName:");
        String className = "tech.previusLabs.Car";
//        System.out.println("MethodName:");
        String methodName = "modifyPriceByName";
        System.out.println("ModelName:");
        String modelName = scanner.next();
        System.out.println("Price:");
        String price = scanner.next();

        reflect(new String[] {className, methodName, modelName, price});

        print(car);

        scanForUpdateCarModels(scanner);
    }

    public static void task7() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Scanner scanner = new Scanner(System.in);
        scanForUpdateCarModels(scanner);
    }
}
