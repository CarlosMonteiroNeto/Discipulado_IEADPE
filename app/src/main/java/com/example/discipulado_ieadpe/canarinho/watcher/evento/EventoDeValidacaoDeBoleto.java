package com.example.discipulado_ieadpe.canarinho.watcher.evento;

/**
 * Evento de validação específico para boletos que permite saber qual o bloco que
 * contém caracteres inválidos.
 */
public interface EventoDeValidacaoDeBoleto extends EventoDeValidacao {

    /**
     * Invocado quando os números digitados estão inválidos. Pode ser apenas um trecho ou o número completo.
     *
     * @param valorAtual    O valor após a digitação.
     * @param blocoInvalido O bloco com valor inválido
     */
    void invalido(String valorAtual, int blocoInvalido);

}
