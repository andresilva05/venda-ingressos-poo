/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Venda_Ingresso.ui;

import Venda_Ingresso.entities.Ingresso;
import Venda_Ingresso.enuns.SetorEnum;
import Venda_Ingresso.services.GerenciadorArquivo;
import Venda_Ingresso.services.GerenciadorIngresso;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Junior
 */
public class JanelaCadastroIngresso extends JDialog {

    // componentes da tela
    private JPanel painelFundo;
    private JButton btnSalvar;
    private JButton btnVoltarTelaInicial;
    private JLabel lblNome;
    private JLabel lblQtde;
    private JTextField txtNome;
    private JTextField txtQtde;

    private JComboBox<SetorEnum> cbxSetores;           // combo box com os setores (agora usando enum)
    private String[] tiposTorcedor = {"Inteira", "Meia"};
    private JComboBox<String> cbxTipoTorcedor;

    private GerenciadorIngresso gerenciador;           // recebe de fora (mesmo objeto da tela inicial)
    private String tipoTorcedor = "";

    // construtor - recebe o gerenciador que já existe
    public JanelaCadastroIngresso(GerenciadorIngresso gerenciador) {
        this.gerenciador = gerenciador; // usa o mesmo objeto, não cria um novo
        criarComponentesInsercao();
    }

    // limpa os campos depois de comprar
    private void limpar() {
        txtNome.setText("");
        txtQtde.setText("");
    }

    // monta a tela
    private void criarComponentesInsercao() {
        btnSalvar = new JButton("Salvar");
        btnVoltarTelaInicial = new JButton("Voltar");

        btnSalvar.addActionListener((e) -> {
            comprarIngresso();
        });

        btnVoltarTelaInicial.addActionListener((e) -> {
            // antes de sair, salva os dados no arquivo
            GerenciadorArquivo arquivo = new GerenciadorArquivo();
            arquivo.serializar(gerenciador.getIngressos(), "ingressos.ser");

            // fecha essa janela
            dispose();

            // abre a tela inicial de novo, passando o mesmo gerenciador
            new TelaInicial(this, true, gerenciador).setVisible(true);
        });

        // cria os componentes
        lblNome = new JLabel("Nome:");
        cbxTipoTorcedor = new JComboBox<>(tiposTorcedor);
        lblQtde = new JLabel("Quantidade:");
        txtNome = new JTextField(10);
        txtQtde = new JTextField(5);
        cbxSetores = new JComboBox<>(SetorEnum.values()); // pega todos os setores do enum

        // quando muda o tipo de torcedor (inteira/meia), guarda a escolha
        cbxTipoTorcedor.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    tipoTorcedor = cbxTipoTorcedor.getSelectedItem().toString();
                }
            }
        });

        // monta o painel
        painelFundo = new JPanel();
        painelFundo.add(lblNome);
        painelFundo.add(txtNome);
        painelFundo.add(cbxTipoTorcedor);
        painelFundo.add(lblQtde);
        painelFundo.add(txtQtde);
        painelFundo.add(cbxSetores);
        painelFundo.add(btnSalvar);
        painelFundo.add(btnVoltarTelaInicial);

        add(painelFundo);

        // configurações da janela
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // centraliza
        pack();
        setVisible(true);

        // quando fechar a janela pelo X, também salva
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                GerenciadorArquivo arquivo = new GerenciadorArquivo();
                arquivo.serializar(gerenciador.getIngressos(), "ingressos.ser");
            }
        });
    }

    // processa a compra quando clica em Salvar
    private void comprarIngresso() {
        // verifica se o nome não está vazio
        if (txtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome não pode estar vazio!");
            return;
        }

        // tenta converter a quantidade para número
        int quantidade;
        try {
            quantidade = Integer.parseInt(txtQtde.getText());
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "Quantidade deve ser maior que zero!");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida! Digite apenas números.");
            return;
        }

        // pega o setor que foi selecionado
        SetorEnum setorSelecionado = (SetorEnum) cbxSetores.getSelectedItem();
        double valorUnitario = setorSelecionado.getPreco(); // pega o preço direto do enum

        // vê se é meia entrada
        tipoTorcedor = cbxTipoTorcedor.getSelectedItem().toString();
        if (tipoTorcedor.equalsIgnoreCase("Meia")) {
            valorUnitario = valorUnitario / 2;
        }

        // cria o ingresso com os dados
        Ingresso ingresso = new Ingresso();
        ingresso.setNome(txtNome.getText());
        ingresso.setSetor(setorSelecionado.getDescricao());
        ingresso.setValor(valorUnitario);
        ingresso.setQuantidade(quantidade);
        ingresso.setDataHora(LocalDateTime.now());

        // manda pro gerenciador tentar comprar
        try {
            if (gerenciador.comprarIngresso(ingresso)) {
                limpar();
                JOptionPane.showMessageDialog(this, quantidade + " ingresso(s) comprado(s) com sucesso!");
                System.out.println("=== APÓS COMPRA ===");
                System.out.println("Total no gerenciador: " + gerenciador.getIngressos().size() + " itens");
            }
        } catch (Exception e) {
            // se der alguma exceção (tipo setor esgotado), mostra a mensagem
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
            limpar();
        }
    }
}