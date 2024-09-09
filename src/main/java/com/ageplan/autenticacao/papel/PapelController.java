package com.ageplan.autenticacao.papel;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gerenciar as operações relacionadas à entidade {@link Papel}.
 * Fornece endpoints para criar, buscar, atualizar e deletar papéis no sistema.
 */
@RestController
@RequestMapping("/api/papeis")
public class PapelController {

    private final PapelService papelService;

    /**
     * Construtor que injeta o serviço de papel.
     *
     * @param papelService o serviço de papel a ser injetado
     */
    @Autowired
    public PapelController(PapelService papelService) {
        this.papelService = papelService;
    }

    /**
     * Endpoint para criar um novo papel.
     *
     * @param papelDTO o DTO contendo os dados do papel a ser criado
     * @return uma ResponseEntity contendo o DTO do papel criado e o status HTTP 201 (Created)
     */
    @PostMapping
    public ResponseEntity<PapelDTO> criarPapel(@Valid @RequestBody PapelDTO papelDTO) {
        PapelDTO novoPapel = papelService.insert(papelDTO);
        return new ResponseEntity<>(novoPapel, HttpStatus.CREATED);
    }

    /**
     * Endpoint para buscar um papel pelo seu identificador.
     *
     * @param id o identificador do papel a ser buscado
     * @return uma ResponseEntity contendo o DTO do papel encontrado e o status HTTP 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<PapelDTO> getPapel(@PathVariable Long id) {
        PapelDTO papel = papelService.getPapel(id);
        return ResponseEntity.ok(papel);
    }

    /**
     * Endpoint para buscar todos os papéis cadastrados no sistema.
     *
     * @return uma ResponseEntity contendo a lista de DTOs dos papéis encontrados e o status HTTP 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<PapelDTO>> getAllPapeis() {
        List<PapelDTO> papeis = papelService.getAllPapeis();
        return ResponseEntity.ok(papeis);
    }

    /**
     * Endpoint para atualizar os dados de um papel existente.
     *
     * @param id       o identificador do papel a ser atualizado
     * @param papelDTO o DTO contendo os novos dados do papel
     * @return uma ResponseEntity contendo o DTO do papel atualizado e o status HTTP 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<PapelDTO> updatePapel(@PathVariable Long id, @Valid @RequestBody PapelDTO papelDTO) {
        PapelDTO updatedPapel = papelService.update(id, papelDTO);
        return ResponseEntity.ok(updatedPapel);
    }

    /**
     * Endpoint para deletar um papel pelo seu identificador.
     *
     * @param id o identificador do papel a ser deletado
     * @return uma ResponseEntity com o status HTTP 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePapel(@PathVariable Long id) {
        papelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}