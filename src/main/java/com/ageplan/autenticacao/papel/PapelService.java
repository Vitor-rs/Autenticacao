package com.ageplan.autenticacao.papel;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável por gerenciar as operações relacionadas à entidade {@link Papel}.
 * Fornece métodos para inserir, buscar, atualizar e deletar papéis no sistema.
 */
@Service
public class PapelService {

    private static final String MENSAGEM = "Papel não encontrado";

    @Autowired
    private PapelRepository papelRepository;

    /**
     * Insere um novo papel no sistema.
     *
     * @param papelDTO o DTO contendo os dados do papel a ser inserido
     * @return o DTO do papel inserido
     * @throws IllegalArgumentException se o papel já existir
     */
    public PapelDTO insert(PapelDTO papelDTO) {
        if (papelRepository.existsByNomePapel(papelDTO.getNomePapel())) {
            throw new IllegalArgumentException("Papel já existe");
        }

        Papel papel = new Papel();
        papel.setNomePapel(papelDTO.getNomePapel());

        Papel savedPapel = papelRepository.save(papel);
        return new PapelDTO(savedPapel);
    }

    /**
     * Busca um papel pelo seu identificador.
     *
     * @param id o identificador do papel a ser buscado
     * @return o DTO do papel encontrado
     * @throws EntityNotFoundException se o papel não for encontrado
     */
    public PapelDTO getPapel(Long id) {
        Papel papel = papelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MENSAGEM));
        return new PapelDTO(papel);
    }

    /**
     * Busca todos os papéis cadastrados no sistema.
     *
     * @return uma lista de DTOs dos papéis encontrados
     */
    public List<PapelDTO> getAllPapeis() {
        return papelRepository.findAll().stream()
                .map(PapelDTO::new)
                .toList();
    }

    /**
     * Atualiza os dados de um papel existente.
     *
     * @param id       o identificador do papel a ser atualizado
     * @param papelDTO o DTO contendo os novos dados do papel
     * @return o DTO do papel atualizado
     * @throws EntityNotFoundException se o papel não for encontrado
     */
    public PapelDTO update(Long id, PapelDTO papelDTO) {
        Papel papel = papelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MENSAGEM));

        papel.setNomePapel(papelDTO.getNomePapel());

        Papel updatedPapel = papelRepository.save(papel);
        return new PapelDTO(updatedPapel);
    }

    /**
     * Deleta um papel pelo seu identificador.
     *
     * @param id o identificador do papel a ser deletado
     * @throws EntityNotFoundException se o papel não for encontrado
     */
    public void delete(Long id) {
        if (!papelRepository.existsById(id)) {
            throw new EntityNotFoundException(MENSAGEM);
        }
        papelRepository.deleteById(id);
    }
}