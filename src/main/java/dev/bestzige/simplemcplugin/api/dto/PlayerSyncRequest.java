package dev.bestzige.simplemcplugin.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * @author BestZige
 * Created: 7/2/2026
 */

@Data
@Builder
public class PlayerSyncRequest {

    private UUID uuid;
    private String username;
}
