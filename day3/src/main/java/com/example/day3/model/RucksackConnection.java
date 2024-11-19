package com.example.day3.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RucksackConnection {
    private Character connectedItem;
    private List<String> rucksacks;

    @Override
    public String toString() {
        return "RucksackConnection{" +
                "connectedItem=" + connectedItem +
                ", rucksacks=" + rucksacks +
                '}';
    }
}
