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
    public static int FINAL_DE_ARQUIVO = 54;
    public static int INICIO_PROGRMA = 58;
    private HashMap<String, Integer> terminais;
    private HashMap<Integer, String> naoTerminais;
    private List<List<Integer>> gramatica;
    private Stack<Integer> pilha;
    private TabelaParsing tabParsing;
    private Semantico semantico;
    private ArquivosUtil arqUtil;
    private String naoTerminalAtual;
    private TokenNaoTerminal tokenNaoTerminal;
    private Automato automato;
    
    public Sintatico(Automato automato) {
        arqUtil = new ArquivosUtil();
        this.pilha = new Stack<>();
        this.semantico = new Semantico(automato);
        this.terminais = arqUtil.adicionarHashMapTokens();
        this.automato = automato;
        this.tabParsing = new TabelaParsing();
        this.naoTerminais = arqUtil.adicionarNaoTerminais();
        this.gramatica = arqUtil.adicionarRegrasGramatica();
        pilha.add(FINAL_DE_ARQUIVO);
        pilha.add(INICIO_PROGRMA);
        /*Adicionado for each para reverter lista das gramaticas*/
        gramatica.stream().forEach((conteudo) -> {
            Collections.reverse(conteudo);
        });
    }

    public boolean analisadorSintatico(JTextArea area, Tokens token) {
        x = pilha.peek();
        if (token.getValor() != null) {
            a = token.getCodigo();
        }
        while (x != FINAL_DE_ARQUIVO) {
            System.out.println(pilha.toString());
            System.out.println(x);
            System.out.println(a);
            area.append("Pilha: " + pilha.toString() + "\n");
            area.append("  x  : " + x + "\n");
            area.append("  a  : " + a + "\n");
                       
            if (x == 10) {
                pilha.pop();
                x = pilha.peek();
            } else if (x < INICIO_PROGRMA) {
                if (x == a) {
                    pilha.pop();
                    System.out.println("Retirou o elemento do topo da pilha");
                    area.append("Retirou elemento da pilha\n");
                    /*Verificação dos tipo de variaveis*/
                    System.out.println("------------------------------");
                    System.out.println(naoTerminalAtual);
                    System.out.println(token.getValor());
                    System.out.println(token.getCodigo());
                    System.out.println("------------------------------");
                    return true;
                } else {
                    System.out.println("ERRO !");
                    area.append("\nErro sintático Linha : " + token.getLinha());
                    return false;
                }
            } else if (x >= 100){
                /*Inicio do semântico*/
                pilha.pop();
                semantico.getRegra(x, token, tokenNaoTerminal);
                if(semantico.getStatusSemantico()){
                    String erroSemantico = semantico.getErroSemantico();
                    area.append(erroSemantico);
                    System.out.println(erroSemantico);
                    return false;
                }
                x = pilha.peek();
            }else if (tabParsing.contemRegra(x, a) || tabParsing.getRegra(x, a) != 0) {
                pilha.pop();
                System.out.println("Regra tabela de parsing: " + tabParsing.getRegra(x, a));
                area.append("Regra tabela de parsing: " + tabParsing.getRegra(x, a) + "\n");
                List<Integer> regra = gramatica.get(tabParsing.getRegra(x, a) -1);
                pilha.addAll(regra);
                /*Verificação do não terminal atual*/
                if (naoTerminais.containsKey(x)){
                    naoTerminalAtual = naoTerminais.get(x);
                    tokenNaoTerminal = new TokenNaoTerminal(x, naoTerminalAtual);
                }
                x = pilha.peek();
            } else {
                System.out.println("ERRO !");
                area.append("\nErro sintático Linha : " + token.getLinha());
                return false;
            }
        }
        System.out.println("Saiu do laço");
        area.append("Saiu do laço");
        return true;
    }
}