package com.ageplan.autenticacao.usuario;

import com.ageplan.autenticacao.papel.Papel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UsuarioControllerTests {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController)
                .setControllerAdvice(new ExceptionHandlerExceptionResolver()) // Configura o tratamento global de exceções
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void criarUsuarioDeveriaRetornarUsuarioDTO() throws Exception {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNomeCompleto("Admin User");
        usuarioDTO.setNomeUsuario("admin");
        usuarioDTO.setEmail("admin@example.com");
        usuarioDTO.setPapeis(Set.of(Papel.NomePapel.valueOf("ROLE_ADMIN")));

        Mockito.when(usuarioService.criarUsuario(Mockito.any(UsuarioDTO.class))).thenReturn(usuarioDTO);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/usuarios/" + usuarioDTO.getId())))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeCompleto").value("Admin User"))
                .andExpect(jsonPath("$.nomeUsuario").value("admin"))
                .andExpect(jsonPath("$.email").value("admin@example.com"));
    }

    @Test
    void getUsuarioDeveriaRetornarUsuarioDTOQuandoIdExiste() throws Exception {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNomeCompleto("Admin User");
        usuarioDTO.setNomeUsuario("admin");
        usuarioDTO.setEmail("admin@example.com");

        Mockito.when(usuarioService.getUsuario(1L)).thenReturn(usuarioDTO);

        mockMvc.perform(get("/api/usuarios/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeCompleto").value("Admin User"))
                .andExpect(jsonPath("$.nomeUsuario").value("admin"))
                .andExpect(jsonPath("$.email").value("admin@example.com"));
    }

    @Test
    void getUsuarioDeveriaRetornarNotFoundQuandoIdNaoExiste() throws Exception {
        Mockito.when(usuarioService.getUsuario(1L)).thenThrow(new jakarta.persistence.EntityNotFoundException("Usuário não encontrado com id: 1"));

        mockMvc.perform(get("/api/usuarios/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Usuário não encontrado com id: 1"));
    }

    @Test
    void getAllUsuariosDeveriaRetornarListaDeUsuarios() throws Exception {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNomeCompleto("Admin User");
        usuarioDTO.setNomeUsuario("admin");
        usuarioDTO.setEmail("admin@example.com");

        Page<UsuarioDTO> page = new PageImpl<>(List.of(usuarioDTO));

        Mockito.when(usuarioService.getAllUsuarios(Mockito.any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/usuarios")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].nomeCompleto").value("Admin User"))
                .andExpect(jsonPath("$.content[0].nomeUsuario").value("admin"))
                .andExpect(jsonPath("$.content[0].email").value("admin@example.com"));
    }

    @Test
    void updateUsuarioDeveriaRetornarUsuarioDTOQuandoIdExiste() throws Exception {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNomeCompleto("Admin User Updated");
        usuarioDTO.setNomeUsuario("admin");
        usuarioDTO.setEmail("admin_updated@example.com");

        Mockito.when(usuarioService.update(Mockito.eq(1L), Mockito.any(UsuarioDTO.class))).thenReturn(usuarioDTO);

        mockMvc.perform(put("/api/usuarios/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeCompleto").value("Admin User Updated"))
                .andExpect(jsonPath("$.email").value("admin_updated@example.com"));
    }

    @Test
    void deleteUsuarioDeveriaRetornarNoContentQuandoIdExiste() throws Exception {
        Mockito.doNothing().when(usuarioService).deleteUsuario(1L);

        mockMvc.perform(delete("/api/usuarios/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUsuarioDeveriaRetornarNotFoundQuandoIdNaoExiste() throws Exception {
        Mockito.doThrow(new jakarta.persistence.EntityNotFoundException("Usuário não encontrado com id: 1")).when(usuarioService).deleteUsuario(1L);

        mockMvc.perform(delete("/api/usuarios/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Usuário não encontrado com id: 1"));
    }

    @Test
    void criarUsuarioDeveriaRetornarBadRequestQuandoEmailJaExiste() throws Exception {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setEmail("admin@example.com");

        Mockito.when(usuarioService.criarUsuario(Mockito.any(UsuarioDTO.class)))
                .thenThrow(new IllegalArgumentException("Email já está em uso"));

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Email já está em uso"));
    }
}