package tech;

import tech.previusLabs.*;
import tech.previusLabs.exceptions.*;

import java.io.Serializable;
import java.sql.Array;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Stream;

public class Scooter implements Vehicle, Serializable, Cloneable {
    private String manufacturer;
    private HashMap<String, Double> models;

    Scooter(String manufacturer) {
        this.manufacturer = manufacturer;
        this.models = new HashMap<String, Double>();
    }

    @Override
    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String[] getArrayOfNames() {
        return models.keySet().toArray(new String[0]);
    }

    @Override
    public double[] getArrayOfPrices() {
        Double[] boxed = models.values().toArray(new Double[0]);
        double[] unboxed = Stream.of(boxed).mapToDouble(Double::doubleValue).toArray();
        return unboxed;
    }

    @Override
    public double getPriceByName(String modelName) throws NoSuchModelNameException {
        double price = models.get(modelName);
        if(models.get(modelName) == null) {
            throw new NoSuchModelNameException(modelName);
        }
        return price;
    }

    @Override
    public void modifyName(String oldModelName, String newModelName) throws NoSuchModelNameException, DuplicateModelNameException {
        double value = getPriceByName(oldModelName);
        removeModel(oldModelName);
        addModel(newModelName, value);
    }

    @Override
    public void modifyPriceByName(String modelName, double newModelPrice) throws ModelPriceOutOfBoundsException, NoSuchModelNameException {
        getPriceByName(modelName);
        models.replace(modelName, newModelPrice);
    }

    @Override
    public void addModel(String modelName, double modelPrice) throws DuplicateModelNameException {
        if(models.get(modelName) != null) {
            throw new DuplicateModelNameException(modelName);
        }
        models.put(modelName, modelPrice);
    }

    @Override
    public void removeModel(String modelName) throws NoSuchModelNameException {
        if(models.get(modelName) == null) {
            throw new NoSuchModelNameException(modelName);
        }
        models.remove(modelName);
    }

    @Override
    public int getSize() {
        return models.size();
    }

    @Override
    public String toString() {
        return "Scooter{" +
                "manufacturer='" + manufacturer + '\'' +
                ", models=" + models +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scooter scooter = (Scooter) o;
        return Objects.equals(manufacturer, scooter.manufacturer) && Objects.equals(models, scooter.models);
    }

    @Override
    public int hashCode() {
        return Objects.hash(manufacturer, models);
    }
}
