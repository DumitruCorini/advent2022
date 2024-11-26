package com.example.day7.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Directory {

    private Integer size;
    private String parentDirectoryPath;
    private List<File> containedFiles;
    private List<String> containedDirectories;

}
