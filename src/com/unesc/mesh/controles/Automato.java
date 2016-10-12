/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unesc.mesh.controles;

import com.unesc.mesh.view.MainView;;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;

/**
 * @author Gustavo, Lucas
 * @see  Trabalho desenvolvido na disciplina de compiladores, solicitado pela professora Msc. Christine Vieira.
 * 
 * @version 1.0
 */
public class Automato {

    /* Variáveis de classe */
    private int posicaoAteToken;
    private Integer numeroInteiro;
    private Float numeroFloat;
    private String comentario;
    private final char[] vetorCharCodigo;
    boolean finalDeArquivo = false;
    private final HashMap hashMapTokens;
    private final JTable token_jTable;
    private final List<Tokens> listTokens = new ArrayList<Tokens>();
//    String expressaoRegularNumero = "^([+-]?(\\d+\\.)?\\d+)$";
    String expressaoRegularNumero = "(([1-9][0-9]*)|(0))([.,][0-9]+)?";

    /* Construtor do automato */
    public Automato(String codigo, HashMap hashMapTokens, JTable token_jTable) {
        this.vetorCharCodigo = (codigo + "$").toCharArray();
        this.hashMapTokens = hashMapTokens;
        this.token_jTable = token_jTable;
        int posicaoAtual = 0;
        inicioAutomato(posicaoAtual);
    }

    /* Inicio do automato */
    private void inicioAutomato(int posicaoAtual) {
        System.gc();
        try {
            posicaoAteToken = posicaoAtual;
            if (Character.isLetter(vetorCharCodigo[posicaoAtual])) {
                analisaPalavraReservada(posicaoAtual + 1);
            } else if (Character.isDigit(vetorCharCodigo[posicaoAtual])) {
                analisaDigito(posicaoAtual + 1);
            } else if (vetorCharCodigo[posicaoAtual] == '_') {
                analisaIdentificador(posicaoAtual + 1);
            } else if (vetorCharCodigo[posicaoAtual] == '>' || vetorCharCodigo[posicaoAtual] == '<' || vetorCharCodigo[posicaoAtual] == '='
                    || vetorCharCodigo[posicaoAtual] == '+' || vetorCharCodigo[posicaoAtual] == '-' || vetorCharCodigo[posicaoAtual] == '*'
                    || vetorCharCodigo[posicaoAtual] == '/' || vetorCharCodigo[posicaoAtual] == '.' || vetorCharCodigo[posicaoAtual] == ','
                    || vetorCharCodigo[posicaoAtual] == ';' || vetorCharCodigo[posicaoAtual] == ':' || vetorCharCodigo[posicaoAtual] == '!'
                    || vetorCharCodigo[posicaoAtual] == '{' || vetorCharCodigo[posicaoAtual] == '}' || vetorCharCodigo[posicaoAtual] == '['
                    || vetorCharCodigo[posicaoAtual] == ']' || vetorCharCodigo[posicaoAtual] == '|' || vetorCharCodigo[posicaoAtual] == '&'
                    || vetorCharCodigo[posicaoAtual] == '(' || vetorCharCodigo[posicaoAtual] == ')') {
                analisaCaracter(posicaoAtual + 1);
            } else if (vetorCharCodigo[posicaoAtual] == '§') {
                while (vetorCharCodigo[posicaoAtual] != '\n') {
                    if (vetorCharCodigo[posicaoAtual] == '$') {
                        inicioAutomato(posicaoAtual);
                        Log.gravar("Comentário: " + comentario.replace("§", " ").trim() + " Linha: " + recuperaLinha(posicaoAtual), Log.LOG);
                    }
                    comentario += vetorCharCodigo[posicaoAtual];
                    posicaoAtual++;
                }
                Log.gravar("Comentário: " + comentario.replace("§", " ").trim() + " Linha: " + recuperaLinha(posicaoAtual), Log.LOG);
                inicioAutomato(posicaoAtual);
            } else if (Character.isWhitespace(vetorCharCodigo[posicaoAtual])) {
                inicioAutomato(posicaoAtual + 1);
            } else if (vetorCharCodigo[posicaoAtual] == '\'') {
                analisaChar(posicaoAtual + 1);
            } else if (vetorCharCodigo[posicaoAtual] == '#') {
                analisaComentarioBloco(posicaoAtual + 1);
            } else if (vetorCharCodigo[posicaoAtual] == '¬'){
                analisaLiteral(posicaoAtual +1);
            } else if (vetorCharCodigo[posicaoAtual] == '\"'){
                analisaString(posicaoAtual +1);
            } else if (vetorCharCodigo[posicaoAtual] == '$' && (posicaoAtual < vetorCharCodigo.length -1)){
                inicioAutomato(posicaoAtual +1);
            } else if (vetorCharCodigo[posicaoAtual] == '$') {
                geraToken(String.valueOf('$'), recuperaLinha(posicaoAtual));
                populaTabela();
            }
            System.out.println(posicaoAtual);
        } catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }
    
