package br.ufrn.eaj.systempasto.backend.repository;

import br.ufrn.eaj.systempasto.backend.domain.entities.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Optional<Funcionario> findByNumero(String numero);
    boolean existsByNumeroOrEmail(String numero, String email);
}
