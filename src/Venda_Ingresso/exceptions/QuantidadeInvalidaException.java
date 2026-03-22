package Venda_Ingresso.exceptions;

public class QuantidadeInvalidaException extends RuntimeException {
    public QuantidadeInvalidaException(String mensagem) {
        super(mensagem);
    }
}