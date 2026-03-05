package com.example.backend.controller;

import com.example.backend.dto.BeneficioRequestDTO;
import com.example.backend.dto.BeneficioResponseDTO;
import com.example.backend.dto.TransferRequestDTO;
import com.example.backend.service.IBeneficioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/beneficios")
@Tag(name = "Benefícios", description = "Gerenciamento de benefícios e transferências")
public class BeneficioController {

    private final IBeneficioService beneficioService;

    public BeneficioController(IBeneficioService beneficioService) {
        this.beneficioService = beneficioService;
    }

    @Operation(
            summary = "Criar um novo benefício",
            description = "Cria um benefício com base nos dados informados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Benefício criado com sucesso",
                    content = @Content(schema = @Schema(implementation = BeneficioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<BeneficioResponseDTO> post(
            @Valid @RequestBody BeneficioRequestDTO dto) {

        BeneficioResponseDTO response = beneficioService.criar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Transferir valor entre benefícios",
            description = "Realiza a transferência de um valor entre dois benefícios"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "404", description = "Benefício não encontrado")
    })
    @PostMapping("/transferencia")
    public ResponseEntity<Void> transfer(
            @Valid @RequestBody TransferRequestDTO transferRequest) {

        beneficioService.transfer(transferRequest.fromId(), transferRequest.toId(), transferRequest.amount());
        return ResponseEntity
                .ok().build();
    }

    @Operation(
            summary = "Atualizar benefício",
            description = "Atualiza os dados de um benefício existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Benefício atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = BeneficioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Benefício não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BeneficioResponseDTO> put(
            @Parameter(description = "ID do benefício", example = "1", required = true)
            @PathVariable("id") Long id,
            @RequestBody BeneficioRequestDTO dto) {

        BeneficioResponseDTO response = beneficioService.atualizar(id, dto);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Listar todos os benefícios",
            description = "Retorna a lista completa de benefícios cadastrados"
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<BeneficioResponseDTO>> get() {
        return ResponseEntity.ok(beneficioService.buscarTodos());
    }

    @Operation(
            summary = "Deletar benefício",
            description = "Remove um benefício existente pelo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Benefício deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Benefício não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do benefício", example = "1", required = true)
            @PathVariable("id") Long id) {

        beneficioService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}