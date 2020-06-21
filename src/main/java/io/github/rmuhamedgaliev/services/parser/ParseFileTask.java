package io.github.rmuhamedgaliev.services.parser;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ParseFileTask implements Callable<List<String>> {
  private final Logger LOGGER = LoggerFactory.getLogger(ParseFileTask.class);

  private final File csvFileFolder;
  private final String fileName;

  public ParseFileTask(File csvFileFolder, String fileName) {
    this.csvFileFolder = csvFileFolder;

    this.fileName = fileName;
  }

  @Override
  public List<String> call() {

    List<String> productStrings = new ArrayList<>();

    File file = new File(
      this.csvFileFolder.getAbsolutePath()
        + File.separator
        + this.fileName);

    try (
      BufferedReader bf = new BufferedReader(
        new FileReader(
          file,
          StandardCharsets.UTF_8
        )
      )
    ) {

      String line = bf.readLine();
      while (line != null) {
        productStrings.add(line);

        line = bf.readLine();
      }
    } catch (IOException e) {
      LOGGER.error("Error on parse CSV file {}", ExceptionUtils.getStackTrace(e));
    }

    return productStrings;
  }
}
