package tech.previusLabs.exceptions;

public class NoSuchModelNameException extends Exception{
    private String modelName;
    public NoSuchModelNameException(String modelName) {
        super("Model with the name \"" + modelName +
                "\" not found");
        this.modelName = modelName;
    }
}
