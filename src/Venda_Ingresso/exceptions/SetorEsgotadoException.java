package Venda_Ingresso.exceptions;

public class SetorEsgotadoException extends RuntimeException {
    public SetorEsgotadoException(String mensagem) {
        super(mensagem);
    }
}