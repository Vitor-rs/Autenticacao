package com.ageplan.autenticacao.pessoa;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Classe abstrata que representa uma pessoa no sistema.
 * Esta classe é mapeada como uma superclasse e pode ser estendida por outras entidades.
 */
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
public abstract class Pessoa implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único da pessoa.
     * Gerado automaticamente pela estratégia de identidade.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome completo da pessoa.
     */
    @Setter
    private String nomeCompleto;


    /**
     * Define o identificador da pessoa.
     *
     * @param id o identificador a ser definido
     * @return a própria instância da classe Pessoa
     */
    public Pessoa setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Verifica se dois objetos Pessoa são iguais.
     *
     * @param o o objeto a ser comparado
     * @return true se os objetos forem iguais, false caso contrário
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pessoa pessoa)) return false;

        return Objects.equals(getId(), pessoa.getId()) && Objects.equals(getNomeCompleto(), pessoa.getNomeCompleto());
    }

    /**
     * Calcula o hash code do objeto Pessoa.
     *
     * @return o hash code do objeto
     */
    @Override
    public int hashCode() {
        int result = Objects.hashCode(getId());
        result = 31 * result + Objects.hashCode(getNomeCompleto());
        return result;
    }

    /**
     * Retorna uma representação em string do objeto Pessoa.
     *
     * @return uma string representando o objeto Pessoa
     */
    @Override
    public String toString() {
        return "Pessoa{" +
                "id=" + id +
                ", nomeCompleto='" + nomeCompleto + '\'' +
                '}';
    }
}