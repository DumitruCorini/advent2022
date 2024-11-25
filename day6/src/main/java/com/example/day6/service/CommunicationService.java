package com.example.day6.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommunicationService {
    public int getMarkerPositionFromDatastreamBuffer(String datastreamBuffer) {
        List<Character> seenCharacters = new ArrayList<>();
        int markerIndex = 1;

        for (Character character : datastreamBuffer.toCharArray()) {
            int seenCharacterPreviousIndex = seenCharacters.indexOf(character);

            // We haven't seen the character in the last 4 characters
            if (seenCharacterPreviousIndex == -1) {
                seenCharacters.add(character);
            } else {
                seenCharacters = seenCharacters.subList(seenCharacterPreviousIndex + 1, seenCharacters.size());
                seenCharacters.add(character);
            }

            if (seenCharacters.size() == 4) {
                return markerIndex;
            }

            markerIndex++;
        }

        return 0;
    }
}
