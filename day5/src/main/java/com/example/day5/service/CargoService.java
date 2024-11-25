package com.example.day5.service;

import com.example.day5.model.CargoState;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class CargoService {

    public List<Stack<Character>> createCargoStartingStateFromTextBlock(String cargoStateString) {
        return createCargoStartingStateAndInstructions(cargoStateString.lines().toList()).getCurrentStackState();
    }

    public CargoState createCargoStateFromFile(String fileName) {
        Resource file = new ClassPathResource(fileName);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.getFile()));

            return createCargoStartingStateAndInstructions(reader.lines().toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CargoState createCargoStartingStateAndInstructions(List<String> cargoStateLines) {
        // create number of stacks depending on the number of stacks of crates
        List<Stack<Character>> cargoStartingState = createEmptyStacksFromFirstLine(cargoStateLines.getFirst());
        List<String> instructionsLines = new ArrayList<>();

        for (int lineIdx = 0; lineIdx < cargoStateLines.size(); lineIdx++) {
            String line = cargoStateLines.get(lineIdx);

            // If we have an empty line, it is the start of instructions in the file
            if (line.isEmpty()) {
                instructionsLines = cargoStateLines.subList(lineIdx + 1, cargoStateLines.size());
                break;
            }

            char[] charactersInLine = line.toCharArray();
            for (int characterIndex = 0; characterIndex < charactersInLine.length; characterIndex++) {
                if (characterIndex % 4 == 0 && charactersInLine[characterIndex] == '[') {
                    int stackIndex = characterIndex / 4;
                    // Since we have [A], if we have '[', add character that follows 'A' to cargoStartingState
                    cargoStartingState.get(stackIndex).addFirst(charactersInLine[characterIndex + 1]);
                }
            }
        }

        return CargoState.builder()
                .currentStackState(cargoStartingState)
                .instructionsToApply(instructionsLines)
                .build();
    }

    private List<Stack<Character>> createEmptyStacksFromFirstLine(String cargoStateFirstLine) {
        List<Stack<Character>> stacks = new ArrayList<>();
        int firstLineLength = cargoStateFirstLine.length() + 1;

        for (int stackIndex = 0; stackIndex < firstLineLength / 4; stackIndex++) {
            stacks.add(new Stack<>());
        }

        return stacks;
    }

    public CargoState applyCraneMover9000InstructionsAndGetNewCargoState(CargoState cargoState) {
        CargoState newCargoState = CargoState.builder()
                .currentStackState(cargoState.getCurrentStackState())
                .instructionsToApply(new ArrayList<>())
                .build();

        for (String instructionToApply : cargoState.getInstructionsToApply()) {
            String[] splitInstruction = instructionToApply.split(" ");
            int moveCount = Integer.parseInt(splitInstruction[1]);
            int fromStack = Integer.parseInt(splitInstruction[3]);
            int toStack = Integer.parseInt(splitInstruction[5]);

            for (int moveIndex = 0; moveIndex < moveCount; moveIndex++) {
                Character elementRemoved = removeElement(newCargoState.getCurrentStackState(), fromStack);
                addElement(newCargoState.getCurrentStackState(), toStack, elementRemoved);
            }
        }

        return newCargoState;
    }

    public CargoState applyCraneMover9001InstructionsAndGetNewCargoState(CargoState cargoState) {
        CargoState newCargoState = CargoState.builder()
                .currentStackState(cargoState.getCurrentStackState())
                .instructionsToApply(new ArrayList<>())
                .build();

        for (String instructionToApply : cargoState.getInstructionsToApply()) {
            String[] splitInstruction = instructionToApply.split(" ");
            int moveCount = Integer.parseInt(splitInstruction[1]);
            int fromStack = Integer.parseInt(splitInstruction[3]);
            int toStack = Integer.parseInt(splitInstruction[5]);

            Stack<Character> elementsToMove = new Stack<>();

            for (int moveIndex = 0; moveIndex < moveCount; moveIndex++) {
                Character elementRemoved = removeElement(newCargoState.getCurrentStackState(), fromStack);
                elementsToMove.push(elementRemoved);
            }

            while (!isEmpty(elementsToMove)) {
                addElement(newCargoState.getCurrentStackState(), toStack, elementsToMove.pop());
            }
        }

        return newCargoState;
    }

    public String getMessageFromStacks(List<Stack<Character>> cargoState) {
        StringBuilder message = new StringBuilder();

        for (Stack<Character> stack : cargoState) {
            message.append(stack.peek());
        }

        return message.toString();
    }

    private Character removeElement(List<Stack<Character>> stackState, int fromStack) {
        return stackState.get(fromStack - 1).removeLast();
    }

    private void addElement(List<Stack<Character>> stackState, int toStack, Character elementToAdd) {
        stackState.get(toStack - 1).push(elementToAdd);
    }
}
