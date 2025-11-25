package br.com.casagrandi.encurtador.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Trata erros de validação de campos (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Trata URL não encontrada - retorna 404 (NÃO 500)
     */
    @ExceptionHandler(UrlNaoEncontradaException.class)
    public ResponseEntity<Map<String, String>> handleUrlNaoEncontrada(UrlNaoEncontradaException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", ex.getMessage());
        error.put("codigo", "URL_NAO_ENCONTRADA");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Trata IllegalArgumentException - retorna 400 Bad Request
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", ex.getMessage());
        error.put("codigo", "ARGUMENTO_INVALIDO");
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Trata RuntimeException genérica - retorna 400 ou 404 dependendo da mensagem
     * IMPORTANTE: Não converter tudo em 404, pode mascarar erros reais
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        String message = ex.getMessage();
        
        // Se a mensagem indica "não encontrado", retorna 404
        if (message != null && (message.contains("não encontrad") || message.contains("not found"))) {
            error.put("erro", message);
            error.put("codigo", "NAO_ENCONTRADO");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        // Caso contrário, retorna 400 Bad Request
        error.put("erro", message != null ? message : "Erro na requisição");
        error.put("codigo", "ERRO_REQUISICAO");
        logger.warn("RuntimeException tratada: {}", message);
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Trata exceções genéricas - retorna 500
     * Loga o erro para debugging
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", "Erro interno do servidor");
        error.put("codigo", "ERRO_INTERNO");
        logger.error("Erro interno não tratado: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
