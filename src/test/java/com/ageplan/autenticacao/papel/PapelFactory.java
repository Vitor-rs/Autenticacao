package com.ageplan.autenticacao.papel;

public class PapelFactory {

    // Construtor privado para prevenir instanciação
    private PapelFactory() {
        throw new AssertionError("A classe utilitária PapelFactory não deve ser instanciada.");
    }

    public static Papel createPapel() {
        Papel papel = new Papel();
        papel.setId(1L);
        papel.setNomePapel(Papel.NomePapel.ROLE_ADMIN);
        return papel;
    }

    public static PapelDTO createPapelDTO() {
        Papel papel = createPapel();
        return new PapelDTO(papel);
    }

    public static Papel createProfessorPapel() {
        Papel papel = new Papel();
        papel.setId(2L);
        papel.setNomePapel(Papel.NomePapel.INSTRUTOR);
        return papel;
    }

    public static PapelDTO createProfessorPapelDTO() {
        Papel papel = createProfessorPapel();
        return new PapelDTO(papel);
    }

    public static Papel createAlunoPapel() {
        Papel papel = new Papel();
        papel.setId(3L);
        papel.setNomePapel(Papel.NomePapel.ALUNO);
        return papel;
    }

    public static PapelDTO createAlunoPapelDTO() {
        Papel papel = createAlunoPapel();
        return new PapelDTO(papel);
    }
}