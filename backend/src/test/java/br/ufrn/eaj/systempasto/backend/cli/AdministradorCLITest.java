package br.ufrn.eaj.systempasto.backend.cli;

import br.ufrn.eaj.systempasto.backend.service.FuncionarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.shell.test.ShellAssertions;
import org.springframework.shell.test.ShellTestClient;
import org.springframework.shell.test.autoconfigure.ShellTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.util.concurrent.TimeUnit;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ShellTest
@ComponentScan(basePackages = "br.ufrn.eaj.systempasto.backend")
class AdministradorCLITest {
    @Autowired
    private ShellTestClient client;
    @MockitoBean
    private FuncionarioService funcionarioService;

    @Test
    void createAdmin_DeveCadastrarAdministrador_QuandoDadosForemValidos() {
        String nome = "adminName";
        String numero = "12345";
        String email = "admin@email.com";
        String senha = "adminSenha";

        var session = client.interactive().run();

        session.write(session.writeSequence()
                .text(String.format("create-admin --nome %s --numero %s --email %s",
                        nome, numero, email))
                .carriageReturn()
                .build());

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen()).containsText("Digite a senha: ");
        });

        session.write(session.writeSequence()
                .text(senha)
                .carriageReturn()
                .build());

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen()).containsText("Confirme a senha: ");
        });

        session.write(session.writeSequence()
                .text(senha)
                .carriageReturn()
                .build());

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen()).containsText("Administrador criado com sucesso!");
        });
    }

    @Test
    void createAdmin_DeveRetornarErro_QuandoNumeroOuEmailJaExistem() {
        when(funcionarioService.existsByNumeroOrEmail(any(), any())).thenReturn(true);

        String nome = "adminName";
        String numero = "12345";
        String email = "admin@email.com";

        var session = client.interactive().run();

        session.write(session.writeSequence()
                .text(String.format("create-admin --nome %s --numero %s --email %s",
                        nome, numero, email))
                .carriageReturn()
                .build());

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen()).containsText("Erro: Os campos email e número devem ser únicos.");
        });
    }

    @Test
    void createAdmin_DeveRetornarErro_QuandoSenhaForVazia() {
        String nome = "adminName";
        String numero = "12345";
        String email = "admin@email.com";
        String senha = "";

        var session = client.interactive().run();

        session.write(session.writeSequence()
                .text(String.format("create-admin --nome %s --numero %s --email %s",
                        nome, numero, email))
                .carriageReturn()
                .build());

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen()).containsText("Digite a senha: ");
        });

        session.write(session.writeSequence()
                .text(senha)
                .carriageReturn()
                .build());

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen()).containsText("Confirme a senha: ");
        });

        session.write(session.writeSequence()
                .text(senha)
                .carriageReturn()
                .build());

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen()).containsText("Erro: As senhas devem ser preenchidas.");
        });
    }

    @Test
    void createAdmin_DeveRetornarErro_QuandoConfirmacaoSenhaForVazia() {
        String nome = "adminName";
        String numero = "12345";
        String email = "admin@email.com";
        String senha = "adminSenha";
        String confirmacaoSenha = "";

        var session = client.interactive().run();

        session.write(session.writeSequence()
                .text(String.format("create-admin --nome %s --numero %s --email %s",
                        nome, numero, email))
                .carriageReturn()
                .build());

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen()).containsText("Digite a senha: ");
        });

        session.write(session.writeSequence()
                .text(senha)
                .carriageReturn()
                .build());

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen()).containsText("Confirme a senha: ");
        });

        session.write(session.writeSequence()
                .text(confirmacaoSenha)
                .carriageReturn()
                .build());

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen()).containsText("Erro: As senhas devem ser preenchidas.");
        });
    }

    @Test
    void createAdmin_DeveRetornarErro_QuandoSenhasNaoConferem() {
        String nome = "adminName";
        String numero = "12345";
        String email = "admin@email.com";
        String senha = "123wasdqe";
        String confirmacaoSenha = "wasdqe123";

        var session = client.interactive().run();
        session.write(session.writeSequence()
                .text(String.format("create-admin --nome %s --numero %s --email %s",
                        nome, numero, email))
                .carriageReturn()
                .build());

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen()).containsText("Digite a senha: ");
        });

        session.write(session.writeSequence()
                .text(senha)
                .carriageReturn()
                .build());

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen()).containsText("Confirme a senha: ");
        });

        session.write(session.writeSequence()
                .text(confirmacaoSenha)
                .carriageReturn()
                .build());

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen()).containsText("Erro: As senhas não conferem.");
        });
    }

}