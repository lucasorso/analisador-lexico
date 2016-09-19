package com.unesc.mesh.controles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import com.unesc.mesh.view.MainView;

public class Arquivo extends JFrame {

    public File abrirArquivo() {
        try {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fc.showOpenDialog(this);
            File SelFile = fc.getSelectedFile();
            return SelFile;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            Log.gravar(ex.getMessage(), Log.LOG);
        }
        return null;
    }

    public String lerArquivo(String nomeArquivo) {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(nomeArquivo);
            bufferedReader = new BufferedReader(fileReader);
            StringBuilder sb = new StringBuilder();
            while (bufferedReader.ready()) {
                sb.append(bufferedReader.readLine());
                sb.append("\n");
            }
            return sb.toString();
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
            Log.gravar(ex.getMessage(), Log.LOG);
            JOptionPane.showMessageDialog(this, "Erro ao ler o arquivo: " + ex.getMessage());
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (java.io.IOException ex) {
                    Log.gravar(ex.getMessage(), Log.LOG);
                    JOptionPane.showMessageDialog(this, "Erro ao ler o arquivo: " + ex.getMessage());
                }
            }
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (java.io.IOException ex) {
                    Log.gravar(ex.getMessage(), Log.LOG);
                    JOptionPane.showMessageDialog(this, "Erro ao ler o arquivo: " + ex.getMessage());
                }
            }
        }
        return null;
    }

    public boolean gravarArquivo(String textoArquivo) {

        JFileChooser fc = new JFileChooser();
        fc.showSaveDialog(fc);
        File selFile = fc.getSelectedFile();
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileWriter = new FileWriter(selFile.getAbsolutePath() + ".txt");
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(textoArquivo);
            bufferedWriter.flush();
            JOptionPane.showMessageDialog(null, "Salvo com sucesso");
            return true;
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
            Log.gravar(ex.getMessage(), Log.LOG);
            JOptionPane.showMessageDialog(null, "Erro ao salvar o arquivo: " + ex.getMessage());
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (java.io.IOException ex) {
                    ex.printStackTrace();
                    Log.gravar(ex.getMessage(), Log.LOG);
                    JOptionPane.showMessageDialog(null, "Erro ao salvar o arquivo: " + ex.getMessage());
                }
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (java.io.IOException ex) {
                    ex.printStackTrace();
                    Log.gravar(ex.getMessage(), Log.LOG);
                    JOptionPane.showMessageDialog(null, "Erro ao salvar o arquivo: " + ex.getMessage());
                }
            }
        }
        return false;
    }
}
