/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unesc.mesh.controles;

import com.unesc.mesh.util.RegraSemantica;
import com.unesc.mesh.view.MainView;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author LucasOrso
 */
public class Semantico {
    
     /*Tipos*/
    private static final String STRING = "string";
    private static final String INT = "int";
    private static final String FLOAT = "float";
    private static final String LITERAL = "literal";
    private static final String IDENTIFICADOR = "identificador";
    private static final String CHAR = "char";
    
    /*Categorias*/
    private static final String VARIAVEL = "Variavel";
    private static final String CONSTANTE = "Constante";
    private static final String FUNCAO = "Função";
    
    /*Variáveis de classe*/
    public boolean mStatus = false;
    private TabelaSemantica tabelaSemantica;
    private Automato automato;
    public String erroSemantico;
    private List<TokenNaoTerminal> listNaoTerminaisEncontrados;
    String tipoAtual;
    
    /**
     * @param automato
     * Contêm todos sos tokens encontrados na analise léxica
     */
    public Semantico(Automato automato) {
        tabelaSemantica = new TabelaSemantica();
        listNaoTerminaisEncontrados = new ArrayList<>();
        this.automato = automato;
    }
    
    public void getRegra(int aRegra, Tokens token, TokenNaoTerminal naoTerminal) {
        Tokens tokenVar;
        Tokens tokenTipo;
        listNaoTerminaisEncontrados.add(naoTerminal);
        switch (RegraSemantica.getRegraSemantica(aRegra)) {
            case REGRA_100:
                /**
                 *  TUDO OK NA REGRA DE CONSTANTES
                 *
                 * Validação de constantes com o mesmo nome */
                tokenVar = automato.getTokenPosicao(automato.getListaTokens().indexOf(token) -1);
                tokenTipo = automato.getTokenPosicao(automato.getListaTokens().indexOf(token) +1);
                
                if (tabelaSemantica.getNomesVariaveis().contains(tokenVar.getValor())){
                    /* encontrou uma constante com o mesmo nome */
                    if (tabelaSemantica.getTipos().contains(retornaTipo(tokenTipo.getCodigo()))){
                        /* encontrou uma constante o mesmo nome e tipo */
                        setStatusSemantico(true);
                        setErroSemantico("Erro, Variavel : " + tokenVar.getValor() + " do tipo " +
                                retornaTipo(tokenTipo.getCodigo()) + " na linha " + tokenVar.getLinha());
                    }
                } else {
                    /* se não encontrou adiciona na tabela */
                    tabelaSemantica.addDados(tokenVar.getValor(), retornaTipo(tokenTipo.getCodigo()), retornaCategoria(aRegra, naoTerminal.getCodigo()));
                    populaTabela(tokenVar.getValor(), retornaTipo(tokenTipo.getCodigo()), retornaCategoria(aRegra, naoTerminal.getCodigo()));   
                }
                break;
            case REGRA_110:
                /**
                 *  TUDO OK NA REGRA DE ENCRONTRAR VARIAVEIS
                 * 
                 * Uso de variáveis não declaradas */
                tokenVar = automato.getTokenPosicao(automato.getListaTokens().indexOf(token) -1);
                //tokenTipo = automato.getTokenPosicao(automato.getListaTokens().indexOf(token) +1);
                
                if (!tabelaSemantica.getNomesVariaveis().contains(tokenVar.getValor())){
                    setStatusSemantico(true);
                    setErroSemantico("Erro, Variável : "  + tokenVar.getValor()  + " não encontrada na talbela Semântica");
                }
                
                break;
            case REGRA_120:
                /*
                 *  TUDO OK NA REGRA DE VARIAVEIS
                 *
                 * Validação de variáveis com o mesmo nome */
                tokenVar = automato.getTokenPosicao(automato.getListaTokens().indexOf(token) -1);
                tokenTipo = automato.getTokenPosicao(automato.getListaTokens().indexOf(token) -2);
                
                if (tabelaSemantica.getNomesVariaveis().contains(tokenVar.getValor())){
                    /* encontrou uma variável com o mesmo nome */
                    if (tabelaSemantica.getTipos().contains(tipoAtual)){
                        /* encontrou uma variável o mesmo nome e tipo */
                        setStatusSemantico(true);
                        setErroSemantico("Erro, Variavel : " + tokenVar.getValor() + " do tipo " +
                                tipoAtual + " na linha " + tokenVar.getLinha());
                    }
                } else {
                    /* se não encontrou adiciona na tabela */
                    if (naoTerminal.getCodigo() == 67 || naoTerminal.getCodigo() == 69){
                        tipoAtual = tokenTipo.getValor();
                    }
                    tabelaSemantica.addDados(tokenVar.getValor(), tipoAtual, retornaCategoria(aRegra, naoTerminal.getCodigo()));
                    populaTabela(tokenVar.getValor(), tipoAtual, retornaCategoria(aRegra, naoTerminal.getCodigo()));   
                }
                break;
            case REGRA_130:
                /**
                 *  TUDO OK NA REGRA DE FUÇÕES
                 * 
                 * Validação defunção com o mesmo nome*/
                tokenVar = automato.getTokenPosicao(automato.getListaTokens().indexOf(token) -1);
                tokenTipo = automato.getTokenPosicao(automato.getListaTokens().indexOf(token) -2);
                
                if (tabelaSemantica.getNomesVariaveis().contains(tokenVar.getValor())){
                    /* encontrou uma variável com o mesmo nome */
                    if (tabelaSemantica.getTipos().contains(tipoAtual)){
                        /* encontrou uma variável o mesmo nome e tipo */
                        setStatusSemantico(true);
                        setErroSemantico("Erro, função : " + tokenVar.getValor() + " do tipo " +
                                tokenTipo.getValor() + " na linha " + tokenVar.getLinha());
                    }
                } else {
                    tabelaSemantica.addDados(tokenVar.getValor(), tokenTipo.getValor(), retornaCategoria(aRegra, naoTerminal.getCodigo()));
                    populaTabela(tokenVar.getValor(), tokenTipo.getValor(), retornaCategoria(aRegra, naoTerminal.getCodigo()));   
                }
                break;
            case REGRA_140:
                /**
                 *  TUDO OK
                 * 
                 *  Verificação de chamada de função que não existe
                 */
                tokenVar = automato.getTokenPosicao(automato.getListaTokens().indexOf(token) -1);
                
                if (!tabelaSemantica.getNomesVariaveis().contains(tokenVar.getValor())){
                    setStatusSemantico(true);
                    setErroSemantico("Erro, fução : "  + tokenVar.getValor()  + " não encontrada na talbela Semântica");
                }
                break;
            case REGRA_150:
                /** 
                 *  TUDO OK
                 * 
                 *  Verificação da atribuição correta
                 */
                tokenVar = automato.getTokenPosicao(automato.getListaTokens().indexOf(token) -1);
                tokenTipo = automato.getTokenPosicao(automato.getListaTokens().indexOf(token) +1);
                
                /* Recupera  o tipo da minha variavel*/
                int posicao = tabelaSemantica.getNomesVariaveis().indexOf(tokenVar.getValor());
                String tipo = tabelaSemantica.getTipos().get(posicao);
                
                if (!tipo.equals(retornaTipo(tokenTipo.getCodigo()))){
                    setStatusSemantico(true);
                    setErroSemantico("Erro, variável " + tokenVar.getValor() + " não é permitido receber este tipo");
                }
                break;  
        }
    }
    
    private void setErroSemantico(String aErro){
        erroSemantico = aErro != null ? aErro : "Erro semântico desconhecido";
    }
    
    public String getErroSemantico(){
        return erroSemantico;
    }
    
    public boolean getStatusSemantico() {
        return mStatus;
    }
    
    public void setStatusSemantico(boolean mStatus) {
        this.mStatus = mStatus;
    }
    
    private String retornaCategoria(int aRegra, int aCodigo){
        switch (aRegra) {
            case 100:
                if (aCodigo == 61 || aCodigo == 64 || aCodigo == 65 || aCodigo == 66){
                    return CONSTANTE;
                }
            case 120:
                if (aCodigo == 62 || aCodigo == 64 || aCodigo == 67 || aCodigo == 67 || aCodigo == 69){
                    return VARIAVEL;
                }
            case 130: 
               if (aCodigo == 72){
                    return FUNCAO;
                }
            default:
                return null;
        }
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