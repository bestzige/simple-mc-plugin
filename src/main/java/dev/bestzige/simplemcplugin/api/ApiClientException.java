package dev.bestzige.simplemcplugin.api;

/**
 * @author BestZige
 * Created: 7/2/2026
 */

public class ApiClientException extends Exception {

    public ApiClientException(String message) {
        super(message);
    }

    public ApiClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
