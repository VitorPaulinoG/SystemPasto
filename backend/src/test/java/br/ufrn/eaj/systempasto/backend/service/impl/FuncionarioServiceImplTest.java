package br.ufrn.eaj.systempasto.backend.service.impl;

import br.ufrn.eaj.systempasto.backend.domain.entities.Funcionario;
import br.ufrn.eaj.systempasto.backend.domain.enums.FuncionarioPapel;
import br.ufrn.eaj.systempasto.backend.repository.FuncionarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FuncionarioServiceImplTest {
    @Mock
    private FuncionarioRepository funcionarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private FuncionarioServiceImpl funcionarioService;

    @Test
    void existsByNumeroOrEmail_DeveRetornarTrue_QuandoNumeroDeFuncionarioOuEmailExistir() {

        when(funcionarioRepository.existsByNumeroOrEmail(any(), any())).thenReturn(true);

        var result = funcionarioService.existsByNumeroOrEmail(any(), any());
        verify(funcionarioRepository).existsByNumeroOrEmail(any(), any());
        assertTrue(result);
    }

    @Test
    void existsByNumeroOrEmail_DeveRetornarFalse_QuandoNumeroDeFuncionarioOuEmailExistir() {
        when(funcionarioRepository.existsByNumeroOrEmail(any(), any())).thenReturn(false);
        var result = funcionarioService.existsByNumeroOrEmail(any(), any());
        verify(funcionarioRepository).existsByNumeroOrEmail(any(), any());
        assertFalse(result);
    }

    @Test
    void loadUserByUsername_DeveRetornarUsuarioAutenticado_QuandoNumeroDeFuncionarioExistir() {
        var funcionarioMock = Funcionario.builder()
                    .nome("Fulano")
                    .numero("123")
                    .email("fulano@email.com")
                    .senha("123456")
                    .papel(FuncionarioPapel.ADMIN)
                    .build();

        when(funcionarioRepository.findByNumero(any())).thenReturn(Optional.of(funcionarioMock));

        var result = funcionarioService.loadUserByUsername("123");
        verify(funcionarioRepository).findByNumero(any());
        assertEquals(result.getUsername(), funcionarioMock.getNumero());
    }

    @Test
    void loadUserByUsername_DeveLancarUsernameNotFoundException_QuandoNumeroDeFuncionarioNaoExistir() {
        when(funcionarioRepository.findByNumero(any())).thenReturn(Optional.ofNullable(null));

        assertThrows(UsernameNotFoundException.class, () -> funcionarioService.loadUserByUsername("123"));
        verify(funcionarioRepository).findByNumero(any());
    }

    @Test
    void save_DeveEncriptarSenha_AoSalvarFuncionario() {
        final String senhaPlainText = "123456";
        var funcionario = Funcionario.builder()
                .nome("Fulano")
                .numero("123")
                .email("fulano@email.com")
                .senha(senhaPlainText)
                .papel(FuncionarioPapel.ADMIN)
                .build();

        funcionarioService.save(funcionario);
        var order = inOrder(passwordEncoder, funcionarioRepository);
        order.verify(passwordEncoder).encode(any());
        order.verify(funcionarioRepository).save(funcionario);
    }
}