    /*
     ==========================================
     ============== ANALISADORES ==============
     ==========================================
    */
    
    /* Analisa String */
    private void analisaString(int posicaoAtual){
        if (vetorCharCodigo[posicaoAtual] == '\"' || Character.isWhitespace(vetorCharCodigo[posicaoAtual])) {
            verificaString(posicaoAtual);
        } 
        if (Character.isDigit(vetorCharCodigo[posicaoAtual]) || Character.isLetter(vetorCharCodigo[posicaoAtual])  ||
                vetorCharCodigo[posicaoAtual] == '_' && vetorCharCodigo[posicaoAtual] != '$'){
            analisaString(posicaoAtual +1);
        } 
        if (vetorCharCodigo[posicaoAtual] == '$'){
            verificaString(posicaoAtual);
        }
    }
    
    /* Analisa Literal */
    private void analisaLiteral(int posicaoAtual){
        if (vetorCharCodigo[posicaoAtual] == '¬') {
            verificaLiteral(posicaoAtual);
        } else if (Character.isDigit(vetorCharCodigo[posicaoAtual]) || Character.isLetter(vetorCharCodigo[posicaoAtual]) || 
                Character.isWhitespace(vetorCharCodigo[posicaoAtual]) && vetorCharCodigo[posicaoAtual] != '$'){
            analisaLiteral(posicaoAtual +1);
        } else if (vetorCharCodigo[posicaoAtual] == '$'){
            verificaLiteral(posicaoAtual);
        }
    }
    
    /* Analisa comentário de bloco */
    private void analisaComentarioBloco(int posicaoAtual) {
        while (vetorCharCodigo[posicaoAtual] !=  '#' && vetorCharCodigo[posicaoAtual] != '$') {
            comentario += vetorCharCodigo[posicaoAtual];
            posicaoAtual++;
        }
        if (vetorCharCodigo[posicaoAtual] == '#'){
            inicioAutomato(posicaoAtual +1);
        } else if (vetorCharCodigo[posicaoAtual] == '$'){
            geraTokenDesconhecido(recuperaLinha(posicaoAtual));
            inicioAutomato(posicaoAtual);
        }
        Log.gravar("Comentário de Bloco: " + comentario.replace("#", " ").trim() + " Linhas: " + recuperaLinha(posicaoAtual), Log.LOG);
    }

    /* Analisa Char */
    private void analisaChar(int posicaoAtual) {
        if (vetorCharCodigo[posicaoAtual] == '\'') {
            verificarChar(posicaoAtual);
        } else if (Character.isDigit(vetorCharCodigo[posicaoAtual]) || Character.isLetter(vetorCharCodigo[posicaoAtual]) && vetorCharCodigo[posicaoAtual] != '$') {
            analisaChar(posicaoAtual + 1);
        }
    }

    /* Analisa caracteres */
    private void analisaCaracter(int posicaoAtual) {
        if (Character.isDigit(vetorCharCodigo[posicaoAtual])) {
            analisaDigito(posicaoAtual + 1);
        }
        if (vetorCharCodigo[posicaoAtual] == '=' || vetorCharCodigo[posicaoAtual] == '+'
                || vetorCharCodigo[posicaoAtual] == '-' || vetorCharCodigo[posicaoAtual] == '|'
                || vetorCharCodigo[posicaoAtual] == '&' || vetorCharCodigo[posicaoAtual] == '.') {
            analisaCaracter(posicaoAtual + 1);
        } else {
            verificaToken(posicaoAtual);
        }
    }

