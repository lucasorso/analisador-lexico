/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unesc.mesh.exception;

import com.unesc.mesh.controles.Tokens;

/**
 *
 * @author LucasOrso
 */
public class SemanticoException extends Exception {
    private Tokens token;

    public Tokens getToken() {
        return token;
    }

    public void setToken(Tokens token) {
        this.token = token;
    }
    
    public SemanticoException() {
    }
   
    public SemanticoException(String message, Tokens token) {
        this.token = token;
    }

    public SemanticoException(String message) {
        super(message);
    }

    public SemanticoException(String message, Throwable cause) {
        super(message, cause);
    }

    public SemanticoException(Throwable cause) {
        super(cause);
    }

    public SemanticoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
