package com.ageplan.autenticacao.papel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Entidade que representa os diferentes papéis que um usuário pode ter no sistema.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Papel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único do papel.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do papel, representado pelo enum {@link NomePapel}.
     * Deve ser único e não nulo.
     */
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private NomePapel nomePapel;

    /**
     * Construtor que aceita um {@link NomePapel}.
     *
     * @param nomePapel o nome do papel
     */
    public Papel(NomePapel nomePapel) {
        this.nomePapel = nomePapel;
    }

    /**
     * Verifica se dois objetos Papel são iguais.
     *
     * @param o o objeto a ser comparado
     * @return true se os objetos forem iguais, false caso contrário
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Papel papel)) return false;

        return getId().equals(papel.getId()) && getNomePapel() == papel.getNomePapel();
    }

    /**
     * Calcula o hash code do objeto Papel.
     *
     * @return o hash code do objeto
     */
    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getNomePapel().hashCode();
        return result;
    }

    /**
     * Retorna uma representação em string do objeto Papel.
     *
     * @return uma string representando o objeto Papel
     */
    @Override
    public String toString() {
        return "Papel{" +
                "id=" + id +
                ", nome=" + nomePapel +
                '}';
    }

    /**
     * Enumeração que define os nomes dos papéis.
     */
    public enum NomePapel {
        ADMIN,
        INSTRUTOR
    }
}