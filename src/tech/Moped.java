package tech;

import tech.previusLabs.Vehicle;
import tech.previusLabs.exceptions.DuplicateModelNameException;
import tech.previusLabs.exceptions.ModelPriceOutOfBoundsException;
import tech.previusLabs.exceptions.NoSuchModelNameException;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Objects;

public class Moped implements Vehicle, Serializable, Cloneable {
    private String manufacturer;
    private LinkedList<Model> models;

    Moped(String manufacturer) {
        this.manufacturer = manufacturer;
        this.models = new LinkedList<Model>();
    }

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
        return models.stream().map(model -> model.modelName).toArray(size -> new String[size]);
    }

    @Override
    public double[] getArrayOfPrices() {
        return models.stream().map(model -> model.modelPrice).mapToDouble(Double::doubleValue).toArray();
    }

    @Override
    public double getPriceByName(String modelName) throws NoSuchModelNameException {
        for (Model model: models) {
            if (model.modelName == modelName) {
                return model.modelPrice;
            }
        }
        throw new NoSuchModelNameException(modelName);
    }

    @Override
    public void modifyName(String oldModelName, String newModelName) throws NoSuchModelNameException, DuplicateModelNameException {
        double value = getPriceByName(oldModelName);
        removeModel(oldModelName);
        addModel(newModelName, value);
    }

    @Override
    public void modifyPriceByName(String modelName, double newModelPrice) throws ModelPriceOutOfBoundsException, NoSuchModelNameException {
        for (Model model: models) {
            if (model.modelName == modelName) {
                model.modelPrice = newModelPrice;
                return;
            }
        }
        throw new NoSuchModelNameException(modelName);
    }

    @Override
    public void addModel(String modelName, double modelPrice) throws DuplicateModelNameException, NoSuchModelNameException {
        for (Model model: models) {
            if(model.modelName == modelName) {
                throw new DuplicateModelNameException(modelName);
            }
        }
        models.add(new Model(modelName, modelPrice));
    }

    @Override
    public void removeModel(String modelName) throws NoSuchModelNameException {
        for (Model model: models) {
            if(model.modelName == modelName) {
                models.remove(model);
                return;
            }
        }
        throw new NoSuchModelNameException(modelName);
    }

    @Override
    public int getSize() {
        return models.size();
    }

    @Override
    public String toString() {
        return "Moped{" +
                "manufacturer='" + manufacturer + '\'' +
                ", models=" + models +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Moped scooter = (Moped) o;
        return Objects.equals(manufacturer, scooter.manufacturer) && Objects.equals(models, scooter.models);
    }

    @Override
    public int hashCode() {
        return Objects.hash(manufacturer, models);
    }
}
