/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unesc.mesh.controles;

import com.unesc.mesh.util.RegraSemantica;
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
    private Tokens ultimoToken;

    /**
     * @TabelaSemantica
     *
     */
    public Semantico() {
        tabelaSemantica = new TabelaSemantica();
    }
    
    public void getRegra(int aRegra, Tokens token, TokenNaoTerminal naoTerminal) {
        String identificador = null;
        Integer codigo = null;
        switch (RegraSemantica.getRegraSemantica(aRegra)) {
            case REGRA_100:
                /*100 Variáveis com o mesmo nome*/
                identificador = token.getValor();
                codigo = token.getCodigo();
                if (tabelaSemantica.getNomesVariaveis().contains(identificador)) {
                    /*se entrou no if é porque já existe uma var declarada*/
                    setErroSemantico(true);
                } else {
                    /*caso não tenha então é adicionado a tabela*/
                    tabelaSemantica.addDados(identificador, retornaTipo(codigo), retornaCategoria(naoTerminal.getCodigo()));
                }
                break;
            case REGRA_110:
                /*110 Variáveis não declaradas*/
                identificador = token.getValor();
                if (!tabelaSemantica.getNomesVariaveis().contains(identificador)) {
                    setErroSemantico(true);
                }
                break;
            case REGRA_120:
                /*120 uso de variavel inteira em operação aritmética*/
                identificador = token.getValor();
                
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
    
    public boolean getStatusSemantico() {
        return mStatus;
    }

    public void setErroSemantico(boolean mStatus) {
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
    
    private String retornaCategoria(int aCodigo){
        switch (aCodigo) {
            case 60:
                return VARIAVEL;
            case 61:
                return CONSTANTE;
        }
        return null;
    }
}
