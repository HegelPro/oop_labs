package tech.previusLabs;

import tech.previusLabs.exceptions.DuplicateModelNameException;
import tech.previusLabs.exceptions.NoSuchModelNameException;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class IOStaticMethods {
    public static double averageOFModelPrices(Vehicle vehicle) {
        double[] prices = vehicle.getArrayOfPrices();
        double number = vehicle.getSize();
        double sum = 0;
        for (double price : prices) sum += price;
        return sum / number;
    }

    public static void printModelNames(Vehicle vehicle) {
        String[] modelNames = vehicle.getArrayOfNames();
        for (String name : modelNames) System.out.println(name);
    }

    public static void printModelPrices(Vehicle vehicle) {
        double[] prices = vehicle.getArrayOfPrices();
        for (double price : prices) System.out.println(price);
    }

    public static void printVehicle(Vehicle vehicle) {
        String[] modelNames = vehicle.getArrayOfNames();
        double[] prices = vehicle.getArrayOfPrices();
        System.out.println(vehicle.getManufacturer());
        for (int i = 0; i < vehicle.getSize(); i++) {
            System.out.println(modelNames[i] + ' ' + prices[i]);
        }
    }

    //метод записи информации о транспортном средстве в байтовый поток
    public static void outputVehicle(Vehicle vehicle, OutputStream out) {
        String[] modelNames = vehicle.getArrayOfNames();
        double[] prices = vehicle.getArrayOfPrices();
        int size = vehicle.getSize();
        //конструкция try-with-resources
        try (DataOutputStream dos = new DataOutputStream(out)) {
            // записываем значения
            IOStaticMethods.outputString(dos, vehicle.getManufacturer());
            dos.writeInt(size);
            int i = 0;
            while (i < size) {
                IOStaticMethods.outputString(dos, modelNames[i]);
                dos.writeDouble(prices[i]);
                i++;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void outputString(DataOutputStream dos, String message)
            throws IOException {
        byte[] byteMessage = message.getBytes(StandardCharsets.UTF_8);
        dos.writeInt(byteMessage.length);
        dos.write(byteMessage, 0, byteMessage.length);
    }

    private static String inputString(DataInputStream dis) throws IOException {
        int messageLength = dis.readInt();
        byte[] message = new byte[messageLength];
        if (dis.read(message, 0, messageLength) == -1)
            throw new IOException();
        return new String(message);
    }

    //метод чтения информации о автомобиле из байтового потока
    public static Car inputCar(InputStream in) {
        // обратное считывание из файлa
        try (DataInputStream dis = new DataInputStream(in)) {
            // записываем значения
            Car car = new Car(IOStaticMethods.inputString(dis),
                    dis.readInt());
            int i = 0;
            while (i < car.getSize()) {
                car.addModel(IOStaticMethods.inputString(dis),
                        dis.readDouble());
                i++;
            }
            return car;
        } catch (IOException | DuplicateModelNameException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    //метод чтения информации о Мотоцикле из байтового потока
    public static Motorcycle inputMotorcycle(InputStream in) {
        try (DataInputStream dis = new DataInputStream(in)) {
            Motorcycle motorcycle = new Motorcycle(IOStaticMethods.inputString(dis));
            int size = dis.readInt();
            int i = 0;
            while (i < size) {
                motorcycle.addModel(IOStaticMethods.inputString(dis), dis.readDouble());
                i++;
            }
            return motorcycle;
        } catch (IOException | DuplicateModelNameException | NoSuchModelNameException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    //метод записи информации о транспортном средстве в символьный поток
    public static void writeVehicle(Vehicle vehicle, Writer out) {
        PrintWriter pwout = new PrintWriter(out);
        int size = vehicle.getSize();
        String[] modelNames = vehicle.getArrayOfNames();
        double[] prices = vehicle.getArrayOfPrices();
        pwout.println(vehicle.getManufacturer() + " , " + size);
        for (int i = 0; i < size; ++i)
            pwout.println(modelNames[i] + " , " + prices[i]);
        if (pwout.checkError())
            System.err.println("Error detected");
        pwout.close();
    }

    //метод чтения информации о автомобиле из символьного потока
    public static Car readCar(Reader in) {
        try {
            BufferedReader reader = new BufferedReader(in);
            String[] line = reader.readLine().split(" , ");
            int size = Integer.parseInt(line[1]);
            Car car = new Car(line[0], size);
            for (int i = 0; i < size; i++) {
                line = reader.readLine().split(" , ");
                car.addModel(line[0], Double.parseDouble(line[1]));
            }
            reader.close();
            return car;
        } catch (IOException | DuplicateModelNameException e) {
            System.out.println(e.getMessage());
            return null;
        }


    }

    //метод чтения информации о мотоцикле из символьного потока
    public static Motorcycle readMotorcycle(Reader in) {
        try {
            BufferedReader reader = new BufferedReader(in);
            String[] line = reader.readLine().split(" , ");
            int size = Integer.parseInt(line[1]);
            Motorcycle motorcycle = new Motorcycle(line[0]);
            for (int i = 0; i < size; i++) {
                line = reader.readLine().split(" , ");
                motorcycle.addModel(line[0], Double.parseDouble(line[1]));
            }
            reader.close();
            return motorcycle;
        } catch (IOException | DuplicateModelNameException | NoSuchModelNameException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
