package com.ageplan.autenticacao.usuario;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

/**
 * Classe de testes para o serviço UsuarioService.
 */
@ExtendWith(SpringExtension.class)
class UsuarioServiceTests {

    @InjectMocks
    private UsuarioService service;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private long idExistente;
    private long idInexistente;
    private UsuarioDTO usuarioDTO;
    private Usuario usuario;
    private String nomeUsuarioExistente;
    private String nomeUsuarioInexistente;

    /**
     * Configuração inicial dos testes.
     */
    @BeforeEach
    void setUp() {
        idExistente = 1L;
        idInexistente = 1000L;
        long idDependente = 4L;
        nomeUsuarioExistente = "usuarioExistente";
        nomeUsuarioInexistente = "usuarioInexistente";
        usuario = UsuarioFactory.createUsuario();
        usuarioDTO = UsuarioFactory.createUsuarioDTO();

        // Configuração dos comportamentos simulados
        Mockito.when(usuarioRepository.findById(idExistente)).thenReturn(Optional.of(usuario));
        Mockito.when(usuarioRepository.findById(idInexistente)).thenReturn(Optional.empty());
        Mockito.when(usuarioRepository.findByNomeUsuario(nomeUsuarioExistente)).thenReturn(Optional.of(usuario));
        Mockito.when(usuarioRepository.findByNomeUsuario(nomeUsuarioInexistente)).thenReturn(Optional.empty());
        Mockito.when(usuarioRepository.save(ArgumentMatchers.any(Usuario.class))).thenReturn(usuario);
        Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString())).thenReturn("encodedPassword");

        Mockito.doNothing().when(usuarioRepository).deleteById(idExistente);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(usuarioRepository).deleteById(idInexistente);
        Mockito.doThrow(DataIntegrityViolationException.class).when(usuarioRepository).deleteById(idDependente);
    }

    @Test
    void loadUserByUsernameDeveriaRetornarUserDetailsQuandoUsuarioExiste() {
        UserDetails result = service.loadUserByUsername(nomeUsuarioExistente);
        Assertions.assertNotNull(result);
        Mockito.verify(usuarioRepository).findByNomeUsuario(nomeUsuarioExistente);
    }

    @Test
    void loadUserByUsernameDeveriaLancarUsernameNotFoundExceptionQuandoUsuarioNaoExiste() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(nomeUsuarioInexistente));
        Mockito.verify(usuarioRepository).findByNomeUsuario(nomeUsuarioInexistente);
    }

    @Test
    void criarUsuarioDeveriaRetornarUsuarioDTO() {
        Mockito.when(usuarioRepository.existsByEmail(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(usuarioRepository.existsByNomeUsuario(ArgumentMatchers.anyString())).thenReturn(false);
        UsuarioDTO result = service.criarUsuario(usuarioDTO);
        Assertions.assertNotNull(result);
        Mockito.verify(usuarioRepository).save(ArgumentMatchers.any(Usuario.class));
        Mockito.verify(passwordEncoder).encode(ArgumentMatchers.anyString());
    }

    @Test
    void criarUsuarioDeveriaLancarIllegalArgumentExceptionQuandoEmailJaExiste() {
        Mockito.when(usuarioRepository.existsByEmail(ArgumentMatchers.anyString())).thenReturn(true);
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.criarUsuario(usuarioDTO));
    }

    @Test
    void getUsuarioDeveriaRetornarUsuarioDTOQuandoIdExiste() {
        UsuarioDTO result = service.getUsuario(idExistente);
        Assertions.assertNotNull(result);
        Mockito.verify(usuarioRepository).findById(idExistente);
    }

    @Test
    void getUsuarioDeveriaLancarEntityNotFoundExceptionQuandoIdNaoExiste() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getUsuario(idInexistente));
        Mockito.verify(usuarioRepository).findById(idInexistente);
    }

    @Test
    void getAllUsuariosDeveriaRetornarPaginaDeUsuarioDTO() {
        Page<Usuario> usuariosPage = new PageImpl<>(List.of(usuario)); // Criar uma página simulada
        Mockito.when(usuarioRepository.findAll(Mockito.any(Pageable.class))).thenReturn(usuariosPage);

        Page<UsuarioDTO> result = service.getAllUsuarios(Pageable.unpaged()); // Passar um Pageable válido
        Assertions.assertFalse(result.isEmpty());
        Mockito.verify(usuarioRepository).findAll(Mockito.any(Pageable.class));
    }

    @Test
    void updateDeveriaRetornarUsuarioDTOQuandoAtualizaUsuarioExistente() {
        Mockito.when(usuarioRepository.existsByEmailAndIdNot(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())).thenReturn(false);
        Mockito.when(usuarioRepository.existsByNomeUsuarioAndIdNot(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())).thenReturn(false);

        UsuarioDTO result = service.update(idExistente, usuarioDTO);
        Assertions.assertNotNull(result);
        Mockito.verify(usuarioRepository).save(ArgumentMatchers.any(Usuario.class));
    }

    @Test
    void updateDeveriaLancarIllegalArgumentExceptionQuandoEmailJaExiste() {
        Mockito.when(usuarioRepository.existsByEmailAndIdNot(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())).thenReturn(true);
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(idExistente, usuarioDTO));
    }

    @Test
    void deleteUsuarioDeveriaExecutarQuandoIdExistente() {
        service.deleteUsuario(idExistente);
        Mockito.verify(usuarioRepository).deleteById(idExistente);
    }

    @Test
    void deleteUsuarioDeveriaLancarEntityNotFoundExceptionQuandoIdNaoExiste() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.deleteUsuario(idInexistente));
        Mockito.verify(usuarioRepository).deleteById(idInexistente);
    }
}
