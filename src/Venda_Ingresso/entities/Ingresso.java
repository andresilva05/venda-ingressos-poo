/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Venda_Ingresso.entities;

import Venda_Ingresso.exceptions.QuantidadeInvalidaException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Junior
 */
public class Ingresso implements Serializable {

    private static final long serialVersionUID = 1L;
    private int codigo;
    private String nome;
    private String setor;
    private double valor;
    private int quantidade;
    private double valorTotal;
    private String dataHora;

    public Ingresso() {
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    // verifica se a quantidade é válida (não pode ser negativa ou zero)
    public void setQuantidade(int quantidade) {
        if (quantidade <= 0) {
            // lança uma exceção personalizada que criamos
            throw new QuantidadeInvalidaException("Erro: Quantidade não pode ser menor ou igual a zero!");
        }
        this.quantidade = quantidade;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getDataHora() {
        return dataHora;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    // formata a data e hora para ficar bonito na tela
    public void setDataHora(LocalDateTime dataHora) {
        // formatando a data (dia/mes/ano)
        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        String dataFormatada = formatadorData.format(dataHora);

        // formatando a hora (hora:minuto:segundo)
        DateTimeFormatter formatadorHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        String horaFormatada = formatadorHora.format(dataHora);

        // junta data e hora com um espaço no meio
        this.dataHora = dataFormatada + " " + horaFormatada;
    }

    // esses dois métodos (hashCode e equals) são para comparar ingressos
    @Override
    public int hashCode() {
        int resultado = 5;
        resultado = 17 * resultado + this.codigo;
        return resultado;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Ingresso outro = (Ingresso) obj;
        // dois ingressos são iguais se tiverem o mesmo código
        if (this.codigo != outro.codigo) {
            return false;
        }
        return true;
    }
}