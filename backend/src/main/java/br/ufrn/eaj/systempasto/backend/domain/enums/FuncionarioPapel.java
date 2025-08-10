package br.ufrn.eaj.systempasto.backend.domain.enums;

public enum FuncionarioPapel {
    FUNCIONARIO("Funcionário"),
    ADMIN("Administrador"),;
    private final String papel;
    FuncionarioPapel(String papel) {
        this.papel = papel;
    }

    public String getPapel() {
        return papel;
    }

}
