package venda_ingresso.exceptions;

public class QuantidadeInvalidaException extends RuntimeException {
    public QuantidadeInvalidaException(String mensagem) {
        super(mensagem);
    }
}