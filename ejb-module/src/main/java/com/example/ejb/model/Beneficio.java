package com.example.ejb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

import java.math.BigDecimal;


@Entity
@Table(name = "beneficio")
@Data
public class Beneficio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "descricao", length = 255)
    private String descricao;

    @Column(name = "valor", precision = 15, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Version
    @Column(name = "version")
    private Long version;

}
