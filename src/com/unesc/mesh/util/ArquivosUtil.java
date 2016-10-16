package com.unesc.mesh.util;

import java.io.BufferedReader;
import java.io.File;
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
}
