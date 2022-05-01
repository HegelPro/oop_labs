package tech;

import java.io.*;
import java.net.*;
import java.util.Arrays;

import tech.previusLabs.*;
import tech.previusLabs.exceptions.*;

public class Lab7 {
    public static class Client {
        public static void main(String[] args) throws IOException, DuplicateModelNameException, NoSuchModelNameException {
            Socket s = new Socket("localhost", 5000);

            Moped moped = new Moped("moped");
            moped.addModel("moped1", 10);
            moped.addModel("moped2", 10);

            Scooter scooter = new Scooter("scooter");
            scooter.addModel("scooter1", 20);
            scooter.addModel("scooter2", 20);

            QuadBike quadBike = new QuadBike("quadBike");
            quadBike.addModel("quadBike1", 30);
            quadBike.addModel("quadBike2", 30);

            Vehicle[] vehicles = new Vehicle[] {moped, scooter, quadBike};

            ObjectOutputStream ois = new ObjectOutputStream(s.getOutputStream());
            ois.writeObject(vehicles);
            ois.flush();

            InputStreamReader in = new InputStreamReader(s.getInputStream());
            BufferedReader bf = new BufferedReader(in);

            String str = bf.readLine();
            System.out.println("Average model price: " + str);
        }
    }

    public static class Server {
        public static void main(String[] args) throws IOException, ClassNotFoundException {
            ServerSocket ss = new ServerSocket(5000);
            Socket s = ss.accept();

            System.out.println("Client connected");

            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            Vehicle[] vehicles = (Vehicle[]) ois.readObject();

            System.out.println(Arrays.toString(vehicles));

            double sumLength = 0;
            double sumPrice = 0;

            for (Vehicle vehicle : vehicles) {
                sumLength += vehicle.getSize();
                for (double arrayOfPrice : vehicle.getArrayOfPrices()) {
                    sumPrice += arrayOfPrice;
                }
            }

            PrintWriter pr = new PrintWriter(s.getOutputStream());
            pr.println(sumPrice / sumLength);
            pr.flush();
        }
    }
}
