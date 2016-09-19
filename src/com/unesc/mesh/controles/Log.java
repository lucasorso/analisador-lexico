/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unesc.mesh.controles;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Lucas
 */
public class Log {
    
    public static final String LOG = "Log.txt";

    public static void gravar(String mensagem, String logFile) {
        //O segundo parametro "true" indica append para o arquivo em questao.
        try {
            FileWriter fileWriter = new FileWriter(logFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String data = (new java.util.Date()).toString();
            String msg = data + " : " + mensagem + "\n";
            bufferedWriter.write(msg);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
