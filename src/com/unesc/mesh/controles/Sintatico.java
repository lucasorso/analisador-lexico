/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unesc.mesh.controles;

import static com.sun.org.apache.xalan.internal.lib.ExsltDynamic.map;
import com.unesc.mesh.util.ArquivosUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 *
 * @author Lucas
 */
public class Sintatico {

    private int x;
    private String a;
    private final HashMap<String, Integer> terminais;
    private final HashMap<String, Integer> naoTerminais;
    private final List<Tokens> listTokens;
    private final Stack<Integer> pilha = new Stack<Integer>();
    private final TabelaParsing tabParsing;
    private final ArquivosUtil arquivosUtil = new ArquivosUtil();
    private List<List<Integer>> gramatica = new ArrayList<List<Integer>>();
    public static final int FINAL_DE_ARQUIVO = 54;
    public static final int INICIO_PILHA = 0;
    private ListIterator<Tokens> it;

    Sintatico(List<Tokens> listTokens, HashMap hashMapTokens, HashMap naoTerminais) {
        this.listTokens = listTokens;
        this.terminais = hashMapTokens;
        this.naoTerminais = naoTerminais;
        this.it = listTokens.listIterator();
        tabParsing = new TabelaParsing();
        gramatica = arquivosUtil.adicionarRegrasGramatica();
//        terminais = terminaisOld.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
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
                           Se M(X,a) <> VAZIO então (existe uma regra)
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
        List<Integer> pilhaInicio = new ArrayList<>();
        pilhaInicio = gramatica.get(INICIO_PILHA);
        Collections.reverse(pilhaInicio);
        pilha.add(FINAL_DE_ARQUIVO);
        pilha.addAll(pilhaInicio);
        int i = 0;
        int x = pilha.peek();
        int a = listTokens.get(i).getChave();

        while (x != 54) {
            /*
            Quando for trabalhar com a lista da gramatica
            lembrar que ela começa com 0 e não 1
             */
//            List<Integer> listaInvertida = gramatica.get(listTokens.get(i).getChave() -1);
            System.out.println(pilha.toString());
            System.out.println(x);
            System.out.println(a);
            if (x == 10) {
                pilha.pop();
                x = pilha.peek();
            } else if (terminais.containsValue(x)) {
                if (x == a) {
                    pilha.pop();
                    i++;
                    a = listTokens.get(i).getChave();
                    List<Integer> conteudo = gramatica.get(a);
                    Collections.reverse(conteudo);
                    pilha.addAll(conteudo);
                    x = pilha.peek();
                    continue;
                } else {
                    System.out.println("ERRO !");
                    break;
                }
            } else if (naoTerminais.containsValue(x)) {
                if (tabParsing.getRegra(x, a) != 0) {
                    pilha.pop();
                    List<Integer> conteudo = gramatica.get(tabParsing.getRegra(x, a));
                    Collections.reverse(conteudo);
                    pilha.addAll(conteudo);
                    x = pilha.peek();
                } else {
                    System.out.println("ERRO !");
                    break;
                }
            }
            System.out.println(pilha.toString());
            System.out.println(x);
            System.out.println(a);
            /*Funcção para inverter lista*/
//            Collections.reverse(listaInvertida);
//            pilha.addAll(listaInvertida);
//            System.out.println(pilha.toString());
        }
    }
}
