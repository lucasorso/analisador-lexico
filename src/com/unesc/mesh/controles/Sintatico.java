/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unesc.mesh.controles;

import com.unesc.mesh.util.ArquivosUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Lucas
 */
public final class Sintatico {

    private int x;
    private String a;
    private final HashMap<String, Integer> terminais;
    private final HashMap<String, Integer> naoTerminais;
    private final List<Tokens> listTokens;
    private final Stack<Integer> pilha = new Stack<Integer>();
    private final TabelaParsing tabParsing;
    private final ArquivosUtil arquivosUtil = new ArquivosUtil();
    private List<List<Integer>> gramatica = new ArrayList<List<Integer>>();

    Sintatico(List<Tokens> listTokens, HashMap hashMapTokens, HashMap naoTerminais){
        this.listTokens = listTokens;
        this.terminais = hashMapTokens;
        this.naoTerminais = naoTerminais;
        tabParsing = new TabelaParsing();
        gramatica = arquivosUtil.adicionarRegrasGramatica();
        analisadorSintatico();
    }
    /* Código do Analisador sintático */
    /*
           Início
                   X recebe o topo da pilha
                   “a”  recebe o símbolo da entrada
           Repita
                   Se X=î então
                           Retire o elemento do topo da pilha
                           X recebe o topo da pilha
                   Senão
                   Se X é terminal então
                           Se X=a então
                              Retire o elemento do topo da pilha
                              Sai do Repita
                           Senão
                          Erro
                              Encerra o programa 		
                   Fim Se
                   Senão (* X é não-terminal*)
                           Se M(X,a) <>  então (existe uma regra)
                                    Retire o elemento do topo da pilha 
                                   Coloque o conteúdo da regra na pilha
                                    X recebe o topo da pilha
                           Senão
                                   Erro
                                   Encerra o programa
                           Fim Se
                   Fim Se
           Até X=$ (*pilha vazia, análise concluída*)
           Fim
    */
    public void analisadorSintatico() {
        /*Adicionando o final de arquivo na pilha*/
        pilha.add(listTokens.get(listTokens.size()-1).getChave());

        for (int i = 0; i < listTokens.size(); i++) {
            /*
            Quando for trabalhar com a lista da gramatica
            lembrar que ela começa com 0 e não 1
            */
            List<Integer> listaInvertida = gramatica.get(listTokens.get(i).getChave() -1);
            if (x == 10) {
                pilha.remove(i);
            } else if (terminais.containsKey(listTokens.get(i).getValor())) {
                
            }
            
            Collections.reverse(listaInvertida);
            pilha.addAll(listaInvertida);
            System.out.println(pilha.toString());
        }
    }
}
