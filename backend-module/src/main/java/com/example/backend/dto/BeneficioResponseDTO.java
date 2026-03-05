package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Dados retornados do benefício")
public record BeneficioResponseDTO (
        @Schema(description = "ID do benefício", example = "1")
        Long id,
        @Schema(description = "Nome do benefício", example = "Vale Alimentação")
        String nome,
        @Schema(description = "Saldo atual do benefício", example = "450.00")
        BigDecimal valor) {}

