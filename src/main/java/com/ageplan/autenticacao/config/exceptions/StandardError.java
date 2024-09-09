package com.ageplan.autenticacao.config.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * Classe que representa um erro padrão na aplicação.
 * Esta classe implementa a interface {@link Serializable} para permitir a serialização dos objetos.
 */
@Setter
@NoArgsConstructor
@Getter
public class StandardError implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Instant timestamp;
    private Integer status;
    private String error;
    private String path;
    private String message;
}