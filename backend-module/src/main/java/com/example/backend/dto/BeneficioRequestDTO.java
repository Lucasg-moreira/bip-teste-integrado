package com.example.backend.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record BeneficioRequestDTO (
        @NotBlank(message = "Nome obrigatório")
        String nome,
        String descricao,
        @NotNull(message = "Valor obrigatório")
        @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser positivo")
        BigDecimal valor) {}
