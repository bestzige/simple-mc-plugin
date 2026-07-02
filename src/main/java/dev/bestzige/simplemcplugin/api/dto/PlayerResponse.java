package dev.bestzige.simplemcplugin.api.dto;

import lombok.Data;

import java.util.UUID;

/**
 * @author BestZige
 * Created: 7/2/2026
 */

@Data
public class PlayerResponse {

    private UUID uuid;
    private String username;
    private String rank;
}
