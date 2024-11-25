package com.example.day6.component;

import com.example.day6.service.CommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    CommunicationService communicationService;

    @Override
    public void run(String... args) {
        String datastreamBuffer = readFile("input.txt").getFirst();
        int markerPosition = communicationService.getMarkerPositionFromDatastreamBuffer(datastreamBuffer);
        System.out.println("Marker position: " + markerPosition);
    }

    private List<String> readFile(String fileName) {
        Resource file = new ClassPathResource(fileName);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.getFile()));

            return reader.lines().toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
