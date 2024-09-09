package com.ageplan.autenticacao.papel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) que representa um Papel no sistema.
 * Utilizado para transferir dados entre as camadas da aplicação.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PapelDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único do papel.
     */
    private Long id;

    /**
     * Nome do papel, representado pelo enum {@link Papel.NomePapel}.
     */
    private Papel.NomePapel nomePapel;

    /**
     * Construtor que aceita uma entidade {@link Papel}.
     *
     * @param papel a entidade Papel
     */
    public PapelDTO(Papel papel) {
        this.id = papel.getId();
        this.nomePapel = papel.getNomePapel();
    }

    /**
     * Verifica se dois objetos PapelDTO são iguais.
     *
     * @param o o objeto a ser comparado
     * @return true se os objetos forem iguais, false caso contrário
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PapelDTO papelDTO)) return false;
        return Objects.equals(getId(), papelDTO.getId()) && getNomePapel() == papelDTO.getNomePapel();
    }

    /**
     * Calcula o hash code do objeto PapelDTO.
     *
     * @return o hash code do objeto
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNomePapel());
    }

    /**
     * Retorna uma representação em string do objeto PapelDTO.
     *
     * @return uma string representando o objeto PapelDTO
     */
    @Override
    public String toString() {
        return "PapelDTO{" +
                "id=" + id +
                ", nomePapel=" + nomePapel +
                '}';
    }
}