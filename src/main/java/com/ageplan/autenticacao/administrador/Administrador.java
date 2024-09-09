package com.ageplan.autenticacao.administrador;

import com.ageplan.autenticacao.papel.Papel;
import com.ageplan.autenticacao.usuario.Usuario;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue("ADMIN")
public class Administrador extends Usuario {

    private static final Logger logger = LoggerFactory.getLogger(Administrador.class);

    private String departamento;

    public Administrador(String departamento) {
        super();
        this.departamento = departamento;
        this.addPapel(new Papel(Papel.NomePapel.ADMIN));
        this.addPapel(new Papel(Papel.NomePapel.INSTRUTOR));
    }

    public void gerenciarFinancas() {
        logger.info("Gerenciando finanças");
    }

    public void gerenciarPedagogico() {
        logger.info("Gerenciando aspectos pedagógicos");
    }

    public void ministrarAula(String assunto) {
        if (this.hasPapel(Papel.NomePapel.INSTRUTOR)) {
            logger.info("Administrador ministrando aula sobre {}", assunto);
        } else {
            throw new IllegalStateException("Este administrador não tem permissão para ministrar aulas");
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Administrador that)) return false;
        if (!super.equals(o)) return false;

        return Objects.equals(getDepartamento(), that.getDepartamento());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(getDepartamento());
        return result;
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "departamento='" + departamento + '\'' +
                '}';
    }
}