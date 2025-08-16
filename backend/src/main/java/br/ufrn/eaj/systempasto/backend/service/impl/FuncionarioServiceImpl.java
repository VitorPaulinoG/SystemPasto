package br.ufrn.eaj.systempasto.backend.service.impl;

import br.ufrn.eaj.systempasto.backend.domain.entities.Funcionario;
import br.ufrn.eaj.systempasto.backend.repository.FuncionarioRepository;
import br.ufrn.eaj.systempasto.backend.security.UsuarioAutenticado;
import br.ufrn.eaj.systempasto.backend.service.FuncionarioService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioServiceImpl implements UserDetailsService, FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    public FuncionarioServiceImpl(
            FuncionarioRepository funcionarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean existsByNumeroOrEmail(String numero, String email) {
        return funcionarioRepository.existsByNumeroOrEmail(numero, email);
    }

    @Override
    public UserDetails loadUserByUsername(String numero) throws UsernameNotFoundException {
        return funcionarioRepository.findByNumero(numero)
                .map(UsuarioAutenticado::new)
                .orElseThrow(() -> new UsernameNotFoundException("Funcionário não encontrado"));
    }

    @Override
    public void save(Funcionario funcionario) {
        funcionario.setSenha(passwordEncoder.encode(funcionario.getSenha()));
        funcionarioRepository.save(funcionario);
    }
}
