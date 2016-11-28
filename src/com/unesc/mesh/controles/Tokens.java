/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unesc.mesh.controles;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lucas
 */
public class Tokens {

    int codigo;
    int linha;
    String valor;
    
    public Tokens() {}
    
    public Tokens(int codigo, String valor, int linha) {
        this.codigo = codigo;
        this.valor = valor;
        this.linha = linha;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }
    
    @Override
    public String toString() {
        return "Tokens{" + "chave=" + codigo + ", valor=" + valor + '}';
    }
}
