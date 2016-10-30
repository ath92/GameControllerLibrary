package  com.disastroids.gamecontrollerlibrary;

/**
 * inerface for all inputMethods. Fairly empty, just specifies that every InputMethod should have a serialze() method.
 */
public interface InputMethod {

    /**
     * Serializes the data of the input method in a string
     */
    public String serialize();
}

