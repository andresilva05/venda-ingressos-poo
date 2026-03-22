package Venda_Ingresso.enuns;

public enum SetorEnum {

    AMARELO("Amarelo", 180.0),
    AZUL("Azul", 100.0),
    BRANCO("Branco", 60.0),
    VERDE("Verde", 350.0);

    // final quer dizer que não pode mudar depois de criado
    private final String descricao;
    private final double preco;      // valor do ingresso nesse setor

    // construtor do enum - roda quando cria cada um dos setores acima
    SetorEnum(String descricao, double preco) {
        this.descricao = descricao;
        this.preco = preco;
    }

    public static SetorEnum fromDescricao(String descricao) {
        // percorre todos os setores do enum
        for (SetorEnum setor : SetorEnum.values()) {
            // compara ignorando maiúsculas/minúsculas
            if (setor.getDescricao().equalsIgnoreCase(descricao)) {
                return setor;  // achou, retorna ele
            }
        }
        return null; // não achou nenhum setor com essa descrição
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco() {
        return preco;
    }

    // é isso que aparece quando o Java precisa mostrar o enum como texto
    // exemplo: no JComboBox aparece "Amarelo" em vez de "AMARELO"
    @Override
    public String toString() {
        return descricao;
    }
}