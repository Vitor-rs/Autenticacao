package com.ageplan.autenticacao.usuario;

import com.ageplan.autenticacao.config.exceptions.ErrorResponse;
import com.ageplan.autenticacao.config.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Controlador REST para gerenciar as operações relacionadas à entidade {@link Usuario}.
 * Fornece endpoints para criar, buscar, atualizar e deletar usuários no sistema.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Construtor que injeta o serviço de usuário.
     *
     * @param usuarioService o serviço de usuário a ser injetado
     */
    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Endpoint para criar um novo usuário.
     *
     * @param usuarioDTO o DTO contendo os dados do usuário a ser criado
     * @return uma ResponseEntity contendo o DTO do usuário criado e o status HTTP 201 (Created)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> criarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO novoUsuario = usuarioService.criarUsuario(usuarioDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoUsuario.getId())
                .toUri();
        return ResponseEntity.created(location).body(novoUsuario);
    }

    /**
     * Endpoint para buscar um usuário pelo seu identificador.
     *
     * @param id o identificador do usuário a ser buscado
     * @return uma ResponseEntity contendo o DTO do usuário encontrado e o status HTTP 200 (OK)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR') or #id == authentication.principal.id")
    public ResponseEntity<UsuarioDTO> getUsuario(@PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.getUsuario(id);
        return ResponseEntity.ok(usuario);
    }

    /**
     * Endpoint para buscar o usuário atualmente autenticado.
     *
     * @param authentication o objeto de autenticação contendo os detalhes do usuário autenticado
     * @return uma ResponseEntity contendo o DTO do usuário autenticado e o status HTTP 200 (OK)
     */
    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> getCurrentUser(Authentication authentication) {
        UsuarioDTO usuario = usuarioService.getUsuarioByUsername(authentication.getName());
        return ResponseEntity.ok(usuario);
    }

    /**
     * Endpoint para buscar todos os usuários cadastrados no sistema com paginação.
     *
     * @param pageable o objeto de paginação
     * @return uma ResponseEntity contendo a página de DTOs dos usuários encontrados e o status HTTP 200 (OK)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<Page<UsuarioDTO>> getAllUsuarios(Pageable pageable) {
        Page<UsuarioDTO> usuarios = usuarioService.getAllUsuarios(pageable);
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Endpoint para atualizar os dados de um usuário existente.
     *
     * @param id         o identificador do usuário a ser atualizado
     * @param usuarioDTO o DTO contendo os novos dados do usuário
     * @return uma ResponseEntity contendo o DTO do usuário atualizado e o status HTTP 200 (OK)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO updatedUsuario = usuarioService.update(id, usuarioDTO);
        return ResponseEntity.ok(updatedUsuario);
    }

    /**
     * Endpoint para deletar um usuário pelo seu identificador.
     *
     * @param id o identificador do usuário a ser deletado
     * @return uma ResponseEntity com o status HTTP 204 (No Content)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para registrar um novo usuário no sistema.
     *
     * @param usuarioDTO o DTO contendo os dados do usuário a ser registrado
     * @return uma ResponseEntity contendo o DTO do usuário registrado e o status HTTP 201 (Created)
     */
    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> registrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO novoUsuario = usuarioService.registrarUsuario(usuarioDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoUsuario.getId())
                .toUri();
        return ResponseEntity.created(location).body(novoUsuario);
    }

    /**
     * Manipulador de exceção para IllegalArgumentException.
     *
     * @param e a exceção lançada
     * @return uma ResponseEntity contendo a resposta de erro e o status HTTP 400 (Bad Request)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Manipulador de exceção para ResourceNotFoundException.
     *
     * @param e a exceção lançada
     * @return uma ResponseEntity contendo a resposta de erro e o status HTTP 404 (Not Found)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}