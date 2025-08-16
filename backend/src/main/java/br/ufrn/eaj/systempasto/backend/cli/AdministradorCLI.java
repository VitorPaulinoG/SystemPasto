package br.ufrn.eaj.systempasto.backend.cli;

import br.ufrn.eaj.systempasto.backend.domain.entities.Funcionario;
import br.ufrn.eaj.systempasto.backend.domain.enums.FuncionarioPapel;
import br.ufrn.eaj.systempasto.backend.service.FuncionarioService;
import org.jline.reader.LineReader;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class AdministradorCLI {
    private FuncionarioService funcionarioService;

    private LineReader lineReader;

    public AdministradorCLI(FuncionarioService funcionarioService, @Lazy LineReader lineReader) {
        this.funcionarioService = funcionarioService;
        this.lineReader = lineReader;
    }
    @ShellMethod(key = "create-admin", value = "Cria um novo usuário com perfil de ADMIN.")
    public String createAdmin(
            @ShellOption(help = "Nome do Administrador") String nome,
            @ShellOption(help = "Número do Administrador") String numero,
            @ShellOption(help = "Email do Administrador") String email
    ) {
        if (funcionarioService.existsByNumeroOrEmail(numero, email))
            return "Erro: Os campos email e número devem ser únicos.";

        String senha = lineReader.readLine("Digite a senha: ", '*');
        String confirmacaoSenha = lineReader.readLine("Confirme a senha: ", '*');

        if (senha.isBlank() || confirmacaoSenha.isBlank())
            return "Erro: As senhas devem ser preenchidas.";

        if (!senha.equals(confirmacaoSenha))
            return "Erro: As senhas não conferem.";

        var administrador = Funcionario.builder()
                .nome(nome)
                .numero(numero)
                .email(email)
                .senha(senha)
                .papel(FuncionarioPapel.ADMIN)
                .build();

        funcionarioService.save(administrador);
        return "Administrador criado com sucesso!";
    }

}
