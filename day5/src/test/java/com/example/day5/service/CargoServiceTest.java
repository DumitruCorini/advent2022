package com.example.day5.service;

import com.example.day5.model.CargoState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CargoServiceTest {

    @Autowired
    CargoService cargoService;

    @Test
    void should_create_cargo_state_with_1_cargo_stack_with_A_character_from_cargo_state_text_block() {
        // GIVEN
        String cargoStateString = """
                [A]
                 1
                """;

        // WHEN
        List<Stack<Character>> cargoState = cargoService.createCargoStartingStateFromTextBlock(cargoStateString);

        // THEN
        assertEquals(1, cargoState.size());
        assertEquals('A', cargoState.getFirst().peek());
    }

    @Test
    void should_create_cargo_state_with_two_stacks_first_containing_A_char_while_second_containing_B() {
        // GIVEN
        String cargoStateString = """
                [A] [B]
                 1   2
                """;

        // WHEN
        List<Stack<Character>> cargoState = cargoService.createCargoStartingStateFromTextBlock(cargoStateString);

        // THEN
        assertEquals(2, cargoState.size());
        assertEquals('A', cargoState.get(0).peek());
        assertEquals('B', cargoState.get(1).peek());
    }

    @Test
    void should_create_cargo_state_with_three_stacks_with_all_empty_stacks() {
        // GIVEN
        String cargoStateString = """
                           \s
                 1   2   3
                """;

        // WHEN
        List<Stack<Character>> cargoState = cargoService.createCargoStartingStateFromTextBlock(cargoStateString);

        // THEN
        assertEquals(3, cargoState.size());
        assertEquals(new ArrayList<>(), cargoState.get(0));
        assertEquals(new ArrayList<>(), cargoState.get(1));
        assertEquals(new ArrayList<>(), cargoState.get(2));
    }

    @Test
    void should_create_cargo_state_with_three_stacks_with_middle_empty_stack() {
        // GIVEN
        String cargoStateString = """
                [A]     [C]
                 1   2   3
                """;

        // WHEN
        List<Stack<Character>> cargoState = cargoService.createCargoStartingStateFromTextBlock(cargoStateString);

        // THEN
        assertEquals(3, cargoState.size());
        assertEquals('A', cargoState.get(0).peek());
        assertEquals(new ArrayList<>(), cargoState.get(1));
        assertEquals('C', cargoState.get(2).peek());
    }

    @Test
    void should_create_cargo_state_with_three_stacks_with_last_empty_stack() {
        // GIVEN
        String cargoStateString = """
                [A]        \s
                 1   2   3
                """;

        // WHEN
        List<Stack<Character>> cargoState = cargoService.createCargoStartingStateFromTextBlock(cargoStateString);

        // THEN
        assertEquals(3, cargoState.size());
        assertEquals('A', cargoState.get(0).peek());
        assertEquals(new ArrayList<>(), cargoState.get(1));
        assertEquals(new ArrayList<>(), cargoState.get(2));
    }

    @Test
    void should_create_cargo_state_with_two_characters_A_C_for_single_stack() {
        // GIVEN
        String cargoStateString = """
                [A]\s
                [C]
                 1
                """;

        // WHEN
        List<Stack<Character>> cargoState = cargoService.createCargoStartingStateFromTextBlock(cargoStateString);

        // THEN
        assertEquals(1, cargoState.size());
        assertEquals("[C, A]", cargoState.getFirst().toString());
    }

    @Test
    void should_create_cargo_state_with_three_stacks_with_multiple_characters() {
        // GIVEN
        String cargoStateString = """
                    [D]   \s
                [N] [C]   \s
                [Z] [M] [P]
                 1   2   3
                """;

        // WHEN
        List<Stack<Character>> cargoState = cargoService.createCargoStartingStateFromTextBlock(cargoStateString);

        // THEN
        assertEquals(3, cargoState.size());
        assertEquals("[Z, N]", cargoState.get(0).toString());
        assertEquals("[M, C, D]", cargoState.get(1).toString());
        assertEquals("[P]", cargoState.get(2).toString());
    }

    @Test
    void should_read_txt_file_inputTestOnlyCargoState_correctly_and_create_cargo_state_with_three_stacks() {
        // GIVEN
        String inputFileName = "inputTestOnlyCargoState.txt";

        // WHEN
        CargoState cargoState = cargoService.createCargoStateFromFile(inputFileName);
        List<Stack<Character>> startingState = cargoState.getCurrentStackState();

        // THEN
        assertEquals(3, startingState.size());
        assertEquals("[Z, N]", startingState.get(0).toString());
        assertEquals("[M, C, D]", startingState.get(1).toString());
        assertEquals("[P]", startingState.get(2).toString());
    }

    @Test
    void should_read_inputTestWithSingleInstruction_detect_movement_instruction_move_1_from_1_to_2_in_file_and_create_instructions_list_containing_it() {
        // GIVEN
        String inputFileName = "inputTestWithSingleInstruction.txt";

        // WHEN
        String expectedFirstInstruction = "move 1 from 1 to 2";
        CargoState actualCargoState = cargoService.createCargoStateFromFile(inputFileName);
        List<String> actionInstructions = actualCargoState.getInstructionsToApply();

        // THEN
        assertEquals(1, actionInstructions.size());
        assertEquals(expectedFirstInstruction, actionInstructions.getFirst());
    }

    @Test
    void should_read_inputTestComplete_file_and_create_cargo_starting_state_with_correct_number_of_stacks_and_instructions() {
        // GIVEN
        String inputFileName = "inputTestComplete.txt";

        // WHEN
        CargoState cargoStartingState = cargoService.createCargoStateFromFile(inputFileName);

        // THEN
        assertEquals(3, cargoStartingState.getCurrentStackState().size());
        assertEquals(4, cargoStartingState.getInstructionsToApply().size());
    }

    @Test
    void should_execute_instructions_for_starting_cargo_starting_state_from_file_inputTestWithSingleInstruction_and_achieve_cargo_state() {
        // GIVEN
        String fileName = "inputTestWithSingleInstruction.txt";
        CargoState initialCargoState = cargoService.createCargoStateFromFile(fileName);

        // WHEN
        List<Stack<Character>> newStackState = new ArrayList<>();
        newStackState.add(createStackWithCharacters('Z'));
        newStackState.add(createStackWithCharacters('M', 'C', 'D', 'N'));
        newStackState.add(createStackWithCharacters('P'));
        CargoState expectedNewCargoState = CargoState.builder()
                .currentStackState(newStackState)
                .instructionsToApply(new ArrayList<>())
                .build();

        CargoState actualNewCargoState = cargoService.applyCraneMover9000InstructionsAndGetNewCargoState(initialCargoState);

        // THEN
        assertEquals(expectedNewCargoState, actualNewCargoState);
    }

    @Test
    void should_read_inputTestComplete_file_and_execute_all_instructions_for_crane_9000() {
        // GIVEN
        String fileName = "inputTestComplete.txt";
        CargoState initialCargoState = cargoService.createCargoStateFromFile(fileName);

        // WHEN
        List<Stack<Character>> newStackState = new ArrayList<>();
        newStackState.add(createStackWithCharacters('C'));
        newStackState.add(createStackWithCharacters('M'));
        newStackState.add(createStackWithCharacters('P', 'D', 'N', 'Z'));
        CargoState expectedNewCargoState = CargoState.builder()
                .currentStackState(newStackState)
                .instructionsToApply(new ArrayList<>())
                .build();

        CargoState actualNewCargoState = cargoService.applyCraneMover9000InstructionsAndGetNewCargoState(initialCargoState);

        // THEN
        assertEquals(expectedNewCargoState, actualNewCargoState);
    }

    @Test
    void should_read_inputTestComplete_file_and_execute_all_instructions_for_crane_9001() {
        // GIVEN
        String fileName = "inputTestComplete.txt";
        CargoState initialCargoState = cargoService.createCargoStateFromFile(fileName);

        // WHEN
        List<Stack<Character>> newStackState = new ArrayList<>();
        newStackState.add(createStackWithCharacters('M'));
        newStackState.add(createStackWithCharacters('C'));
        newStackState.add(createStackWithCharacters('P', 'Z', 'N', 'D'));
        CargoState expectedNewCargoState = CargoState.builder()
                .currentStackState(newStackState)
                .instructionsToApply(new ArrayList<>())
                .build();

        CargoState actualNewCargoState = cargoService.applyCraneMover9001InstructionsAndGetNewCargoState(initialCargoState);

        // THEN
        assertEquals(expectedNewCargoState, actualNewCargoState);
    }

    @Test
    void should_get_message_CMZ_from_the_letters_from_top_of_three_stacks() {
        String cargoStateString = """
                [C] [M] [Z]
                 1   2   3
                """;
        List<Stack<Character>> cargoState = cargoService.createCargoStartingStateFromTextBlock(cargoStateString);

        // WHEN
        String expectedMessage = "CMZ";
        String actualMessage = cargoService.getMessageFromStacks(cargoState);

        // THEN
        assertEquals(expectedMessage, actualMessage);
    }

    private Stack<Character> createStackWithCharacters(Character... charactersToAdd) {
        Stack<Character> stack = new Stack<>();

        for (Character character : charactersToAdd) {
            stack.push(character);
        }

        return stack;
    }
}
