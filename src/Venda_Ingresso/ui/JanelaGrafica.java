/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Venda_Ingresso.ui;

import Venda_Ingresso.entities.Ingresso;
import Venda_Ingresso.services.GerenciadorIngresso;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Junior
 */
class JanelaGrafica extends JDialog {

    // componentes da tela
    private JPanel painelFundo;
    private JPanel painelBotoes;
    private JTable tabelaIngressos;        // tabela pra mostrar os dados
    private JScrollPane scroll;             // barra de rolagem pra tabela
    private DefaultTableModel modelo;       // modelo que controla os dados da tabela
    private ArrayList<Ingresso> ingressos;  // lista com os ingressos a mostrar
    private GerenciadorIngresso gerenciador; // pra poder voltar pra tela inicial

    // construtor - recebe a lista de ingressos e o gerenciador
    public JanelaGrafica(ArrayList<Ingresso> ingressos, GerenciadorIngresso gerenciador) {
        this.ingressos = ingressos;
        this.gerenciador = gerenciador;
        this.modelo = new DefaultTableModel();

        criarTabela();
        criarComponentes();
        carregarDados(this.ingressos);
    }

    // volta pra tela inicial
    private void voltarParaTelaInicial() {
        setVisible(false); // esconde essa janela primeiro

        // usa invokeLater pra garantir que abre depois que essa fechar
        SwingUtilities.invokeLater(() -> {
            new TelaInicial(null, true, gerenciador).setVisible(true);
            dispose(); // libera recursos dessa janela
        });
    }

    // configura as colunas da tabela
    private void criarTabela() {
        tabelaIngressos = new JTable(modelo);
        tabelaIngressos.setSize(700, 800);

        // adiciona as colunas
        modelo.addColumn("Código");
        modelo.addColumn("Nome");
        modelo.addColumn("Setor");
        modelo.addColumn("Qtd");
        modelo.addColumn("Valor");
        modelo.addColumn("Total");
        modelo.addColumn("Data e Hora");

        // ajusta a largura das colunas
        tabelaIngressos.getColumnModel().getColumn(0).setPreferredWidth(5);   // código
        tabelaIngressos.getColumnModel().getColumn(1).setPreferredWidth(70);  // nome
        tabelaIngressos.getColumnModel().getColumn(2).setPreferredWidth(20);  // setor
        tabelaIngressos.getColumnModel().getColumn(3).setPreferredWidth(1);   // quantidade
        tabelaIngressos.getColumnModel().getColumn(4).setPreferredWidth(15);  // valor unitário
        tabelaIngressos.getColumnModel().getColumn(5).setPreferredWidth(15);  // valor total
        tabelaIngressos.getColumnModel().getColumn(6).setPreferredWidth(70);  // data/hora
    }

    // monta a tela
    private void criarComponentes() {
        painelBotoes = new JPanel();

        scroll = new JScrollPane(tabelaIngressos);
        painelFundo = new JPanel();
        painelFundo.add(scroll);
        painelFundo.add(painelBotoes);
        add(painelFundo);

        JButton btnVoltar = new JButton("Voltar para Tela Inicial");

        btnVoltar.addActionListener((e) -> {
            voltarParaTelaInicial();
        });
        painelBotoes.add(btnVoltar);

        setTitle("Ingressos");
        setLocationRelativeTo(null); // centraliza
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // quando fechar a janela pelo X, também volta pra tela inicial
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                voltarParaTelaInicial();
            }
        });

        pack();
        setSize(600, 600);
    }

    // coloca os dados na tabela
    private void carregarDados(ArrayList<Ingresso> ingressos) {
        modelo.setNumRows(0); // limpa a tabela

        if (ingressos != null && !ingressos.isEmpty()) {
            // percorre cada ingresso da lista
            for (Ingresso ing : ingressos) {
                // adiciona uma linha na tabela com os dados do ingresso
                modelo.addRow(new Object[]{
                        ing.getCodigo(),
                        ing.getNome(),
                        ing.getSetor(),
                        ing.getQuantidade(),
                        String.format("%.2f", ing.getValor()),
                        String.format("%.2f", ing.getValorTotal()),
                        ing.getDataHora()
                });
            }

            // calcula os totais (agora sem usar streams)
            int totalIngressos = 0;
            double totalArrecadado = 0;

            for (Ingresso ing : ingressos) {
                totalIngressos += ing.getQuantidade();
                totalArrecadado += ing.getValorTotal();
            }

            System.out.println("=== RELATÓRIO CARREGADO ===");
            System.out.println("Total de ingressos: " + totalIngressos);
            System.out.println("Total arrecadado: R$ " + String.format("%.2f", totalArrecadado));
        } else {
            System.out.println("Nenhum ingresso para exibir");
        }
    }
}