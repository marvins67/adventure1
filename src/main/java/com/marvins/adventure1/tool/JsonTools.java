package com.marvins.adventure1.tool;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonTools {
    public static <T> void writeObjectToJsonFile(T object, String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(filePath), object);
            System.out.println("L'objet a été écrit dans le fichier JSON : " + filePath);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture de l'objet en fichier JSON : " + e.getMessage());
        }
    }

    public static <T> T readJsonFileToObject(String filePath, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        T object = null;

        try {
            object = objectMapper.readValue(new File(filePath), clazz);
            System.out.println("L'objet a été lu depuis le fichier JSON : " + filePath);
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier JSON : " + e.getMessage());
        }
        return object;
    }
}