package com.example.day7.component;

import com.example.day7.model.Directory;
import com.example.day7.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    DeviceService deviceService;

    @Override
    public void run(String... args) {
        List<String> logs = deviceService.readLogsFromFile("input.txt");
        Map<String, Directory> directoryStructure = deviceService.createDirectoryPathFromLog(logs);
        List<String> smallDirectories = deviceService.getDirectoriesWithSizesSmallerThan(directoryStructure, 100000);
        Integer sizeOfSmallDirectories = deviceService.getSizeOfSmallDirectories(directoryStructure, smallDirectories);
        System.out.println("Total size of small directories: " + sizeOfSmallDirectories);
    }
}
