package com.ageplan.autenticacao.papel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para gerenciar a entidade {@link Papel}.
 * Fornece métodos para realizar operações de CRUD e consultas específicas na base de dados.
 */
@Repository
public interface PapelRepository extends JpaRepository<Papel, Long> {

    /**
     * Encontra um papel pelo nome.
     *
     * @param nomePapel o nome do papel a ser encontrado
     * @return um {@link Optional} contendo o papel encontrado, ou vazio se não encontrado
     */
    Optional<Papel> findByNomePapel(Papel.NomePapel nomePapel);

    /**
     * Verifica se um papel existe pelo nome.
     *
     * @param nomePapel o nome do papel a ser verificado
     * @return true se um papel com o nome fornecido existir, false caso contrário
     */
    boolean existsByNomePapel(Papel.NomePapel nomePapel);
}