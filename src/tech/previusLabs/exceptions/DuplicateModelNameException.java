package tech.previusLabs.exceptions;

public class DuplicateModelNameException extends Exception{
    private String modelName;
    public DuplicateModelNameException(String modelName) {
        super("Model named \"" + modelName +
                "\" already exists");
        this.modelName = modelName;
    }
}
