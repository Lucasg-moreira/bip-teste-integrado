package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Dados para transferência entre benefícios")
public record TransferRequestDTO (
        @NotNull
        @Schema(description = "ID do benefício de origem", example = "1")
        Long fromId,
        @NotNull
        @Schema(description = "ID do benefício de destino", example = "2")
        Long toId,
        @NotNull
        @Positive
        @Schema(description = "Valor da transferência", example = "100.50")
        BigDecimal amount
) {

}