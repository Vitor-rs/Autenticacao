package com.ageplan.autenticacao.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface de repositório para gerenciar entidades Usuario.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Encontra um Usuario pelo email.
     *
     * @param email o email do Usuario
     * @return um Optional contendo o Usuario encontrado, ou vazio se não encontrado
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Encontra um Usuario pelo nome de usuário.
     *
     * @param nomeUsuario o nome de usuário do Usuario
     * @return um Optional contendo o Usuario encontrado, ou vazio se não encontrado
     */
    Optional<Usuario> findByNomeUsuario(String nomeUsuario);

    /**
     * Verifica se um Usuario existe pelo email.
     *
     * @param email o email a ser verificado
     * @return true se um Usuario com o email fornecido existir, false caso contrário
     */
    boolean existsByEmail(String email);

    /**
     * Verifica se um Usuario existe pelo nome de usuário.
     *
     * @param nomeUsuario o nome de usuário a ser verificado
     * @return true se um Usuario com o nome de usuário fornecido existir, false caso contrário
     */
    boolean existsByNomeUsuario(String nomeUsuario);

    /**
     * Encontra Usuarios cujo nome completo contém a string fornecida.
     *
     * @param nome a string a ser pesquisada no nome completo
     * @return uma lista de Usuarios cujo nome completo contém a string fornecida
     */
    @Query("SELECT u FROM Usuario u WHERE u.nomeCompleto LIKE %:nome%")
    List<Usuario> findByNomeCompletoContaining(@Param("nome") String nome);

    /**
     * Encontra Usuarios pelo papel.
     *
     * @param nomePapel o nome do papel
     * @return uma lista de Usuarios com o papel fornecido
     */
    @Query("SELECT u FROM Usuario u JOIN u.papeis p WHERE p.nomePapel = :nomePapel")
    List<Usuario> findByPapel(@Param("nomePapel") String nomePapel);

    /**
     * Verifica se um Usuario existe pelo email, excluindo um Usuario específico pelo ID.
     *
     * @param email o email a ser verificado
     * @param id    o ID do Usuario a ser excluído
     * @return true se um Usuario com o email fornecido existir, excluindo o Usuario especificado, false caso contrário
     */
    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.email = :email AND u.id != :id")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);

    /**
     * Verifica se um Usuario existe pelo nome de usuário, excluindo um Usuario específico pelo ID.
     *
     * @param nomeUsuario o nome de usuário a ser verificado
     * @param id          o ID do Usuario a ser excluído
     * @return true se um Usuario com o nome de usuário fornecido existir, excluindo o Usuario especificado, false caso contrário
     */
    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.nomeUsuario = :nomeUsuario AND u.id != :id")
    boolean existsByNomeUsuarioAndIdNot(@Param("nomeUsuario") String nomeUsuario, @Param("id") Long id);
}