    /* Analisa identificadores */
    private void analisaIdentificador(int posicaoAtual) {
        if (Character.isLetter(vetorCharCodigo[posicaoAtual])) {
            analisaIdentificador(posicaoAtual + 1);
        } else {
            verificaIdentificador(posicaoAtual);
        }
    }

    /* Analisa Palavras reservadas */
    private void analisaPalavraReservada(int posicaoAtual) {
        if (Character.isLetter(vetorCharCodigo[posicaoAtual]) || vetorCharCodigo[posicaoAtual] == '_') {
            analisaPalavraReservada(posicaoAtual + 1);
        } else {
            verificaToken(posicaoAtual);
        }
    }

    /* Analisa digitos */
    private void analisaDigito(int posicaoAtual) {
        if (Character.isDigit(vetorCharCodigo[posicaoAtual]) || vetorCharCodigo[posicaoAtual] == '.') {
            analisaDigito(posicaoAtual + 1);
        } else {
            verificaNumero(posicaoAtual);
        }
    }

    /*
     ==========================================
     ========= VERIFICADORES DE TOKEN =========
     ==========================================
     */
    
    /* Verifica String e gera Token */
    private void verificaString(int posicaoAtual){
        String auxString = String.valueOf(vetorCharCodigo);
        String mString = auxString.substring(posicaoAteToken, posicaoAtual +1);
        if (mString.startsWith("\"") && mString.endsWith("\"")){
            geraToken("_string", recuperaLinha(posicaoAtual), mString.replaceAll("\"", ""));
        } else {
            geraTokenDesconhecido(recuperaLinha(posicaoAtual));
        }
        
        if (vetorCharCodigo[posicaoAtual] == '$'){
            inicioAutomato(posicaoAtual);
        } else {
            inicioAutomato(posicaoAtual + 1);
        }
    }
    
    /* Verifica Literal e gera Token */
    private void verificaLiteral(int posicaoAtual){
        String auxLiteral = String.valueOf(vetorCharCodigo);
        String mLiteral = auxLiteral.substring(posicaoAteToken, posicaoAtual +1);
        if (mLiteral.startsWith("¬") && mLiteral.endsWith("¬")){
            geraToken("_literal", recuperaLinha(posicaoAtual), mLiteral.replaceAll("¬", ""));
        } else {
            geraTokenDesconhecido(posicaoAtual);
        }
        if (vetorCharCodigo[posicaoAtual] == '$'){
            inicioAutomato(posicaoAtual);
        } else {
            inicioAutomato(posicaoAtual + 1);
        }
    }
    
    /* Verifica char e gera Token */
    private void verificarChar(int posicaoAtual) {
        String mChar = getSimboloEncontrado(posicaoAtual).replace('\'', ' ').trim();
        if (mChar.length() == 1) {
            geraToken("_char", recuperaLinha(posicaoAtual), mChar);
        } else {
            geraTokenDesconhecido(posicaoAtual);
        }
        inicioAutomato(posicaoAtual + 1);
    }

    /* Verifica identificador e gera Token  */
    private void verificaIdentificador(int posicaoAtual) {
        String identificador = getSimboloEncontrado(posicaoAtual);
        if (identificador.startsWith("_")) {
            geraToken("_identificador", recuperaLinha(posicaoAtual), identificador);
        } else {
            geraTokenDesconhecido(posicaoAtual);
        }
        inicioAutomato(posicaoAtual);
    }

