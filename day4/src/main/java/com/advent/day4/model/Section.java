package com.advent.day4.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Section {
    private Integer start;
    private Integer end;
}
