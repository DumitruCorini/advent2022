package com.example.day7.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class File {

    private String name;
    private Integer size;
}
