/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package venda_ingresso.main;

import venda_ingresso.entities.Ingresso;
import venda_ingresso.enums.SetorEnum;
import venda_ingresso.services.CompradorRunnable;
import venda_ingresso.services.GerenciadorArquivo;
import venda_ingresso.services.GerenciadorIngresso;
import venda_ingresso.ui.TelaInicial;

import java.util.List;

/**
 * @author Junior
 */
public class Principal {

    public static void main(String[] args) {
        GerenciadorIngresso gerenciador = new GerenciadorIngresso();
        GerenciadorArquivo arquivo = new GerenciadorArquivo();

        // M06 - Thread Daemon de salvamento automático a cada 500ms
        Thread daemonThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    arquivo.serializar(gerenciador.getIngressos(), "ingressos_backup.ser");
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                System.out.println("Thread daemon de salvamento interrompida.");
                Thread.currentThread().interrupt(); // restaura o status
            }
        });
        daemonThread.setDaemon(true);
        daemonThread.start();

        // M05 - 4 Threads Compradoras
        Thread t1 = new Thread(new CompradorRunnable("Ana", SetorEnum.AMARELO, 2, gerenciador), "Thread-Ana");
        Thread t2 = new Thread(new CompradorRunnable("Carlos", SetorEnum.AZUL, 3, gerenciador), "Thread-Carlos");
        Thread t3 = new Thread(new CompradorRunnable("Beatriz", SetorEnum.BRANCO, 1, gerenciador), "Thread-Beatriz");
        Thread t4 = new Thread(new CompradorRunnable("João", SetorEnum.VERDE, 4, gerenciador), "Thread-Joao");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Encerrar daemon
        daemonThread.interrupt();

        // Salvar final
        arquivo.serializar(gerenciador.getIngressos(), "ingressos_final.ser");

        // M07 - Desserializar e mostrar threadOrigem (que será null porque é transient)
        List<Ingresso> listaLida = arquivo.desserializar("ingressos_final.ser");
        System.out.println("--- RELATÓRIO FINAL ---");
        for (Ingresso ing : listaLida) {
            System.out.println("Ingresso de " + ing.getNome() + " | Thread Origem: " + ing.getThreadOrigem());
        }
        
        // EXPLICAÇÃO (M07): O valor aparece null pois o atributo "threadOrigem"
        // possui o modificador 'transient'. Campos transient dizem para a máquina
        // Java ignorar a variável no momento da serialização. Ao desserializar, o valor volta com seu default (null).

        // Por fim, pode abrir a interface gráfica original
        TelaInicial telaInicial = new TelaInicial();
    }
}
