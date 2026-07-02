package dev.bestzige.simplemcplugin.api.dto;

import lombok.Data;

/**
 * @author BestZige
 * Created: 7/2/2026
 */

@Data
public class ApiResponse<T> {

    private boolean success;
    private String code;
    private String message;
    private T data;
}
