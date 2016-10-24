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
import javax.swing.JTextArea;

/**
 *
 * @author Lucas
 */
public class Sintatico {

    private int x;
    private int a;
    private int i = -1;
    private int j;
    public static int FINAL_DE_ARQUIVO = 54;
    public static int INICIO_PILHA = 0;
    private String analisando = "Analisando";
    private HashMap<String, Integer> terminais = new HashMap<String, Integer>();
    private HashMap<String, Integer> naoTerminais = new HashMap<String, Integer>();
    private List<List<Integer>> gramatica = new ArrayList<List<Integer>>();
    private final List<Tokens> listTokensEncotrados;
    private final Stack<Integer> pilha = new Stack<Integer>();
    private final TabelaParsing tabParsing;
    

    public Sintatico(Automato automato) {
        ArquivosUtil arqUtil = new ArquivosUtil();
        this.terminais = arqUtil.adicionarHashMapTokens();
        this.listTokensEncotrados = automato.getListaTokens();
        this.tabParsing = new TabelaParsing();
        this.naoTerminais = arqUtil.adicionarNaoTerminais();
        this.gramatica = arqUtil.adicionarRegrasGramatica();
        List<Integer> pilhaInicio = new ArrayList<>();
        pilhaInicio = gramatica.get(INICIO_PILHA);
        Collections.reverse(pilhaInicio);
        pilha.add(FINAL_DE_ARQUIVO);
        pilha.addAll(pilhaInicio);
    }
    
    
    public void analisadorSintatico(JTextArea area) throws RuntimeException{
        i++;
        x = pilha.peek();
        if (listTokensEncotrados.get(i) != null){
            a = listTokensEncotrados.get(i).getChave();    
        }
        while (x != 54) {
            /*
            Quando for trabalhar com a lista da gramatica
            lembrar que ela começa com 0 e não 1
             */
//            List<Integer> listaInvertida = gramatica.get(listTokens.get(i).getChave() -1);
            System.out.println(pilha.toString());
            System.out.println(x);
            System.out.println(a);
            area.append("Pilha: " + pilha.toString());
            area.append("\n");
            area.append("  X  : " + x);
            area.append("\n");
            area.append("  A  : " + a);
            area.append("\n");
            if (x == 10) {
                pilha.pop();
                x = pilha.peek();
            } else if (terminais.containsValue(x) && x != -1 && x != 58 && x != 59 && x != 60 && x != 61) {
                if (x == a) {
                    pilha.pop();
                    System.out.println("Saia do Repita !");
                    analisadorSintatico(area);
                } else {
                    System.out.println("ERRO !");
                    area.append("ERRO : " + pilha.toString());
                    area.append("\n");
                    throw new RuntimeException("Exception do CARAMBA!  Não funciona!");
                }
            } else if (naoTerminais.containsValue(x)) {
                if (tabParsing.getRegra(x, a) != 0) {
                    pilha.pop();
                    List<Integer> conteudo = gramatica.get(tabParsing.getRegra(x, a -1));
                    Collections.reverse(conteudo);
                    pilha.addAll(conteudo);
                    x = pilha.peek();
                } else {
                    System.out.println("ERRO !");
                    area.append("ERRO : " + pilha.toString());
                    area.append("\n");
                    throw new RuntimeException("Exception do CARAMBA!  Não funciona!");
                }
            }
        }
        System.out.println("SAIU DO LAÇO");
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
}
