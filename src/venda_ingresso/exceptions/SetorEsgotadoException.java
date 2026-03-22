package venda_ingresso.exceptions;

public class SetorEsgotadoException extends RuntimeException {
    public SetorEsgotadoException(String mensagem) {
        super(mensagem);
    }
}