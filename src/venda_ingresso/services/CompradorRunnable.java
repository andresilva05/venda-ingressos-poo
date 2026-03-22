package venda_ingresso.services;

import venda_ingresso.entities.Ingresso;
import venda_ingresso.enums.SetorEnum;

public class CompradorRunnable implements Runnable {
    private String nome;
    private SetorEnum setor;
    private int quantidade;
    private GerenciadorIngresso gerenciadorIngresso;

    public CompradorRunnable(String nome, SetorEnum setor, int quantidade, GerenciadorIngresso gerenciadorIngresso) {
        this.nome = nome;
        this.setor = setor;
        this.quantidade = quantidade;
        this.gerenciadorIngresso = gerenciadorIngresso;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(50);

            Ingresso ingresso = new Ingresso() ;

            ingresso.setNome(nome);
            ingresso.setSetor(setor.getDescricao());
            ingresso.setValor(setor.getPreco());
            ingresso.setQuantidade(quantidade);

            gerenciadorIngresso.comprarIngresso(ingresso);

        } catch (InterruptedException interrupt) {
            Thread.currentThread().interrupt();
            return;

        }
    }
}
