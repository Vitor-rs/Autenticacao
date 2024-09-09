package com.ageplan.autenticacao.usuario;

import com.ageplan.autenticacao.papel.Papel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * Data Transfer Object (DTO) que representa um Usuário no sistema.
 * Utilizado para transferir dados entre as camadas da aplicação.
 */
@NoArgsConstructor
@Getter
@Setter
public class UsuarioDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único do usuário.
     */
    private Long id;

    /**
     * Nome completo do usuário.
     */
    private String nomeCompleto;

    /**
     * Nome de usuário utilizado para login.
     */
    private String nomeUsuario;

    /**
     * Endereço de e-mail do usuário.
     */
    private String email;

    /**
     * Senha do usuário.
     * Este campo é utilizado apenas para escrita e não será serializado na resposta JSON.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    /**
     * Conjunto de papéis atribuídos ao usuário, representados pelo enum {@link Papel.NomePapel}.
     */
    private Set<Papel.NomePapel> papeis;

    /**
     * Construtor que aceita uma entidade {@link Usuario}.
     *
     * @param usuario a entidade Usuario
     */
    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nomeCompleto = usuario.getNomeCompleto();
        this.nomeUsuario = usuario.getNomeUsuario();
        this.email = usuario.getEmail();
        // Não incluí a senha aqui por questões de segurança
        this.papeis = usuario.getPapeis().stream()
                .map(Papel::getNomePapel)
                .collect(java.util.stream.Collectors.toSet());
    }

    /**
     * Verifica se dois objetos UsuarioDTO são iguais.
     *
     * @param o o objeto a ser comparado
     * @return true se os objetos forem iguais, false caso contrário
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioDTO that)) return false;
        return java.util.Objects.equals(getId(), that.getId()) &&
                java.util.Objects.equals(getNomeUsuario(), that.getNomeUsuario()) &&
                java.util.Objects.equals(getEmail(), that.getEmail());
    }

    /**
     * Calcula o hash code do objeto UsuarioDTO.
     *
     * @return o hash code do objeto
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(getId(), getNomeUsuario(), getEmail());
    }

    /**
     * Retorna uma representação em string do objeto UsuarioDTO.
     *
     * @return uma string representando o objeto UsuarioDTO
     */
    @Override
    public String toString() {
        return "UsuarioDTO{" +
                "id=" + id +
                ", nomeCompleto='" + nomeCompleto + '\'' +
                ", nomeUsuario='" + nomeUsuario + '\'' +
                ", email='" + email + '\'' +
                ", papeis=" + papeis +
                '}';
    }
}