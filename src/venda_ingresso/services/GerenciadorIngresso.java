/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package venda_ingresso.services;

import venda_ingresso.entities.Ingresso;
import venda_ingresso.exceptions.SetorEsgotadoException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import venda_ingresso.enums.SetorEnum;

/**
 *
 * @author Junior
 */
public class GerenciadorIngresso {

    private ArrayList<Ingresso> ingressos;          // lista com todas as compras
    private int prox = 0;                           // próximo código a ser usado
    private Map<String, Integer> ingressosPorSetor; // conta quantos ingressos por setor

    public GerenciadorIngresso() {
        ingressos = new ArrayList<>();
        ingressosPorSetor = new HashMap<>();        // começa com o mapa vazio
    }

    // tenta comprar um ingresso (ou vários de uma vez)
    public boolean comprarIngresso(Ingresso ingresso) {
        if (ingresso == null) {
            return false;  // veio um ingresso vazio, não faz nada
        }

        String setor = ingresso.getSetor();
        int quantidadeComprada = ingresso.getQuantidade();

        // verifica quantos já foram vendidos nesse setor
        int vendidos = ingressosPorSetor.getOrDefault(setor, 0);

        // ve se passa do limite dinâmico do setor
        SetorEnum setorEnum = SetorEnum.fromDescricao(setor);
        if (setorEnum == null) {
            throw new IllegalArgumentException("Setor inválido: " + setor);
        }
        int limiteDoSetor = setorEnum.getLimiteIngressos();

        if (vendidos + quantidadeComprada > limiteDoSetor) {
            // se passar, lança exceção com mensagem explicando
            throw new SetorEsgotadoException("Setor " + setor + " não tem capacidade para " +
                    quantidadeComprada + " ingressos. Disponível: " +
                    (limiteDoSetor - vendidos));
        }

        // dá um novo código pro ingresso (incrementa antes de usar)
        System.out.println("ANTES - prox atual: " + prox);
        ingresso.setCodigo(++prox);
        System.out.println("DEPOIS - novo código: " + prox + " para " + ingresso.getNome());

        // calcula o valor total (preço unitário * quantidade)
        ingresso.setValorTotal(ingresso.getValor() * quantidadeComprada);

        // adiciona na lista (UM objeto só, mesmo com quantidade > 1)
        ingressos.add(ingresso);

        // atualiza o contador do setor
        ingressosPorSetor.put(setor, vendidos + quantidadeComprada);

        System.out.println("Adicionado: " + ingresso.getNome() + " | Setor: " + setor +
                " | Qtd: " + quantidadeComprada + " | Código: " + ingresso.getCodigo());

        return true;  // deu certo
    }

    // retorna a lista com todas as compras
    public ArrayList<Ingresso> getIngressos() {
        return ingressos;
    }

    // substitui a lista inteira (usado quando carrega do arquivo)
    public void setIngressos(ArrayList<Ingresso> ingressos) {
        this.ingressos = ingressos;

        // precisa reconstruir o mapa de setores com os dados carregados
        ingressosPorSetor.clear();

        // percorre cada ingresso da lista
        for (Ingresso ing : ingressos) {
            String setor = ing.getSetor();
            int quantidade = ing.getQuantidade();

            // pega o que já tinha e soma a quantidade desse ingresso
            int contadorAtual = ingressosPorSetor.getOrDefault(setor, 0);
            ingressosPorSetor.put(setor, contadorAtual + quantidade);
        }

        // descobre qual foi o maior código usado até agora
        int maiorCodigo = 0;
        for (Ingresso ing : ingressos) {
            if (ing.getCodigo() > maiorCodigo) {
                maiorCodigo = ing.getCodigo();
            }
        }

        // atualiza o prox pra continuar de onde parou
        this.prox = maiorCodigo;

        System.out.println("=== setIngressos ===");
        System.out.println("Ingressos recebidos: " + ingressos.size() + " itens");
        System.out.println("Maior código encontrado: " + maiorCodigo);
        System.out.println("prox atualizado para: " + this.prox);
    }
}