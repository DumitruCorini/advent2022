package com.example.day7.service;

import com.example.day7.model.Directory;
import com.example.day7.model.File;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DeviceServiceTest {

    @Autowired
    DeviceService deviceService;

    @Test
    void should_create_default_directory_when_changing_directory_into_default_one() {
        // GIVEN
        String logLine = "$ cd /";
        Map<String, Directory> expectedDirectoryStructure = new HashMap<>();
        expectedDirectoryStructure.put("/", Directory.builder().build());

        // WHEN
        Map<String, Directory> actualDirectoryStructure = deviceService.createDirectoryPathFromLog(List.of(logLine));

        // THEN
        assertEquals(expectedDirectoryStructure, actualDirectoryStructure);
    }

    @Test
    void should_create_directory_structure_with_two_directories_a_and_b_from_ls_command_logs() {
        // GIVEN
        List<String> logLines = createLogLines("$ cd /", "$ ls", "dir a", "dir b");
        Map<String, Directory> expectedDirectoryStructure = new HashMap<>();
        expectedDirectoryStructure.put("/", Directory.builder().containedDirectories(new ArrayList<>(Arrays.asList("/a/", "/b/"))).build());
        expectedDirectoryStructure.put("/a/", Directory.builder().parentDirectoryPath("/").build());
        expectedDirectoryStructure.put("/b/", Directory.builder().parentDirectoryPath("/").build());

        // WHEN
        Map<String, Directory> actualDirectoryStructure = deviceService.createDirectoryPathFromLog(logLines);

        // THEN
        assertEquals(expectedDirectoryStructure, actualDirectoryStructure);
    }

    @Test
    void should_create_text_file_toto_inside_default_directory() {
        // GIVEN
        List<String> logLines = createLogLines("$ cd /", "$ ls", "123 toto.txt");
        Map<String, Directory> expectedDirectoryStructure = new HashMap<>();
        expectedDirectoryStructure.put("/", Directory.builder()
                .size(123)
                .containedFiles(new ArrayList<>(Collections.singletonList(File.builder().name("toto.txt").size(123).build())))
                .build());

        // WHEN
        Map<String, Directory> actualDirectoryStructure = deviceService.createDirectoryPathFromLog(logLines);

        // THEN
        assertEquals(expectedDirectoryStructure, actualDirectoryStructure);
    }

    @Test
    void should_create_directory_structure_with_directory_a_and_subdirectory_b_when_logs_have_cd_a_and_ls_inside_a() {
        // GIVEN
        List<String> logLines = createLogLines("$ cd /", "$ ls", "dir a", "$ cd a", "$ ls", "dir b");
        Map<String, Directory> expectedDirectoryStructure = new HashMap<>();
        expectedDirectoryStructure.put("/", Directory.builder()
                .containedDirectories(new ArrayList<>(Collections.singletonList("/a/")))
                .build());
        expectedDirectoryStructure.put("/a/", Directory.builder()
                .parentDirectoryPath("/")
                .containedDirectories(new ArrayList<>(Collections.singletonList("/a/b/")))
                .build());
        expectedDirectoryStructure.put("/a/b/", Directory.builder()
                .parentDirectoryPath("/a/")
                .build());

        // WHEN
        Map<String, Directory> actualDirectoryStructure = deviceService.createDirectoryPathFromLog(logLines);

        // THEN
        assertEquals(expectedDirectoryStructure, actualDirectoryStructure);
    }

    @Test
    void should_create_directory_a_with_subdirectory_b_with_subdirectory_c() {
        // GIVEN
        List<String> logLines = createLogLines("$ cd /", "$ ls", "dir a", "$ cd a", "$ ls", "dir b", "$ cd b", "$ ls", "dir c");
        Map<String, Directory> expectedDirectoryStructure = new HashMap<>();
        expectedDirectoryStructure.put("/", Directory.builder()
                .containedDirectories(new ArrayList<>(Collections.singletonList("/a/")))
                .build());
        expectedDirectoryStructure.put("/a/", Directory.builder()
                .parentDirectoryPath("/")
                .containedDirectories(new ArrayList<>(Collections.singletonList("/a/b/")))
                .build());
        expectedDirectoryStructure.put("/a/b/", Directory.builder()
                .parentDirectoryPath("/a/")
                .containedDirectories(new ArrayList<>(Collections.singletonList("/a/b/c/")))
                .build());
        expectedDirectoryStructure.put("/a/b/c/", Directory.builder()
                .parentDirectoryPath("/a/b/")
                .build());

        // WHEN
        Map<String, Directory> actualDirectoryStructure = deviceService.createDirectoryPathFromLog(logLines);

        // THEN
        assertEquals(expectedDirectoryStructure, actualDirectoryStructure);
    }

    @Test
    void should_create_directory_a_with_subdirectory_b_and_directory_c_with_subdirectory_d() {
        // GIVEN
        List<String> logLines = createLogLines("$ cd /", "$ ls", "dir a", "dir c", "$ cd a", "$ ls", "dir b", "$ cd ..", "$ cd c", "$ ls", "dir d");
        Map<String, Directory> expectedDirectoryStructure = new HashMap<>();
        expectedDirectoryStructure.put("/", Directory.builder()
                .containedDirectories(new ArrayList<>(Arrays.asList("/a/", "/c/")))
                .build());
        expectedDirectoryStructure.put("/a/", Directory.builder()
                .parentDirectoryPath("/")
                .containedDirectories(new ArrayList<>(Collections.singletonList("/a/b/")))
                .build());
        expectedDirectoryStructure.put("/a/b/", Directory.builder()
                .parentDirectoryPath("/a/")
                .build());
        expectedDirectoryStructure.put("/c/", Directory.builder()
                .parentDirectoryPath("/")
                .containedDirectories(new ArrayList<>(Collections.singletonList("/c/d/")))
                .build());
        expectedDirectoryStructure.put("/c/d/", Directory.builder()
                .parentDirectoryPath("/c/")
                .build());

        // WHEN
        Map<String, Directory> actualDirectoryStructure = deviceService.createDirectoryPathFromLog(logLines);

        // THEN
        assertEquals(expectedDirectoryStructure, actualDirectoryStructure);
    }

    @Test
    void should_create_directory_a_with_text_file_toto_and_init_size_123_for_both() {
        // GIVEN
        List<String> logLines = createLogLines("$ cd /", "$ ls", "dir a", "$ cd a", "$ ls", "123 toto.txt");
        Map<String, Directory> expectedDirectoryStructure = new HashMap<>();
        expectedDirectoryStructure.put("/", Directory.builder()
                .size(123)
                .containedDirectories(new ArrayList<>(Collections.singletonList("/a/")))
                .build());
        expectedDirectoryStructure.put("/a/", Directory.builder()
                .parentDirectoryPath("/")
                .size(123)
                .containedFiles(Collections.singletonList(File.builder().name("toto.txt").size(123).build()))
                .build());

        // WHEN
        Map<String, Directory> actualDirectoryStructure = deviceService.createDirectoryPathFromLog(logLines);

        // THEN
        assertEquals(expectedDirectoryStructure, actualDirectoryStructure);
    }

    @Test
    void should_create_directory_a_subdirectory_b_with_text_file_inside_fileAB_with_size_123_and_directory_c_subdirectory_d_with_text_file_inside_fileCD_with_file_456() {
        // GIVEN
        List<String> logLines = createLogLines("$ cd /", "$ ls", "dir a", "dir c",
                "$ cd a", "$ ls", "dir b", "$ cd b", "$ ls", "123 fileAB.txt",
                "$ cd ..", "$ cd ..",
                "$ cd c", "$ ls", "dir d", "$ cd d", "$ ls", "456 fileCD.txt");
        Map<String, Directory> expectedDirectoryStructure = new HashMap<>();
        expectedDirectoryStructure.put("/", Directory.builder()
                .size(579)
                .containedDirectories(new ArrayList<>(Arrays.asList("/a/", "/c/")))
                .build());
        expectedDirectoryStructure.put("/a/", Directory.builder()
                .size(123)
                .parentDirectoryPath("/")
                .containedDirectories(new ArrayList<>(Collections.singletonList("/a/b/")))
                .build());
        expectedDirectoryStructure.put("/a/b/", Directory.builder()
                .size(123)
                .containedFiles(new ArrayList<>(Collections.singletonList(File.builder().name("fileAB.txt").size(123).build())))
                .parentDirectoryPath("/a/")
                .build());
        expectedDirectoryStructure.put("/c/", Directory.builder()
                .parentDirectoryPath("/")
                .size(456)
                .containedDirectories(new ArrayList<>(Collections.singletonList("/c/d/")))
                .build());
        expectedDirectoryStructure.put("/c/d/", Directory.builder()
                .size(456)
                .containedFiles(new ArrayList<>(Collections.singletonList(File.builder().name("fileCD.txt").size(456).build())))
                .parentDirectoryPath("/c/")
                .build());

        // WHEN
        Map<String, Directory> actualDirectoryStructure = deviceService.createDirectoryPathFromLog(logLines);

        // THEN
        assertEquals(expectedDirectoryStructure, actualDirectoryStructure);
    }

    @Test
    void should_read_logs_from_inputTest_text_file_and_create_directory_structure() {
        // GIVEN
        String fileName = "inputTest.txt";
        Map<String, Directory> expectedDirectoryStructure = new HashMap<>();
        expectedDirectoryStructure.put("/", Directory.builder()
                .size(48381165)
                .containedDirectories(new ArrayList<>(Arrays.asList("/a/", "/d/")))
                .containedFiles(new ArrayList<>(Arrays.asList(
                        File.builder().name("b.txt").size(14848514).build(),
                        File.builder().name("c.dat").size(8504156).build()
                )))
                .build());
        expectedDirectoryStructure.put("/a/", Directory.builder()
                .size(94853)
                .parentDirectoryPath("/")
                .containedDirectories(new ArrayList<>(Collections.singletonList("/a/e/")))
                .containedFiles(new ArrayList<>(Arrays.asList(
                        File.builder().name("f").size(29116).build(),
                        File.builder().name("g").size(2557).build(),
                        File.builder().name("h.lst").size(62596).build()
                )))
                .build());
        expectedDirectoryStructure.put("/a/e/", Directory.builder()
                .size(584)
                .parentDirectoryPath("/a/")
                .containedFiles(new ArrayList<>(Collections.singletonList(File.builder().name("i").size(584).build())))
                .build());
        expectedDirectoryStructure.put("/d/", Directory.builder()
                .size(24933642)
                .parentDirectoryPath("/")
                .containedFiles(new ArrayList<>(Arrays.asList(
                        File.builder().name("j").size(4060174).build(),
                        File.builder().name("d.log").size(8033020).build(),
                        File.builder().name("d.ext").size(5626152).build(),
                        File.builder().name("k").size(7214296).build()
                )))
                .build());

        // WHEN
        List<String> logs = deviceService.readLogsFromFile(fileName);
        Map<String, Directory> actualDirectoryStructure = deviceService.createDirectoryPathFromLog(logs);

        // THEN
        assertEquals(expectedDirectoryStructure, actualDirectoryStructure);
    }

    @Test
    void should_get_directory_a_as_having_less_than_100000_size_between_two_directories_a_and_b() {
        // GIVEN
        List<String> logs = createLogLines("$cd /", "$ ls", "dir a", "dir b",
                "$ cd a", "$ ls", "1000 testA.txt",
                "$ cd ..",
                "$ cd b", "$ ls", "200000 testB.txt");
        Map<String, Directory> directoryStructure = deviceService.createDirectoryPathFromLog(logs);
        List<String> expectedSmallerDirectories = new ArrayList<>(Collections.singletonList("/a/"));

        // WHEN
        List<String> actualSmallerDirectories = deviceService.getDirectoriesWithSizesSmallerThan(directoryStructure, 100000);

        // THEN
        assertEquals(expectedSmallerDirectories, actualSmallerDirectories);
    }

    @Test
    void should_get_directories_with_size_smaller_than_100000_from_text_file_inputTest_and_get_the_total_size_of_those_directories_95437() {
        // GIVEN
        String fileName = "inputTest.txt";
        List<String> logs = deviceService.readLogsFromFile(fileName);
        Map<String, Directory> directoryStructure = deviceService.createDirectoryPathFromLog(logs);
        List<String> expectedSmallDirectories = new ArrayList<>(Arrays.asList("/a/e/", "/a/"));
        Integer expectedSmallDirectorySizesSum = 95437;

        // WHEN
        List<String> actualSmallDirectories = deviceService.getDirectoriesWithSizesSmallerThan(directoryStructure, 100000);
        Integer actualSmallDirectoriesSizesSum = deviceService.getSizeOfDirectories(directoryStructure, actualSmallDirectories);

        // THEN
        assertEquals(expectedSmallDirectories, actualSmallDirectories);
        assertEquals(expectedSmallDirectorySizesSum, actualSmallDirectoriesSizesSum);
    }

    @Test
    void should_get_size_of_directory_to_delete_100000_from_size_of_main_directory() {
        // GIVEN
        Integer currentlyUsedSpace = 48381165;
        Integer expectedSpaceToFree = 8381165;

        // WHEN
        Integer actualSpaceToFree = deviceService.getSpaceToFree(currentlyUsedSpace);

        // THEN
        assertEquals(expectedSpaceToFree, actualSpaceToFree);
    }

    @Test
    void should_get_directories_from_file_inputTest_that_are_bigger_than_8381165() {
        // GIVEN
        String fileName = "inputTest.txt";
        List<String> logs = deviceService.readLogsFromFile(fileName);
        Map<String, Directory> directoryStructure = deviceService.createDirectoryPathFromLog(logs);
        Integer sizeToCompareTo = 8381165;
        List<String> expectedBigDirectories = new ArrayList<>(Arrays.asList("/d/", "/"));

        // WHEN
        List<String> actualBigDirectories = deviceService.getDirectoriesWithSizesBiggerThan(directoryStructure, sizeToCompareTo);

        // THEN
        assertEquals(expectedBigDirectories, actualBigDirectories);
    }

    @Test
    void should_get_smallest_directory_between_directories_with_size_bigger_than_8381165_in_text_file_inputTest() {
        // GIVEN
        String fileName = "inputTest.txt";
        List<String> logs = deviceService.readLogsFromFile(fileName);
        Map<String, Directory> directoryStructure = deviceService.createDirectoryPathFromLog(logs);
        Integer sizeToCompareTo = 8381165;
        List<String> bigDirectories = deviceService.getDirectoriesWithSizesBiggerThan(directoryStructure, sizeToCompareTo);
        Integer expectedSizeOfSmallestDirectory = 24933642;

        // WHEN
        Integer actualSizeOfSmallestDirectory = deviceService.getSmallestSizeBetween(directoryStructure, bigDirectories);

        // THEN
        assertEquals(expectedSizeOfSmallestDirectory, actualSizeOfSmallestDirectory);
    }

    private List<String> createLogLines(String... logLine) {
        return List.of(logLine);
    }
}
