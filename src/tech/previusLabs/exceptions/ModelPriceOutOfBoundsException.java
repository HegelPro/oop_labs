package tech.previusLabs.exceptions;

public class ModelPriceOutOfBoundsException extends RuntimeException{
    public ModelPriceOutOfBoundsException() {
        super("Model Price Out Of Bound Exception: " +
                "the price must be greater than 0");
    }
}
