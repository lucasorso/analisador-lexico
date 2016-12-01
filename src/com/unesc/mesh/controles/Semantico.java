/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unesc.mesh.controles;

import com.unesc.mesh.util.RegraSemantica;
import com.unesc.mesh.view.MainView;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author LucasOrso
 */
public class Semantico {
    
     /*Tipos*/
    private static final String STRING = "String";
    private static final String INT = "Int";
    private static final String FLOAT = "Float";
    private static final String LITERAL = "Literal";
    private static final String IDENTIFICADOR = "Identificador";
    private static final String CHAR = "Char";
    
    /*Categorias*/
    private static final String VARIAVEL = "Variável";
    private static final String CONSTANTE = "Constante";
    
    public static boolean mStatus = false;
    private final TabelaSemantica tabelaSemantica;
    private final Automato automato;
    public static String erroSemantico;

    /**
     * @param automato
     * Contêm todos sos tokens encontrados na analise léxica
     */
    public Semantico(Automato automato) {
        tabelaSemantica = new TabelaSemantica();
        this.automato = automato;
    }
    
    public void getRegra(int aRegra, Tokens token, TokenNaoTerminal naoTerminal) {
        Tokens tokenVar;
        Tokens tokenTipo;
        switch (RegraSemantica.getRegraSemantica(aRegra)) {
            case REGRA_100:
                /* Validação de constantes com o mesmo nome */
                tokenVar = automato.getTokenPosicao(automato.getListaTokens().indexOf(token) -1);
                //tokenTipo = automato.getTokenPosicao(automato.getListaTokens().indexOf(token) +1);
                if (tabelaSemantica.getNomesVariaveis().contains(tokenVar.getValor())){
                    /* encontrou uma constante com o mesmo nome */
                    if (tabelaSemantica.getTipos().contains(retornaTipo(tokenTipo.getCodigo()))){
                        /* encontrou uma constante o mesmo nome e tipo */
                        seStatusSemantico(true);
                        setErroSemantico("Erro, constante : " + tokenVar.getValor() + " do tipo " +
                                tokenTipo.getValor() + " na linha " + tokenVar.getLinha());
                    }
                } else {
                    /* se não encontrou adiciona na tabela */
                    tokenVar = automato.getTokenPosicao(automato.getListaTokens().indexOf(token) -1);
                    tokenTipo = automato.getTokenPosicao(automato.getListaTokens().indexOf(token) +1);
                    tabelaSemantica.addDados(tokenVar.getValor(), retornaTipo(tokenTipo.getCodigo()), naoTerminal.getValor());
                    populaTabela(tokenVar.getValor(), retornaTipo(tokenTipo.getCodigo()), naoTerminal.getValor());   
                }
                break;
            case REGRA_110:
                /*110 Variáveis não declaradas*/
//                identificador = token.getValor();
//                for (TabelaSemantica ts : tabelaSemantica) {
//                    if (!ts.getNomesVariaveis().contains(identificador)) {
//                        /*se entrou no if é porque já existe uma var declarada*/
//                        setErroSemantico(false);
//                    }
//                }
                break;
            case REGRA_120:
                /* Validação de variáveis com o mesmo nome */
                tokenVar = automato.getTokenPosicao(automato.getListaTokens().indexOf(token) -1);
                //tokenTipo = automato.getTokenPosicao(automato.getListaTokens().indexOf(token) +1);
                if (tabelaSemantica.getNomesVariaveis().contains(tokenVar.getValor())){
                    /* encontrou uma variável com o mesmo nome */
                    //if (tabelaSemantica.getTipos().contains(retornaTipo(tokenTipo.getCodigo()))){
                        /* encontrou uma variável o mesmo nome e tipo */
                        seStatusSemantico(true);
                        setErroSemantico("Erro, variável : " + tokenVar.getValor() + " na linha " + tokenVar.getLinha());
                    //}
                } else {
                    /* se não encontrou adiciona na tabela */
                    tokenVar = automato.getTokenPosicao(automato.getListaTokens().indexOf(token) -1);
                    //tokenTipo = automato.getTokenPosicao(automato.getListaTokens().indexOf(token) +1);
                    tabelaSemantica.addDados(tokenVar.getValor(), retornaTipo(tokenVar.getCodigo()), naoTerminal.getValor());
                    populaTabela(tokenVar.getValor(), retornaTipo(tokenVar.getCodigo()), naoTerminal.getValor());   
                }
                
                
                break;
            case REGRA_130:
                /*#130 função com o mesmo nome*/
                
                break;
            case REGRA_140:
                /*140# Verifica na tabela semântica se existe alguma variável igual,
                caso não tenho a variável não esta declarada no escopo do programa.*/
                
                break;
            case REGRA_150:
                /*150# Verifica se na procedure há o retorno conforme 
                a declaração de inteiro ou string.*/
                
                break;  
        }
    }
    
    private void setErroSemantico(String aErro){
        erroSemantico = aErro != null ? aErro : "Erro semântico desconhecido";
    }
    
    public static String getErroSemantico(){
        return erroSemantico;
    }
    
    public boolean getStatusSemantico() {
        return mStatus;
    }
    
    public void seStatusSemantico(boolean mStatus) {
        Semantico.mStatus = mStatus;
    }
    
    private String retornaTipo(int aCodigo){
        switch (aCodigo) {
            case 36:
                return STRING;
            case 37:
                return INT;
            case 38:
                return FLOAT;
            case 39:
                return LITERAL;
            case 40:
                return IDENTIFICADOR;
            case 41:
                return CHAR;
        }
        return null;
    }
    
    /* Popula a tabela */
    private void populaTabela(String aVariavel, String aTipo, String aCategoria) {
        DefaultTableModel modeloTable;
        modeloTable = (DefaultTableModel) MainView.semantico_jTable1.getModel();
        modeloTable.addRow(new Object[]{aVariavel, aTipo, aCategoria});   
    }
}