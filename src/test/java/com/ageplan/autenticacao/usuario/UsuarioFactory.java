package com.ageplan.autenticacao.usuario;

import com.ageplan.autenticacao.papel.Papel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UsuarioFactory {

    // Construtor privado para evitar instância
    private UsuarioFactory() {
        throw new AssertionError("A classe utilitária UsuarioFactory não deve ser instanciada.");
    }

    public static Usuario createUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNomeCompleto("Admin User");
        usuario.setNomeUsuario("admin");
        usuario.setEmail("admin@example.com");
        usuario.setSenha("admin123");

        Set<Papel> papeis = new HashSet<>();
        papeis.add(createAdminPapel());
        usuario.setPapeis(new ArrayList<>(papeis));

        return usuario;
    }

    public static UsuarioDTO createUsuarioDTO() {
        Usuario usuario = createUsuario();
        return new UsuarioDTO(usuario);
    }

    private static Papel createAdminPapel() {
        Papel papel = new Papel();
        papel.setId(1L);
        papel.setNomePapel(Papel.NomePapel.ROLE_ADMIN);
        return papel;
    }

    public static Usuario createProfessorUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(2L);
        usuario.setNomeCompleto("Professor User");
        usuario.setNomeUsuario("professor");
        usuario.setEmail("professor@example.com");
        usuario.setSenha("professor123");

        Set<Papel> papeis = new HashSet<>();
        papeis.add(createProfessorPapel());
        usuario.setPapeis(new ArrayList<>(papeis));

        return usuario;
    }

    public static UsuarioDTO createProfessorUsuarioDTO() {
        Usuario usuario = createProfessorUsuario();
        return new UsuarioDTO(usuario);
    }

    private static Papel createProfessorPapel() {
        Papel papel = new Papel();
        papel.setId(2L);
        papel.setNomePapel(Papel.NomePapel.INSTRUTOR);
        return papel;
    }

    public static Usuario createAlunoUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(3L);
        usuario.setNomeCompleto("Aluno User");
        usuario.setNomeUsuario("aluno");
        usuario.setEmail("aluno@example.com");
        usuario.setSenha("aluno123");

        Set<Papel> papeis = new HashSet<>();
        papeis.add(createAlunoPapel());
        usuario.setPapeis(new ArrayList<>(papeis));

        return usuario;
    }

    public static UsuarioDTO createAlunoUsuarioDTO() {
        Usuario usuario = createAlunoUsuario();
        return new UsuarioDTO(usuario);
    }

    private static Papel createAlunoPapel() {
        Papel papel = new Papel();
        papel.setId(3L);
        papel.setNomePapel(Papel.NomePapel.ALUNO);
        return papel;
    }
}