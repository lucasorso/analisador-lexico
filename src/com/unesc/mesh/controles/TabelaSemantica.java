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
 * @author LucasOrso
 */
public class TabelaSemantica {
    
    private List<String> nomesVariaveis = new ArrayList<>();
    private List<String> categorias = new ArrayList<>();
    private List<String> tipos = new ArrayList<>();
    private List<Integer> quantidadeParametros = new ArrayList<>();
    
    public void addDados(String aVariavel, String aTipo, String aCategoria) {
        this.nomesVariaveis.add(aVariavel);
        this.tipos.add(aTipo);
        this.categorias.add(aCategoria);
    }

    public List<String> getNomesVariaveis() {
        return nomesVariaveis;
    }

    public void setNomesVariaveis(List<String> nomesVariaveis) {
        this.nomesVariaveis = nomesVariaveis;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public List<String> getTipos() {
        return tipos;
    }

    public void setTipos(List<String> tipos) {
        this.tipos = tipos;
    }

    public List<Integer> getQuantidadeParametros() {
        return quantidadeParametros;
    }

    public void setQuantidadeParametros(List<Integer> quantidadeParametros) {
        this.quantidadeParametros = quantidadeParametros;
    }
    
    @Override
    public String toString() {
        return "TabelaSemantica{" + "nomesVariaveis=" + nomesVariaveis + ", categorias=" + categorias + ", tipos=" + tipos + ", quantidadeParametros=" + quantidadeParametros + '}';
    }
}
