package dev.bestzige.simplemcplugin.api.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author BestZige
 * Created: 7/2/2026
 */

@Data
@Builder
public class TransferRequest {

    private UUID fromUuid;
    private UUID toUuid;
    private BigDecimal amount;
    private String currency;
}
