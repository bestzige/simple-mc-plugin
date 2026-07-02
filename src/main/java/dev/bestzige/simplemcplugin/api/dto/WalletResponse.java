package dev.bestzige.simplemcplugin.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author BestZige
 * Created: 7/2/2026
 */

@Data
public class WalletResponse {

    private UUID playerUuid;
    private BigDecimal coins;
    private BigDecimal gems;
}
