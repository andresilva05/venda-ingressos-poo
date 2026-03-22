package Venda_Ingresso.teste;

import Venda_Ingresso.entities.Ingresso;
import Venda_Ingresso.services.GerenciadorArquivo;


import java.util.ArrayList;

public class TesteTxt {
    public static void main(String[] args) {
        // Carrega os ingressos do arquivo serializado
        GerenciadorArquivo arquivo = new GerenciadorArquivo();
        ArrayList<Ingresso> ingressos = (ArrayList<Ingresso>) arquivo.desserializar("ingressos.ser");

        if (ingressos != null && !ingressos.isEmpty()) {
            // Exporta para TXT
            arquivo.exportarTxt(ingressos, "meu_relatorio.txt");
            System.out.println("Relatório gerado: meu_relatorio.txt");
        } else {
            System.out.println("Nenhum ingresso para exportar");
        }

        // Depois de gerar o arquivo, abre automaticamente
        if (ingressos != null && !ingressos.isEmpty()) {
            arquivo.exportarTxt(ingressos, "meu_relatorio.txt");
            System.out.println("Relatório gerado: meu_relatorio.txt");

            // Tenta abrir o arquivo com o programa padrão (Bloco de Notas)
            try {
                java.awt.Desktop.getDesktop().open(new java.io.File("meu_relatorio.txt"));
            } catch (Exception e) {
                System.out.println("Não foi possível abrir o arquivo automaticamente");
            }
        }
    }
}