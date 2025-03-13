package com.marvins.adventure1.tool;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileTools {
    public static List<String> getFilesInDirectory(String directoryPath) {
        List<String> fileList = new ArrayList<>();
        File directory = new File(directoryPath);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    // Ajoutez seulement les fichiers (ignorez les sous-répertoires)
                    if (file.isFile()) {
                        fileList.add(file.getName());
                    }
                }
            }
        } else {
            System.err.println("Le chemin spécifié n'est pas un répertoire : " + directoryPath);
        }
        return fileList;
    }

    public static List<String> getFilesInResourceDirectory(String resourceDir) {
        List<String> fileList = new ArrayList<>();
        try {
            Path path = Paths.get(FileTools.class.getClassLoader().getResource(resourceDir).toURI());
            Files.list(path)
                    .filter(Files::isRegularFile) // Filtrer seulement les fichiers
                    .forEach(file -> fileList.add(file.getFileName().toString())); // Ajouter le nom du fichier à la liste
        } catch (IOException | URISyntaxException e) {
            System.err.println("Erreur lors de la récupération des fichiers dans le répertoire de ressources : " + e.getMessage());
        }

        return fileList;
    }
}