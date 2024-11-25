package com.example.day5.component;

import com.example.day5.model.CargoState;
import com.example.day5.service.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    CargoService cargoService;

    @Override
    public void run(String... args) {
        CargoState cargoState = cargoService.createCargoStateFromFile("input.txt");

        CargoState new9000CargoState = cargoService.applyCraneMover9000InstructionsAndGetNewCargoState(cargoState);

        String cargoMessage9000 = cargoService.getMessageFromStacks(new9000CargoState.getCurrentStackState());

        System.out.println("Cargo message 9000: " + cargoMessage9000);

        CargoState cargoStateFor90001 = cargoService.createCargoStateFromFile("input.txt");

        CargoState new9001CargoState = cargoService.applyCraneMover9001InstructionsAndGetNewCargoState(cargoStateFor90001);

        String cargoMessage9001 = cargoService.getMessageFromStacks(new9001CargoState.getCurrentStackState());

        System.out.println("Cargo message 9001: " + cargoMessage9001);
        
    }
}
