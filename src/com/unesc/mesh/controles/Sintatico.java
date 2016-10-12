/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unesc.mesh.controles;

/**
 *
 * @author Lucas
 */
public class Sintatico {
    
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
                           Se M(X,a) <>  então (existe uma regra)
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
