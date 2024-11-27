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
        // Part 1
        List<String> logs = deviceService.readLogsFromFile("input.txt");
        Map<String, Directory> directoryStructure = deviceService.createDirectoryPathFromLog(logs);
        List<String> smallDirectories = deviceService.getDirectoriesWithSizesSmallerThan(directoryStructure, 100000);
        Integer sizeOfSmallDirectories = deviceService.getSizeOfDirectories(directoryStructure, smallDirectories);
        System.out.println("Total size of small directories: " + sizeOfSmallDirectories);

        // Part 2
        Integer mainDirectorySize = directoryStructure.get("/").getSize();
        Integer neededSpaceToFree = deviceService.getSpaceToFree(mainDirectorySize);
        List<String> bigDirectories = deviceService.getDirectoriesWithSizesBiggerThan(directoryStructure, neededSpaceToFree);
        Integer sizeOfBigSufficientDirectoryToDelete = deviceService.getSmallestSizeBetween(directoryStructure, bigDirectories);
        System.out.println("Size of sufficient directory to clear: " + sizeOfBigSufficientDirectoryToDelete);
    }
}
