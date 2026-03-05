package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Dados para criação/atualização de benefício")
public record BeneficioRequestDTO (
        @Schema(description = "Nome do benefício", example = "Vale Alimentação")
        @NotBlank(message = "Nome obrigatório")
        String nome,
        @Schema(description = "Descrição do benefício", example = "Benefício para alimentação")
        String descricao,
        @Schema(description = "Saldo inicial do benefício", example = "500.00")
        @NotNull(message = "Valor obrigatório")
        @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser positivo")
        BigDecimal valor) {}
