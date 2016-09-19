/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unesc.mesh.controles;

import com.sun.istack.internal.Nullable;
import com.unesc.mesh.view.MainView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;

/**
 *
 * @author Gustavo, Lucas
 */
public class Automato {

    private int posicaoAteToken;
    private int numeroInteiro;
    private float numeroFloat;
    private char[] vetorCharCodigo;
    boolean finalDeArquivo = false;
    private HashMap hashMapTokens;
    private JTable token_jTable;
    private List<Tokens> listTokens = new ArrayList<Tokens>();
    String expressaoRegularNumero = "^([+-]?(\\d+\\.)\\d+)$";
//    String expressaoRegularInt = "^[+-]?\\d+$";  

    public Automato(String codigo, HashMap hashMapTokens, JTable token_jTable) {
        this.vetorCharCodigo = (codigo + "$").toCharArray();
        this.hashMapTokens = hashMapTokens;
        this.token_jTable = token_jTable;
        int posicaoAtual = 0;
        inicioAutomato(posicaoAtual);
    }

    private void inicioAutomato(int posicaoAtual) {
        posicaoAteToken = posicaoAtual;
        if (Character.isLetter(vetorCharCodigo[posicaoAtual])) {
            analisaLetra(posicaoAtual);
        } else if (Character.isWhitespace(vetorCharCodigo[posicaoAtual])) {
            inicioAutomato(posicaoAtual + 1);
        } else if (Character.isDigit(vetorCharCodigo[posicaoAtual])) {
            analiseDigito(posicaoAtual);
        } else if (vetorCharCodigo[posicaoAtual] == '$') {
            populaTabela();
        }
        System.out.println(posicaoAtual);
    }

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

    private void analisaLetra(int posicaoAtual) {
        if (Character.isLetter(vetorCharCodigo[posicaoAtual]) || vetorCharCodigo[posicaoAtual] == '_') {
            analisaLetra(posicaoAtual + 1);
        } else {
            verificaToken(posicaoAtual);
        }
    }

    private void analiseDigito(int posicaoAtual) {
        if (Character.isDigit(vetorCharCodigo[posicaoAtual]) || vetorCharCodigo[posicaoAtual] == '.') {
            if (vetorCharCodigo[posicaoAtual - 1] == '.') {
                analiseDigito(posicaoAtual + 1);
            }
        } else {
            verificaNumero(posicaoAtual);
        }
    }

    private void verificaNumero(int posicaoAtual) {
        Object objNumber = typeNumber(getSimboloEncontrado(posicaoAtual));
        numeroFloat = 0;
        numeroInteiro = 0;
        if (objNumber != null) {
            if (objNumber instanceof Integer) {
                numeroFloat = (Float) objNumber;
            } else if (objNumber instanceof Integer) {
                numeroInteiro = (Integer) objNumber;
                
            }
        }
    }

    private void verificaToken(int posicaoAtual ) {
        if (hashMapTokens.containsKey(getSimboloEncontrado(posicaoAtual))) {
            geraToken(getSimboloEncontrado(posicaoAtual), recuperaLinha(posicaoAtual));
        } else {
            gertaTokenDesconhecido();
        }
        inicioAutomato(posicaoAtual);
    }

    private String getSimboloEncontrado(int posicaoAtual) {
        String mString = new String(vetorCharCodigo);
        return String.valueOf(mString.subSequence(posicaoAteToken, posicaoAtual));
    }

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

    private void geraToken(String token, int linha) {
        Tokens novoToken = new Tokens((Integer) hashMapTokens.get(token), token, linha);
        listTokens.add(novoToken);
    }

    private void gertaTokenDesconhecido() {

    }

    private void populaTabela() {
        DefaultTableModel modeloTable;
        modeloTable = (DefaultTableModel) token_jTable.getModel();

        for (int i = 0; i < listTokens.size(); i++) {
            modeloTable.addRow(new Object[]{listTokens.get(i).linha, listTokens.get(i).valor, listTokens.get(i).codigo});
        }
    }
}
