package com.example.day6.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CommunicationServiceTest {

    @Autowired
    CommunicationService communicationService;

    @ParameterizedTest
    @MethodSource(value = "getDatastreamsWithExpectedPositionsForStartOfPacket")
    void should_get_marker_position_for_start_of_packet_in_datastream_buffer(String datastreamBuffer, int expectedMarkerPosition) {
        // GIVEN

        // WHEN
        int actualMarkerPosition = communicationService.getMarkerPositionFromDatastreamBuffer(datastreamBuffer, 4);

        // THEN
        assertEquals(expectedMarkerPosition, actualMarkerPosition);
    }

    @ParameterizedTest
    @MethodSource(value = "getDatastreamsWithExpectedPositionsForStartOfMessage")
    void should_get_marker_position_for_start_of_message_in_datastream_buffer(String datastreamBuffer, int expectedMarkerPosition) {
        // GIVEN

        // WHEN
        int actualMarkerPosition = communicationService.getMarkerPositionFromDatastreamBuffer(datastreamBuffer, 14);

        // THEN
        assertEquals(expectedMarkerPosition, actualMarkerPosition);
    }

    private static Stream<Arguments> getDatastreamsWithExpectedPositionsForStartOfPacket() {
        return Stream.of(
                Arguments.of("abcd", 4),
                Arguments.of("abcad", 5),
                Arguments.of("bvwbjplbgvbhsrlpgdmjqwftvncz", 5),
                Arguments.of("nppdvjthqldpwncqszvftbrmjlhg", 6),
                Arguments.of("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 10),
                Arguments.of("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 11),
                Arguments.of("qzbzwwgha", 9)
        );
    }

    private static Stream<Arguments> getDatastreamsWithExpectedPositionsForStartOfMessage() {
        return Stream.of(
                Arguments.of("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 19),
                Arguments.of("bvwbjplbgvbhsrlpgdmjqwftvncz", 23),
                Arguments.of("nppdvjthqldpwncqszvftbrmjlhg", 23),
                Arguments.of("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 29),
                Arguments.of("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 26)
        );
    }
}