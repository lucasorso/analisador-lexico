/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unesc.mesh.controles;

import com.unesc.mesh.view.MainView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;

/**
 *
 * @author Gustavo, Lucas
 */
public class Automato {

    private int posicaoAteToken;
    private int numeroInteiro;
    private float numeroFloat;
    private final char[] vetorCharCodigo;
    boolean finalDeArquivo = false;
    private HashMap hashMapTokens;
    private JTable token_jTable;
    private List<Tokens> listTokens = new ArrayList<Tokens>();
    String expressaoRegularNumero = "^([+-]?(\\d+\\.)?\\d+)$";

    /*Construtor do automato*/
    public Automato(String codigo, HashMap hashMapTokens, JTable token_jTable) {
        this.vetorCharCodigo = (codigo + "$").toCharArray();
        this.hashMapTokens = hashMapTokens;
        this.token_jTable = token_jTable;
        int posicaoAtual = 0;
        inicioAutomato(posicaoAtual);
    }

    /*Inicio do automato*/
    private void inicioAutomato(int posicaoAtual) {
        posicaoAteToken = posicaoAtual;
        if (Character.isLetter(vetorCharCodigo[posicaoAtual])) {
            analisaPalavraReservada(posicaoAtual);
        } else if (Character.isWhitespace(vetorCharCodigo[posicaoAtual])) {
            inicioAutomato(posicaoAtual + 1);
        } else if (Character.isDigit(vetorCharCodigo[posicaoAtual])) {
            analiseDigito(posicaoAtual);
        } else if (vetorCharCodigo[posicaoAtual] == '_') {
            analisaIdentificador(posicaoAtual);
        } else if (vetorCharCodigo[posicaoAtual] == '>' || vetorCharCodigo[posicaoAtual] == '<' || vetorCharCodigo[posicaoAtual] == '='
                || vetorCharCodigo[posicaoAtual] == '+' || vetorCharCodigo[posicaoAtual] == '-' || vetorCharCodigo[posicaoAtual] == '*'
                || vetorCharCodigo[posicaoAtual] == '/' || vetorCharCodigo[posicaoAtual] == '.' || vetorCharCodigo[posicaoAtual] == ','
                || vetorCharCodigo[posicaoAtual] == ';' || vetorCharCodigo[posicaoAtual] == ':' || vetorCharCodigo[posicaoAtual] == '!'
                || vetorCharCodigo[posicaoAtual] == '{' || vetorCharCodigo[posicaoAtual] == '}' || vetorCharCodigo[posicaoAtual] == '['
                || vetorCharCodigo[posicaoAtual] == ']' || vetorCharCodigo[posicaoAtual] == '|' || vetorCharCodigo[posicaoAtual] == '#'
                || vetorCharCodigo[posicaoAtual] == '§' || vetorCharCodigo[posicaoAtual] == '¬' || vetorCharCodigo[posicaoAtual] == '&') {
            analisaCaracter(posicaoAtual);
        } else if (vetorCharCodigo[posicaoAtual] == '$') {
            geraToken(String.valueOf('$'), recuperaLinha(posicaoAtual));
            populaTabela();
        } 
        System.out.println(posicaoAtual);
    }

    /*
     ========= ANALISADORES DE TOKEN ==========
     ==========================================
     */
    /**/
    private void analisaCaracter(int posicaoAtual) {
        if (vetorCharCodigo[posicaoAtual] == '>' || vetorCharCodigo[posicaoAtual] == '<' || vetorCharCodigo[posicaoAtual] == '='
                || vetorCharCodigo[posicaoAtual] == '+' || vetorCharCodigo[posicaoAtual] == '-' || vetorCharCodigo[posicaoAtual] == '*'
                || vetorCharCodigo[posicaoAtual] == '/' || vetorCharCodigo[posicaoAtual] == '.' || vetorCharCodigo[posicaoAtual] == ','
                || vetorCharCodigo[posicaoAtual] == ';' || vetorCharCodigo[posicaoAtual] == ':' || vetorCharCodigo[posicaoAtual] == '!'
                || vetorCharCodigo[posicaoAtual] == '{' || vetorCharCodigo[posicaoAtual] == '}' || vetorCharCodigo[posicaoAtual] == '['
                || vetorCharCodigo[posicaoAtual] == ']' || vetorCharCodigo[posicaoAtual] == '|' || vetorCharCodigo[posicaoAtual] == '#'
                || vetorCharCodigo[posicaoAtual] == '§' || vetorCharCodigo[posicaoAtual] == '¬' || vetorCharCodigo[posicaoAtual] == '&') {
            analisaCaracter(posicaoAtual +1);
        } else if (vetorCharCodigo[posicaoAtual -1] == vetorCharCodigo[posicaoAtual]){
            analisaCaracter(posicaoAtual +1);
        } else if (vetorCharCodigo[posicaoAtual -1] == vetorCharCodigo[posicaoAtual]){
            verificaToken(posicaoAtual);
        } else{
            gertaTokenDesconhecido(posicaoAtual);
        }
    }

    /**/
    private void analisaIdentificador(int posicaoAtual) {
        if (vetorCharCodigo[posicaoAtual] == '_' || Character.isLetter(vetorCharCodigo[posicaoAtual])) {
            analisaIdentificador(posicaoAtual + 1);
        } else {
            verificaIdentificador(posicaoAtual);
        }
    }

