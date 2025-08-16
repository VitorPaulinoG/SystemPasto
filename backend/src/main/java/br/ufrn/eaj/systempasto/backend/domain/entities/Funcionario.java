package br.ufrn.eaj.systempasto.backend.domain.entities;

import br.ufrn.eaj.systempasto.backend.domain.enums.FuncionarioPapel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String nome;
    @NotBlank
    @Column(unique = true)
    private String numero;
    @NotBlank
    @Column(unique = true)
    private String email;
    @NotBlank
    private String senha;
    @NotNull
    @Enumerated(EnumType.STRING)
    private FuncionarioPapel papel;

}
