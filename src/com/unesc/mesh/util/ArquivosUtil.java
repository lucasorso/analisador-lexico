package com.unesc.mesh.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Lucas
 */
public class ArquivosUtil {

    public List<List<Integer>> adicionarRegrasGramatica() {
        List<List<Integer>> listGramaticaRegra = new ArrayList<List<Integer>>();
        try {
            File file = new File(getClass().getResource("../arquivos/GramaticaCodificada.txt").getFile());
            FileReader fileReader = new FileReader(file.getAbsoluteFile());
            BufferedReader bufferedReader = new LineNumberReader(fileReader);
            while (bufferedReader.ready()) {
                String linha = bufferedReader.readLine();
                int[] valorInt = Arrays.stream(linha.replace("-", "").split("	")).map(String::trim).mapToInt(Integer::parseInt).toArray();
                List<Integer> lisTemp = Arrays.stream(valorInt).boxed().collect(Collectors.toList());
                lisTemp.remove(0);
                listGramaticaRegra.add(lisTemp);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return listGramaticaRegra;
    }

    public HashMap<String, Integer> adicionarHashMapTokens(){

        HashMap<String, Integer> hashMapTokens = new HashMap<String, Integer>();
        try {
            File file = new File(getClass().getResource("../arquivos/tokens.txt").getFile());
            FileReader fileReader = new FileReader(file.getAbsoluteFile());
            BufferedReader bufferedReader = new LineNumberReader(fileReader);
            while (bufferedReader.ready()) {
                String linha = bufferedReader.readLine();
                String[] valor;
                valor = linha.split(" "); //separa por espaço número e palavra
                hashMapTokens.put(valor[0].toString(), Integer.parseInt(valor[1])); // adiciona no ashMap
            }
        } catch (FileNotFoundException ex) {
            System.out.println("ArquivoUtil.java -> Erro ao abrir arquivo : " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("ArquivoUtil.java -> Erro ao manipular arquivo : " + ex.getMessage());
        }
        return hashMapTokens;
    }

    public HashMap<Integer, String> adicionarNaoTerminais(){

        HashMap<Integer, String> hashMapNaoTerminais = new HashMap<>();
        try {
            File file = new File(getClass().getResource("../arquivos/naoTerminaisCodificados.txt").getFile());
            FileReader fileReader = new FileReader(file.getAbsoluteFile());
            BufferedReader bufferedReader = new LineNumberReader(fileReader);
            while (bufferedReader.ready()) {
                String linha = bufferedReader.readLine();
                String[] valor;
                valor = linha.split(" "); //separa por espaço número e palavra
                hashMapNaoTerminais.put(Integer.parseInt(valor[0]), valor[1]); // adiciona no ashMap
            }
        } catch (FileNotFoundException ex) {
            System.out.println("ArquivoUtil.java -> Erro ao abrir arquivo : " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("ArquivoUtil.java -> Erro ao manipular arquivo : " + ex.getMessage());
        }
        return hashMapNaoTerminais;
    }
}