    /**/
    private void analisaPalavraReservada(int posicaoAtual) {
        if (Character.isLetter(vetorCharCodigo[posicaoAtual]) || vetorCharCodigo[posicaoAtual] == '_') {
            analisaPalavraReservada(posicaoAtual + 1);
        } else {
            verificaToken(posicaoAtual);
        }
    }

    /**/
    private void analiseDigito(int posicaoAtual) {
        if (Character.isDigit(vetorCharCodigo[posicaoAtual]) || vetorCharCodigo[posicaoAtual] == '.') {
            analiseDigito(posicaoAtual + 1);
        } else {
            verificaNumero(posicaoAtual);
        }
    }

    /*
     ========= VERIFICADORES DE TOKEN =========
     ==========================================
     */
 /*Verifica identificador e gera Token*/
    private void verificaIdentificador(int posicaoAtual) {
        String identificador = getSimboloEncontrado(posicaoAtual);
        if (identificador.startsWith("_")) {
            geraToken("_identificador", recuperaLinha(posicaoAtual), identificador);
        }
        inicioAutomato(posicaoAtual);
    }

    /*Verifica numero e gera Token*/
    private void verificaNumero(int posicaoAtual) {
        Object objNumber = typeNumber(getSimboloEncontrado(posicaoAtual));
        numeroFloat = 0;
        numeroInteiro = 0;
        if (objNumber != null) {
            if (objNumber instanceof Float) {
                numeroFloat = Float.parseFloat(objNumber.toString());
                geraToken("_numfloat", recuperaLinha(posicaoAtual), numeroFloat);
            } else if (objNumber instanceof Integer) {
                numeroInteiro = Integer.parseInt(objNumber.toString());
                geraToken("_numint", recuperaLinha(posicaoAtual), numeroInteiro);
            }
        }
        inicioAutomato(posicaoAtual);
    }

    /*Verifica palavra reservada e gera Token*/
    private void verificaToken(int posicaoAtual) {
        if (hashMapTokens.containsKey(getSimboloEncontrado(posicaoAtual))) {
            geraToken(getSimboloEncontrado(posicaoAtual), recuperaLinha(posicaoAtual));
        } else {
            gertaTokenDesconhecido(recuperaLinha(posicaoAtual));
        }
        inicioAutomato(posicaoAtual);
    }

    /*
     ========= FUNÇÕES AUXILIARES =========
     ======================================
     */
 /*Retorna a linha onde esta o token encontrado*/
    private int recuperaLinha(int pos) {
        int linha;
        if (pos == 0) {
            linha = 1;
        } else {
            linha = 0;
        }
        try {
            int offs = pos;
            while (offs > 0) {
                offs = Utilities.getRowStart(MainView.codigo_jTextArea, offs) - 1;
                linha++;
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
            Log.gravar(e.getMessage(), Log.LOG);
        }
        return linha;
    }

    /*Retorna o simbolo formado, entre o último */
    private String getSimboloEncontrado(int posicaoAtual) {
        String mString = new String(vetorCharCodigo);
        return String.valueOf(mString.subSequence(posicaoAteToken, posicaoAtual));
    }

    /*Verifica o tipo do numero, se é Integer ou float*/
    private Number typeNumber(String simboloEncontrado) {
        if (simboloEncontrado.matches(expressaoRegularNumero)) {
            if (simboloEncontrado.contains(".")) {
                return Float.parseFloat(simboloEncontrado);
            }
            return Integer.parseInt(simboloEncontrado);
        } else {
            return null;
        }
    }

    /*
     ========= GERADORES DE TOKEN =========
     ======================================
     */
    /*Gera Token com identificador*/
    private void geraToken(String token, int linha, String tokenGenerico) {
        Tokens novoToken = new Tokens((Integer) hashMapTokens.get(token), token, linha);
        listTokens.add(novoToken);
    }

    /*Gera Token com numero float*/
    private void geraToken(String token, int linha, float numFloat) {
        Tokens novoToken = new Tokens((Integer) hashMapTokens.get(token), String.valueOf(numFloat), linha);
        listTokens.add(novoToken);
    }

    /*Gera Token com numero inteiro*/
    private void geraToken(String token, int linha, int numInt) {
        Tokens novoToken = new Tokens((Integer) hashMapTokens.get(token), String.valueOf(numInt), linha);
        listTokens.add(novoToken);
    }

    /*Gera Token com palavra reservada*/
    private void geraToken(String token, int linha) {
        Tokens novoToken = new Tokens((Integer) hashMapTokens.get(token), token, linha);
        listTokens.add(novoToken);
    }

    /*Se não encontrar nenhum token conhecido*/
    private void gertaTokenDesconhecido(int linha) {
        Tokens novoToken = new Tokens((Integer) hashMapTokens.get("desconhecido"), "Desconhecido", linha);
        listTokens.add(novoToken);

    }

    /*Popula a tabela*/
    private void populaTabela() {
        DefaultTableModel modeloTable;
        modeloTable = (DefaultTableModel) token_jTable.getModel();
        for (int i = 0; i < listTokens.size(); i++) {
            modeloTable.addRow(new Object[]{listTokens.get(i).linha, listTokens.get(i).valor, listTokens.get(i).codigo});
        }
    }
}
