package com.ageplan.autenticacao.config.exceptions;

import java.io.Serial;

/**
 * Classe de exceção personalizada para lidar com erros relacionados ao banco de dados.
 * Esta classe estende a classe {@link RuntimeException}.
 */
public class DataBaseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constrói uma nova DataBaseException com a mensagem detalhada especificada.
     *
     * @param msg a mensagem detalhada.
     */
    public DataBaseException(String msg) {
        super(msg);
    }
}