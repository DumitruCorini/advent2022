package com.example.day8.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ForestManagerService {

    public List<List<Integer>> createForestFromFile(String fileName) {
        List<List<Integer>> createdForest = new ArrayList<>();

        Resource file = new ClassPathResource(fileName);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.getFile()));

            reader.lines().forEach(line -> {
                List<Integer> createdForestLine = new ArrayList<>();
                for (char treeSize : line.toCharArray()) {
                    createdForestLine.add(Character.getNumericValue(treeSize));
                }
                createdForest.add(createdForestLine);
            });

            return createdForest;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<List<Integer>> createForestFromTextBlock(String forest) {
        List<List<Integer>> createdForest = new ArrayList<>();

        forest.lines().forEach(line -> {
            List<Integer> createdForestLine = new ArrayList<>();
            for (char treeSize : line.toCharArray()) {
                createdForestLine.add(Character.getNumericValue(treeSize));
            }
            createdForest.add(createdForestLine);
        });

        return createdForest;
    }

    public int getVisibleTreesCount(List<List<Integer>> forest) {
        int visibleTreeCount = forest.size() * forest.getFirst().size();

        // Start with 1, and go until length - 1 because the trees at the borders of the forest are always visible
        for (int forestLineIdx = 1; forestLineIdx < forest.size() - 1; forestLineIdx++) {
            List<Integer> forestLine = forest.get(forestLineIdx);
            for (int treeIdx = 1; treeIdx < forestLine.size() - 1; treeIdx++) {
                Integer treeSize = forestLine.get(treeIdx);

                // Check if the tree is hidden:
                // Tree is hidden if it has a tree that is bigger or the same size in all four directions
                List<Integer> treesToLeft = forestLine.subList(0, treeIdx);
                List<Integer> treesToRight = forestLine.subList(treeIdx + 1, forestLine.size());
                List<Integer> treesAbove = createTreesAbove(forest, forestLineIdx, treeIdx);
                List<Integer> treesBelow = createTreesBelow(forest, forestLineIdx, treeIdx);

                Optional<Integer> biggerTreeToLeftSize = treesToLeft.stream().filter(treeToLeftSize -> treeSize <= treeToLeftSize).findFirst();
                Optional<Integer> biggerTreeToRightSize = treesToRight.stream().filter(treeToRightSize -> treeSize <= treeToRightSize).findFirst();
                Optional<Integer> biggerTreeAbove = treesAbove.stream().filter(treeAboveSize -> treeSize <= treeAboveSize).findFirst();
                Optional<Integer> biggerTreeBelow = treesBelow.stream().filter(treesBelowSize -> treeSize <= treesBelowSize).findFirst();
                if (biggerTreeToLeftSize.isPresent() && biggerTreeToRightSize.isPresent() && biggerTreeAbove.isPresent() && biggerTreeBelow.isPresent()) {
                    visibleTreeCount--;
                }
            }
        }
        return visibleTreeCount;
    }

    private List<Integer> createTreesAbove(List<List<Integer>> forest, int rowIndex, int treeIdx) {
        List<Integer> treesAbove = new ArrayList<>();

        for (int treeAboveIdx = 0; treeAboveIdx < rowIndex; treeAboveIdx++) {
            treesAbove.add(forest.get(treeAboveIdx).get(treeIdx));
        }

        return treesAbove;
    }

    private List<Integer> createTreesBelow(List<List<Integer>> forest, int rowIdx, int treeIdx) {
        List<Integer> treesBelow = new ArrayList<>();

        for (int treeBelowIdx = rowIdx + 1; treeBelowIdx < forest.size(); treeBelowIdx++) {
            treesBelow.add(forest.get(treeBelowIdx).get(treeIdx));
        }

        return treesBelow;
    }
}
