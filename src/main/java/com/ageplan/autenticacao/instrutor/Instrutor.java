package com.ageplan.autenticacao.instrutor;

import com.ageplan.autenticacao.papel.Papel;
import com.ageplan.autenticacao.usuario.Usuario;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue("INSTRUTOR")
public class Instrutor extends Usuario {

    private String especialidade;

    public Instrutor(String nomeCompleto, String nomeUsuario, String email, String senha, String especialidade) {
        super(nomeCompleto, nomeUsuario, email, senha);
        this.especialidade = especialidade;
        this.addPapel(new Papel(Papel.NomePapel.INSTRUTOR));
    }

    public void ministrarAula(String assunto) {
        System.out.println("Ministrando aula sobre " + assunto);
    }
}
