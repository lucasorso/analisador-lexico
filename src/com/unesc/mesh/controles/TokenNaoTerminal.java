/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unesc.mesh.controles;

/**
 *
 * @author LucasOrso
 */
public class TokenNaoTerminal {
    private int codigo;
    private String valor;

    public TokenNaoTerminal() {
    }

    public TokenNaoTerminal(int codigo, String valor) {
        this.codigo = codigo;
        this.valor = valor;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "TokenNaoTerminal{" + "codigo=" + codigo + ", valor=" + valor + '}';
    }   
}
