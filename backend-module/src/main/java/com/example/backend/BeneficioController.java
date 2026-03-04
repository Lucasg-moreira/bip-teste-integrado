package com.example.backend;

import com.example.backend.dto.BeneficioRequestDTO;
import com.example.backend.dto.BeneficioResponseDTO;
import com.example.backend.service.IBeneficioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/v1/beneficios")
public class BeneficioController {

    private final IBeneficioService beneficioService;

    public BeneficioController(IBeneficioService beneficioService) {
        this.beneficioService = beneficioService;
    }

    @PostMapping
    public ResponseEntity<BeneficioResponseDTO> post(
            @Valid @RequestBody BeneficioRequestDTO dto) {

        BeneficioResponseDTO response = beneficioService.criar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping
    @RequestMapping("/transferencia")
    public ResponseEntity<Void> transfer(
            @RequestParam Long fromId,
            @RequestParam Long toId,
            @RequestParam BigDecimal amount) {
        beneficioService.transfer(fromId, toId, amount);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BeneficioResponseDTO> put(
            @PathVariable Long id,
            @RequestBody BeneficioRequestDTO dto) {

        BeneficioResponseDTO response = beneficioService.atualizar(id, dto);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BeneficioResponseDTO>> get() {

        return ResponseEntity.ok(beneficioService.buscarTodos());
    }

}
