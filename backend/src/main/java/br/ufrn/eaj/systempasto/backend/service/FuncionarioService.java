package br.ufrn.eaj.systempasto.backend.service;

import br.ufrn.eaj.systempasto.backend.domain.entities.Funcionario;

public interface FuncionarioService {


    boolean existsByNumeroOrEmail(String numero, String email);
    void save(Funcionario funcionario);
}
