package venda_ingresso.services;

import venda_ingresso.entities.Ingresso;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorArquivo {

    // salva a lista de ingressos em um arquivo
    public void serializar(List<Ingresso> ingressos, String path) {
        try {
            // FileOutputStream escreve bytes no arquivo
            FileOutputStream arquivoBytes = new FileOutputStream(path);
            // ObjectOutputStream converte objetos em bytes
            ObjectOutputStream objetoBytes = new ObjectOutputStream(arquivoBytes);

            objetoBytes.writeObject(ingressos); // escreve a lista inteira de uma vez

            objetoBytes.close();
            arquivoBytes.close();

            System.out.println("Arquivo salvo com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo: " + e.getMessage());
        } finally {
            System.out.println("[LOG] Operação de serialização finalizada");
        }
    }

    // carrega a lista de ingressos de um arquivo
    public List<Ingresso> desserializar(String path) {
        try {
            FileInputStream arquivoBytes = new FileInputStream(path);
            ObjectInputStream objetoBytes = new ObjectInputStream(arquivoBytes);

            // quando lemos, precisamos converter de volta para List<Ingresso>
            List<Ingresso> listaIngressos = (List<Ingresso>) objetoBytes.readObject();

            objetoBytes.close();
            arquivoBytes.close();

            System.out.println("Arquivo lido com sucesso!");
            return listaIngressos;

        } catch (IOException e) {
            // erro de leitura/escrita (arquivo não existe, permissão negada, etc)
            System.out.println("Erro de IO: " + e.getMessage());
            return new ArrayList<>(); // retorna lista vazia pra não quebrar o programa

        } catch (ClassNotFoundException e) {
            // erro se a classe Ingresso mudou depois que salvou o arquivo
            System.out.println("Erro de classe: " + e.getMessage());
            return new ArrayList<>();

        } finally {
            System.out.println("[LOG] Operação de desserializar finalizada");
        }
    }

    // gera um arquivo TXT bonitinho pra gente ler
    public void exportarTxt(List<Ingresso> ingressos, String path) {
        try {
            // BufferedWriter é mais eficiente que FileWriter sozinho
            BufferedWriter escritor = new BufferedWriter(new FileWriter(path));

            // cabeçalho do relatório
            escritor.write("=== RELATÓRIO DE INGRESSOS ===");
            escritor.newLine();
            escritor.newLine();

            int totalIngressos = 0;
            double valorTotal = 0.0;

            // percorre cada ingresso da lista
            for (Ingresso ingresso : ingressos) {
                // monta uma linha com todas as informações
                String linha = ingresso.getNome() + " | Setor: " + ingresso.getSetor() +
                        " | Qtd: " + ingresso.getQuantidade() +
                        " | Valor Unit: R$ " + String.format("%.2f", ingresso.getValor()) +
                        " | Total: R$ " + String.format("%.2f", ingresso.getValorTotal());

                escritor.write(linha);
                escritor.newLine();

                // vai acumulando os totais
                totalIngressos += ingresso.getQuantidade();
                valorTotal += ingresso.getValorTotal();
            }

            // rodapé com os totais gerais
            escritor.newLine();
            escritor.write("Total de ingressos (somando quantidades): " + totalIngressos);
            escritor.newLine();
            escritor.write("Valor total arrecadado: R$ " + String.format("%.2f", valorTotal));

            escritor.close();
            System.out.println("Relatório gerado com sucesso!");

        } catch (IOException e) {
            System.out.println("Erro ao gerar relatório: " + e.getMessage());
        } finally {
            System.out.println("[LOG] Operação de exportação finalizada");
        }
    }
}