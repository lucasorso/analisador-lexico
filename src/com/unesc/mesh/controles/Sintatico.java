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
    public static int INICIO_PROGRMA = 58;
    private String analisando = "Analisando";
    private HashMap<String, Integer> terminais = new HashMap<String, Integer>();
    private HashMap<String, Integer> naoTerminais = new HashMap<String, Integer>();
    private List<List<Integer>> gramatica = new ArrayList<List<Integer>>();
    private List<Tokens> listTokensEncotrados;
    private final Stack<Integer> pilha = new Stack<Integer>();
    private final TabelaParsing tabParsing;
    ArquivosUtil arqUtil = new ArquivosUtil();

    public Sintatico(Automato automato) {
        
        this.terminais = arqUtil.adicionarHashMapTokens();
        this.listTokensEncotrados = automato.getListaTokens();
        this.tabParsing = new TabelaParsing();
        this.naoTerminais = arqUtil.adicionarNaoTerminais();
        this.gramatica = arqUtil.adicionarRegrasGramatica();
        pilha.add(FINAL_DE_ARQUIVO);
        pilha.add(INICIO_PROGRMA);
        /*Adicionado for each para reverter minha das gramaticas*/
        gramatica.stream().forEach((conteudo) -> {
            System.out.println(conteudo.toString());
            Collections.reverse(conteudo);
            System.out.println(conteudo.toString());
        });
    }

    public boolean analisadorSintatico(JTextArea area, Tokens token) throws RuntimeException {
        x = pilha.peek();
        if (token.getValor() != null) {
            a = token.getCodigo();
        }
        while (x != 54) {
            System.out.println(pilha.toString());
            System.out.println(x);
            System.out.println(a);
            area.append("Pilha: " + pilha.toString() + "\n");
            area.append("  x  : " + x + "\n");
            area.append("  a  : " + a + "\n");
            if (x == 10) {
                pilha.pop();
                x = pilha.peek();
            } else if (x < 58) {
                if (x == a) {
                    pilha.pop();
                    System.out.println("Retirou o elemento do topo da pilha");
                    area.append("Retirou elemento da pilha\n");
                    return true;
                } else {
                    System.out.println("ERRO !");
                    area.append("\nErro sintático Linha : " + token.getLinha());
                    return false;
//                    throw new RuntimeException("Exception do CARAMBA!  Não funciona!");
                }
            } else if (tabParsing.contemRegra(x, a) || tabParsing.getRegra(x, a) != 0) {
                pilha.pop();
                System.out.println("Regra tabela de parsing: " + tabParsing.getRegra(x, a));
                area.append("Regra tabela de parsing: " + tabParsing.getRegra(x, a) + "\n");
                List<Integer> regra = gramatica.get(tabParsing.getRegra(x, a) -1);
                pilha.addAll(regra);
                x = pilha.peek();
                gramatica = arqUtil.adicionarRegrasGramatica();
            } else {
                System.out.println("ERRO !");
                area.append("\nErro sintático Linha : " + token.getLinha());
                return false;
//                throw new RuntimeException("Exception do CARAMBA!  Não funciona!");
            }
        }
        System.out.println("Saiu do laço");
        area.append("Saiu do laço");
        return true;
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
