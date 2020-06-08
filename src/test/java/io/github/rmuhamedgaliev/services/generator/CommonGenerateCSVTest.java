package io.github.rmuhamedgaliev.services.generator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommonGenerateCSVTest {
  private final Logger LOGGER = LoggerFactory.getLogger(CommonGenerateCSVTest.class);

  private GenerateService generateService;
  private File testFile;

  @BeforeEach
  void setUp() {
    long csvFileNumberPerLine = 10;
    String csvFilePath = "/tmp/ao1";
    int timeOut = 10;

    this.generateService = new CommonGenerateCSV(csvFileNumberPerLine, csvFilePath, timeOut);

    this.generateService.generateCSVFiles(1);
    this.testFile = new File(csvFilePath + "/0.csv");
  }

  @Test
  public void checkCountOfLinesInGeneratedFile() {



    try(
      BufferedReader bufferedReader = new BufferedReader(
        new FileReader(
          testFile,
          StandardCharsets.UTF_8
        )
      );
      ) {

      List<String> fileLines = new ArrayList<>();

      bufferedReader.lines().forEach(fileLines::add);

      assertEquals(fileLines.size(), 10);

    } catch (IOException e) {
      LOGGER.error("Error on open file {}", testFile.getAbsoluteFile());
    }
  }

  @AfterEach
  void tearDown() throws IOException {
    boolean isTestFileDelted = this.testFile.delete();

    if (!isTestFileDelted) {
      throw new IOException("Can't remove test file " + testFile.getAbsolutePath());
    }
  }
}
