/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Venda_Ingresso.ui;

import Venda_Ingresso.services.GerenciadorArquivo;
import Venda_Ingresso.services.GerenciadorIngresso;
import Venda_Ingresso.entities.Ingresso;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author Junior
 */
public class TelaInicial extends JDialog {

    // componentes da tela
    private JPanel painelFundo;
    private JButton btnComprar;
    private JButton btnGerarRelatorio;

    private GerenciadorIngresso gerenciador; // gerencia os ingressos

    // construtor padrão (quando abre o programa)
    public TelaInicial() {
        gerenciador = new GerenciadorIngresso(); // cria o gerenciador
        carregarIngressosSalvos(); // tenta carregar ingressos que já existiam

        // calcula o total de ingressos (somando quantidades)
        int totalQuantidades = 0;
        ArrayList<Ingresso> lista = gerenciador.getIngressos();
        for (Ingresso ing : lista) {
            totalQuantidades += ing.getQuantidade();
        }

        System.out.println("=== TELA INICIAL INICIADA ===");
        System.out.println("Ingressos carregados: " + gerenciador.getIngressos().size() + " itens");
        System.out.println("Total de ingressos (quantidades): " + totalQuantidades);

        criarComponentesTela();
    }

    // construtor usado quando volta de outra tela (recebe o gerenciador já existente)
    public TelaInicial(JanelaCadastroIngresso cadastro, boolean isModal, GerenciadorIngresso gerenciador) {
        super(cadastro, isModal);
        this.gerenciador = gerenciador; // usa o mesmo gerenciador (não cria outro)
        criarComponentesTela();
    }

    // tenta carregar os ingressos do arquivo
    private void carregarIngressosSalvos() {
        GerenciadorArquivo arquivo = new GerenciadorArquivo();
        List<Ingresso> ingressosCarregados = arquivo.desserializar("ingressos.ser");

        if (ingressosCarregados != null && !ingressosCarregados.isEmpty()) {
            gerenciador.setIngressos(new ArrayList<>(ingressosCarregados));
        }
    }

    // monta a tela
    private void criarComponentesTela() {
        btnComprar = new JButton("Comprar Ingresso");
        btnGerarRelatorio = new JButton("Gerar Relatório");

        // ação do botão comprar
        btnComprar.addActionListener((e) -> {
            setVisible(false); // esconde a tela atual

            // abre a tela de compra depois que essa esconder
            SwingUtilities.invokeLater(() -> {
                JanelaCadastroIngresso janelaCadastroIngresso = new JanelaCadastroIngresso(gerenciador);
                janelaCadastroIngresso.setVisible(true);
                dispose(); // fecha essa tela
            });
        });

        // ação do botão gerar relatório
        btnGerarRelatorio.addActionListener((e) -> {
            // pega a lista de ingressos do gerenciador
            ArrayList<Ingresso> listaParaRelatorio = gerenciador.getIngressos();
            if (listaParaRelatorio == null) {
                listaParaRelatorio = new ArrayList<>();
            }

            setVisible(false); // esconde a tela atual

            final ArrayList<Ingresso> listaFinal = listaParaRelatorio;

            // abre a tela de relatório depois que essa esconder
            SwingUtilities.invokeLater(() -> {
                JanelaGrafica janelaGrafica = new JanelaGrafica(listaFinal, gerenciador);
                janelaGrafica.setVisible(true);
                dispose(); // fecha essa tela
            });
        });

        // monta o painel
        painelFundo = new JPanel();
        painelFundo.add(btnComprar);
        painelFundo.add(btnGerarRelatorio);

        add(painelFundo);

        // configurações da janela
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // centraliza
        pack();
        setSize(300, 200);
        setVisible(true);
    }
}