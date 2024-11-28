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
                List<Integer> treesToLeft = getTreesToLeft(forestLine, treeIdx);
                List<Integer> treesToRight = getTreesToRight(forestLine, treeIdx);
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

    public Integer getHighestTreeVisibilityScore(List<List<Integer>> forest) {
        int highestTreeVisibilityScore = 0;

        // Start with 1, and go until length - 1 because the trees at the borders of the forest always have visibility 0, 
        // because they have at least one side that has no trees 
        for (int forestLineIdx = 1; forestLineIdx < forest.size() - 1; forestLineIdx++) {
            List<Integer> forestLine = forest.get(forestLineIdx);
            for (int treeIdx = 1; treeIdx < forestLine.size() - 1; treeIdx++) {
                Integer treeSize = forestLine.get(treeIdx);

                // To get the current tree visibility score, we have to get the number of trees that are smaller or equal in size:
                //  for the trees to the left, starting from the last tree
                //  for the trees to the right, starting from the beginning
                //  for the trees above, starting from the end
                //  for the trees below, starting from the beginning
                // multiply these four numbers to get the tree visibility score

                List<Integer> treesToLeft = getTreesToLeft(forestLine, treeIdx);
                List<Integer> treesToRight = getTreesToRight(forestLine, treeIdx);
                List<Integer> treesAbove = createTreesAbove(forest, forestLineIdx, treeIdx);
                List<Integer> treesBelow = createTreesBelow(forest, forestLineIdx, treeIdx);

                Integer treeVisibility = 1;
                treeVisibility *= getTreeVisibilityScore(treesToLeft.reversed(), treeSize);
                treeVisibility *= getTreeVisibilityScore(treesToRight, treeSize);
                treeVisibility *= getTreeVisibilityScore(treesAbove.reversed(), treeSize);
                treeVisibility *= getTreeVisibilityScore(treesBelow, treeSize);

                if (treeVisibility > highestTreeVisibilityScore) {
                    highestTreeVisibilityScore = treeVisibility;
                }
            }
        }

        return highestTreeVisibilityScore;
    }

    private static Integer getTreeVisibilityScore(List<Integer> treesToCheck, Integer currentTreeSize) {
        Integer currentTreeVisibility = 0;
        for (Integer treeToCheck : treesToCheck) {
            currentTreeVisibility++;
            if (currentTreeSize <= treeToCheck) {
                break;
            }
        }
        return currentTreeVisibility;
    }

    private List<Integer> getTreesToLeft(List<Integer> forestLine, int treeIdx) {
        return forestLine.subList(0, treeIdx);
    }

    private List<Integer> getTreesToRight(List<Integer> forestLine, int treeIdx) {
        return forestLine.subList(treeIdx + 1, forestLine.size());
    }

    private List<Integer> createTreesAbove(List<List<Integer>> forest, int rowIndex, int treeIdx) {
        List<Integer> treesAbove = new ArrayList<>();

        for (int treeAboveRowIdx = 0; treeAboveRowIdx < rowIndex; treeAboveRowIdx++) {
            treesAbove.add(forest.get(treeAboveRowIdx).get(treeIdx));
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
