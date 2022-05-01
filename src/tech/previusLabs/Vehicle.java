package tech.previusLabs;

import tech.previusLabs.exceptions.DuplicateModelNameException;
import tech.previusLabs.exceptions.ModelPriceOutOfBoundsException;
import tech.previusLabs.exceptions.NoSuchModelNameException;

public interface Vehicle {
    String getManufacturer();

    void setManufacturer(String manufacturer);

    String[] getArrayOfNames();

    double[] getArrayOfPrices();

    double getPriceByName(String modelName) throws NoSuchModelNameException;

    void modifyName(String oldModelName, String newModelName) throws
            NoSuchModelNameException, DuplicateModelNameException, NoSuchModelNameException, DuplicateModelNameException;

    void modifyPriceByName(String modelName, double newModelPrice) throws
            ModelPriceOutOfBoundsException, NoSuchModelNameException;

    void addModel(String modelName, double modelPrice) throws
            DuplicateModelNameException, NoSuchModelNameException;

    void removeModel(String modelName) throws NoSuchModelNameException;

    int getSize();
}
