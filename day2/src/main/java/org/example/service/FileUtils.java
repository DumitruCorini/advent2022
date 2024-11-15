package org.example.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileUtils {
    public static List<String> getLinesFromFile(String fileName) {
        List<String> linesFromFile;

        try {
            linesFromFile = Files.readAllLines(Path.of(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return linesFromFile;
    }
}
