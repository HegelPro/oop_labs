package tech.previusLabs;

import tech.previusLabs.exceptions.DuplicateModelNameException;
import tech.previusLabs.exceptions.ModelPriceOutOfBoundsException;
import tech.previusLabs.exceptions.NoSuchModelNameException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Car implements Vehicle, Serializable, Cloneable {
    //поле типа String, хранящее марку автомобиля
    private String manufacturer;
    private Model[] modelsArray;
    //private int size;

    public Car(String Manufacturer, int sizeArrayModels) {
        this.manufacturer = Manufacturer;
        modelsArray = new Model[sizeArrayModels];
    }

    public Car(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Car() {
    }

    //метод для поиска
    private Model findModelByName(String modelName) {
        int i = 0;
        while (i < modelsArray.length &&
                !(modelsArray[i] == null) &&
                !modelsArray[i].modelName.equals(modelName))
            i++;
        if (i == modelsArray.length || modelsArray[i] == null) return null;
        else return modelsArray[i];
    }


    //метод для получения марки автомобиля
    public String getManufacturer() {
        return manufacturer;
    }

    //метод для модификации марки автомобиля
    public void setManufacturer(String carManufacturer) {
        this.manufacturer = carManufacturer;
    }

    //внутренний класс Модель, имеющий поля название модели и её цену,
    // а также конструктор (класс Автомобиль хранит массив Моделей)
    private class Model implements Serializable, Cloneable {
        private String modelName;
        private double modelPrice;

        public Model(String modelName, double modelPrice) {
            this.modelName = modelName;
            this.modelPrice = modelPrice;
        }

        @Override
        public String toString() {
            return modelName + "=" + modelPrice;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return (Model) super.clone();
        }
    }

    //метод, возвращающий массив названий всех моделей
    public String[] getArrayOfNames() {
        String[] carModels = new String[modelsArray.length];
        for (int i = 0; i < carModels.length; i++)
            carModels[i] = modelsArray[i].modelName;
        return carModels;
    }

    //метод, возвращающий массив значений цен моделей
    public double[] getArrayOfPrices() {
        double[] carPrices = new double[modelsArray.length];
        for (int i = 0; i < carPrices.length; i++)
            carPrices[i] = modelsArray[i].modelPrice;
        return carPrices;
    }

    //метод для получения значения цены модели по её названию
    public double getPriceByName(String carModel) throws NoSuchModelNameException {
        Model soughtModel = findModelByName(carModel);
        if (soughtModel == null) throw new NoSuchModelNameException(carModel);
        else return soughtModel.modelPrice;
    }

    //метод для модификации значения названия модели
    public void modifyName(String oldCarModel, String newCarModel)
            throws NoSuchModelNameException, DuplicateModelNameException {
        if (findModelByName(newCarModel) != null) throw new
                DuplicateModelNameException(newCarModel);
        else {
            Model soughtModel = findModelByName(oldCarModel);
            if (soughtModel == null) throw new NoSuchModelNameException(oldCarModel);
            else
                soughtModel.modelName = newCarModel;
        }
    }

    //метод для модификации значения цены модели по её названию
    public void modifyPriceByName(String carModel, double newCarPrice) throws
            NoSuchModelNameException {
        if (newCarPrice < 0) throw new ModelPriceOutOfBoundsException();
        else {
            Model soughtModel = findModelByName(carModel);
            if (soughtModel == null) throw new NoSuchModelNameException(carModel);
            else
                soughtModel.modelPrice = newCarPrice;
        }
    }

    //метод добавления названия модели и её цены (путем создания нового
    // массива Моделей), использовать метод Arrays.copyOf()
    public void addModel(String carModel, double carPrice) throws
            DuplicateModelNameException {
        if (findModelByName(carModel) != null) throw new
                DuplicateModelNameException(carModel);
        else {
            if (carPrice < 0) throw new ModelPriceOutOfBoundsException();
            else {
                int i = 0;
                while (i < modelsArray.length && modelsArray[i] != null)
                    i++;
                Model newModel = new Model(carModel, carPrice);
                if (i == modelsArray.length) {
                    modelsArray = Arrays.copyOf(modelsArray, modelsArray.length + 1);
                    modelsArray[modelsArray.length - 1] = newModel;
                } else modelsArray[i] = newModel;
            }
        }
    }

    //метод удаления модели с заданным именем и её цены, использовать
    // методы System.arraycopy, Arrays.copyOf()
    public void removeModel(String carModel) throws NoSuchModelNameException {
        if (findModelByName(carModel) == null) throw new
                NoSuchModelNameException(carModel);
        else {
            int indexForModelToRemove = -1;
            int i = 0;
            while (i < modelsArray.length && modelsArray[i] != null) {
                if (modelsArray[i].modelName.equals(carModel))
                    indexForModelToRemove = i;
                i++;
            }
            if (indexForModelToRemove != (modelsArray.length - 1))
                System.arraycopy(modelsArray, indexForModelToRemove + 1, modelsArray,
                        indexForModelToRemove, modelsArray.length -
                                (indexForModelToRemove + 1));
            modelsArray = Arrays.copyOf(modelsArray, modelsArray.length - 1);
        }
    }

    //метод для получения размера массива Моделей
    public int getSize() {
        return modelsArray.length;
    }

    public void printAllModels() {
        int i = 0;
        while (i < modelsArray.length && modelsArray[i] != null) {
            System.out.println("Name: " + this.manufacturer + " " +
                    modelsArray[i].modelName + " Price: " +
                    modelsArray[i].modelPrice);
            i++;
        }
    }

    @Override
    public String toString() {
        return "Car{" +
                "manufacturer='" + manufacturer + '\'' +
                ", models=" + Arrays.toString(modelsArray) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        //if (o == null || getClass() != o.getClass()) return false;
        //Car car = (Car) o;
        //return manufacturer.equals(car.manufacturer) && Arrays.equals(modelsArray, car.modelsArray);
        if (o instanceof Vehicle) {
            Vehicle v = (Vehicle) o;
            //int curSize = getSize();
            int comparedSize = v.getSize();
            if (comparedSize == getSize())
                if (v.getManufacturer().equals(manufacturer)) {
                    String[] currentNames = getArrayOfNames();
                    Arrays.sort(currentNames);
                    double[] currentPrices = getArrayOfPrices();
                    Arrays.sort(currentPrices);
                    String[] comparedNames = v.getArrayOfNames();
                    Arrays.sort(comparedNames);
                    double[] comparedPrices = v.getArrayOfPrices();
                    Arrays.sort(comparedPrices);
                    int i = 0;
                    while (i < comparedSize && currentNames[i].equals(comparedNames[i]) &&
                            (Double.compare(currentPrices[i], comparedPrices[i]) == 0))
                        i++;
                    return i == comparedSize;
                }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(manufacturer);
        result = 31 * result + Arrays.hashCode(modelsArray);
        return result;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Car vehicleClone = (Car) super.clone();
        vehicleClone.modelsArray = modelsArray.clone();
        for (int i = 0; i < getSize(); i++) {
            vehicleClone.modelsArray[i] = (Model) modelsArray[i].clone();
        }
        vehicleClone.manufacturer = getManufacturer();
        //vehicleClone.size = getSize();
        return vehicleClone;
    }
}
