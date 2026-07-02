package dev.bestzige.simplemcplugin.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author BestZige
 * Created: 7/2/2026
 */

@Data
public class TransactionResponse {

    private UUID id;
    private BigDecimal amount;
    private String currency;
    private String type;
}