    /* Verifica numero e gera Token */
    private void verificaNumero(int posicaoAtual) {
        Object objNumber = typeNumber(getSimboloEncontrado(posicaoAtual), posicaoAtual);
        numeroFloat = 0f;
        numeroInteiro = 0;
        if (objNumber != null) {
            if (objNumber instanceof Float) {
                numeroFloat = Float.parseFloat(objNumber.toString());
                geraToken("_numfloat", recuperaLinha(posicaoAtual), numeroFloat);
            } else if (objNumber instanceof Integer) {
                numeroInteiro = Integer.parseInt(objNumber.toString());
                geraToken("_numint", recuperaLinha(posicaoAtual), numeroInteiro);
            }
        } else {
            geraTokenDesconhecido(recuperaLinha(posicaoAtual));
        }
        inicioAutomato(posicaoAtual);
    }

    /* Verifica palavra reservada e gera Token  */
    private void verificaToken(int posicaoAtual) {
        if (hashMapTokens.containsKey(getSimboloEncontrado(posicaoAtual))) {
            geraToken(getSimboloEncontrado(posicaoAtual), recuperaLinha(posicaoAtual));
        } else {
            geraTokenDesconhecido(recuperaLinha(posicaoAtual));
        }
        inicioAutomato(posicaoAtual);
    }

    /*
     ==========================================
     =========== FUNÇÕES AUXILIARES ===========
     ==========================================
    */
    
    /* Retorna a linha onde esta o token encontrado */
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

    /* Retorna o simbolo formado, entre o último */
    private String getSimboloEncontrado(int posicaoAtual) {
        String mString = new String(vetorCharCodigo);
        if (mString.startsWith("§")) {
            return String.valueOf(mString.substring(posicaoAteToken, posicaoAtual));
        }
        return String.valueOf(mString.substring(posicaoAteToken, posicaoAtual));
    }

    /* Verifica o tipo do numero, se é Integer ou float */
    private Number typeNumber(String simboloEncontrado, int posicaoAtual) {
        try {
            if (simboloEncontrado.matches(expressaoRegularNumero)) {
                if (simboloEncontrado.contains(".")) {
                    return Double.parseDouble(simboloEncontrado) > Float.MAX_VALUE ? null : Float.parseFloat(simboloEncontrado);
                }
                return Long.parseLong(simboloEncontrado) > Integer.MAX_VALUE ? null : Integer.parseInt(simboloEncontrado);
            }
        } catch (NumberFormatException e){
            e.printStackTrace();
            Log.gravar(e.getMessage() + "\n" + simboloEncontrado, Log.LOG);
            geraTokenDesconhecido(recuperaLinha(posicaoAtual));
        }
        return null;
    }
    
    /* Popula a tabela */
    private void populaTabela() {
        DefaultTableModel modeloTable;
        modeloTable = (DefaultTableModel) token_jTable.getModel();
        listTokens.stream().forEach((listToken) -> {
            modeloTable.addRow(new Object[]{listToken.linha, listToken.valor, listToken.codigo});
        });
    }

    /*
     ==========================================
     =========== GERADORES DE TOKEN ===========
     ==========================================
    */
    
    /* Gera Token com identificador*/
    private void geraToken(String token, int linha, String tokenGenerico) {
        Tokens novoToken = new Tokens((Integer) hashMapTokens.get(token), tokenGenerico, linha);
        listTokens.add(novoToken);
    }

    /* Gera Token com numero float*/
    private void geraToken(String token, int linha, Float numFloat) {
        Tokens novoToken = new Tokens((Integer) hashMapTokens.get(token), String.valueOf(numFloat), linha);
        listTokens.add(novoToken);
    }

    /* Gera Token com numero inteiro*/
    private void geraToken(String token, int linha, Integer numInt) {
        Tokens novoToken = new Tokens((Integer) hashMapTokens.get(token), String.valueOf(numInt), linha);
        listTokens.add(novoToken);
    }

    /* Gera Token com palavra reservada*/
    private void geraToken(String token, int linha) {
        Tokens novoToken = new Tokens((Integer) hashMapTokens.get(token), token, linha);
        listTokens.add(novoToken);
    }

    /* Se não encontrar nenhum token conhecido*/
    private void geraTokenDesconhecido(int linha) {
        Tokens novoToken = new Tokens((Integer) hashMapTokens.get("desconhecido"), "Desconhecido", linha);
        listTokens.add(novoToken);
    }
}