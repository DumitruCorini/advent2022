package com.example.day7.service;

import com.example.day7.model.Directory;
import com.example.day7.model.File;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@Service
public class DeviceService {

    private static final Integer MAX_DEVICE_SPACE = 70000000;
    private static final Integer NEEDED_UNUSED_SPACE = 30000000;

    public Map<String, Directory> createDirectoryPathFromLog(List<String> logLines) {
        // Map with:
        // Key: directory path
        // Value: directory with contained files and subDirectories
        Map<String, Directory> directories = new HashMap<>();

        // Create default directory
        directories.put("/", Directory.builder().build());

        boolean isLsMode = false;
        String currentDirectoryPath = "/";

        for (String logLine : logLines) {
            if (logLine.startsWith("$")) {
                isLsMode = false;
            }

            if (isLsMode) {
                treatLSModeForLogLine(logLine, currentDirectoryPath, directories);
            } else {
                // Treat non ls mode
                String[] splitLogLine = logLine.split(" ");
                String instructionToRun = splitLogLine[1];
                if (instructionToRun.equals("ls")) {
                    isLsMode = true;
                } else if (instructionToRun.equals("cd")) {
                    String directoryToChangeInto = splitLogLine[2];
                    if (directoryToChangeInto.equals("..")) {
                        Directory currentDirectory = directories.get(currentDirectoryPath);
                        currentDirectoryPath = currentDirectory.getParentDirectoryPath();
                    } else if (!directoryToChangeInto.equals("/")) {
                        currentDirectoryPath += directoryToChangeInto + "/";
                    }
                }
            }
        }

        return directories;
    }

    public List<String> readLogsFromFile(String fileName) {
        Resource file = new ClassPathResource(fileName);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.getFile()));

            return reader.lines().toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getDirectoriesWithSizesSmallerThan(Map<String, Directory> directoryStructure, Integer maxSize) {
        return directoryStructure.entrySet().stream()
                .filter(directoryEntry -> directoryEntry.getValue().getSize() < maxSize)
                .map(Map.Entry::getKey)
                .toList();
    }

    public Integer getSizeOfDirectories(Map<String, Directory> directories, List<String> directoryPaths) {
        Integer sum = 0;

        for (String smallDirectoryPath : directoryPaths) {
            Directory directory = directories.get(smallDirectoryPath);
            sum += directory.getSize();
        }

        return sum;
    }

    public Integer getSpaceToFree(Integer currentlyUsedSpace) {
        Integer currentlyUnusedSpace = MAX_DEVICE_SPACE - currentlyUsedSpace;
        return NEEDED_UNUSED_SPACE - currentlyUnusedSpace;
    }

    public List<String> getDirectoriesWithSizesBiggerThan(Map<String, Directory> directoryStructure, Integer sizeToCompareTo) {
        return directoryStructure.entrySet().stream()
                .filter(directoryEntry -> directoryEntry.getValue().getSize() > sizeToCompareTo)
                .map(Map.Entry::getKey)
                .toList();
    }

    public Integer getSmallestSizeBetween(Map<String, Directory> directoryStructure, List<String> bigDirectories) {
        Integer smallestSize = Integer.MAX_VALUE;

        for (String bigDirectory : bigDirectories) {
            Integer size = directoryStructure.get(bigDirectory).getSize();
            if (size < smallestSize) {
                smallestSize = size;
            }
        }

        return smallestSize;
    }

    private void treatLSModeForLogLine(String logLine, String currentDirectoryPath, Map<String, Directory> directories) {
        if (logLine.startsWith("dir")) {
            createNewDirectory(logLine, currentDirectoryPath, directories);
        } else {
            createNewFile(logLine, currentDirectoryPath, directories);
        }
    }

    private void createNewDirectory(String logLine, String parentDirectoryPath, Map<String, Directory> directories) {
        // It is a directory
        String[] splitLogLine = logLine.split(" ");
        String directoryToCreate = splitLogLine[1] + "/";

        // Create new directory in current one and create subdirectories for it
        Directory parentDirectory = directories.get(parentDirectoryPath);

        List<String> containedDirectories = parentDirectory.getContainedDirectories();

        if (isNull(containedDirectories)) {
            containedDirectories = new ArrayList<>();
        }

        containedDirectories.add(parentDirectoryPath + directoryToCreate);
        parentDirectory.setContainedDirectories(containedDirectories);

        String newDirectoryPath;
        // Doing this in order to not have double '//' in case of default directory
        if (parentDirectoryPath.equals("/")) {
            newDirectoryPath = "/" + directoryToCreate;
        } else {
            newDirectoryPath = parentDirectoryPath + directoryToCreate;
        }

        directories.put(newDirectoryPath, Directory.builder().parentDirectoryPath(parentDirectoryPath).build());
    }

    private void createNewFile(String logLine, String parentDirectoryPath, Map<String, Directory> directories) {
        // It is a file
        String[] splitLogLine = logLine.split(" ");
        Integer fileSize = Integer.parseInt(splitLogLine[0]);
        String fileName = splitLogLine[1];

        // Create new file in current directory
        Directory parentDirectory = directories.get(parentDirectoryPath);

        if (isNull(parentDirectory)) {
            throw new RuntimeException("Parent directory not found: " + parentDirectoryPath);
        }

        List<File> containedFiles = parentDirectory.getContainedFiles();

        if (isNull(containedFiles)) {
            containedFiles = new ArrayList<>();
        }

        containedFiles.add(File.builder().name(fileName).size(fileSize).build());
        parentDirectory.setContainedFiles(containedFiles);

        // Update parent directory size
        updateParentDirectorySize(parentDirectoryPath, directories, fileSize);
    }

    private void updateParentDirectorySize(String parentDirectoryPath, Map<String, Directory> directories, Integer fileSize) {
        Directory parentDirectory = directories.get(parentDirectoryPath);

        if (!isNull(parentDirectory)) {
            Integer parentDirectorySize = !isNull(parentDirectory.getSize()) ? parentDirectory.getSize() : 0;
            parentDirectory.setSize(parentDirectorySize + fileSize);

            updateParentDirectorySize(parentDirectory.getParentDirectoryPath(), directories, fileSize);
        }
    }
}
