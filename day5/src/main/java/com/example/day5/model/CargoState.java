package com.example.day5.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Stack;

@Data
@Builder
public class CargoState {
    private List<Stack<Character>> currentStackState;
    private List<String> instructionsToApply;
}
