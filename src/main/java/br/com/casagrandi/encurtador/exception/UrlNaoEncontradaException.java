package br.com.casagrandi.encurtador.exception;

/**
 * Exceção lançada quando uma URL encurtada não é encontrada.
 * Retorna 404 ao invés de 500.
 */
public class UrlNaoEncontradaException extends RuntimeException {
    
    public UrlNaoEncontradaException(String codigo) {
        super("URL não encontrada para o código: " + codigo);
    }
    
    public UrlNaoEncontradaException(String message, Throwable cause) {
        super(message, cause);
    }
}
