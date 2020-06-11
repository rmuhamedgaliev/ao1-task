package io.github.rmuhamedgaliev.services.generator;

import io.github.rmuhamedgaliev.model.Product;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CreateFileTask implements Runnable {
  private final static Logger LOGGER = LoggerFactory.getLogger(CreateFileTask.class);

  private final File outputFile;
  private final long numberOfLines;

  public CreateFileTask(File outputFile, long numberOfLines) {
    this.outputFile = outputFile;
    this.numberOfLines = numberOfLines;
  }

  @Override
  public void run() {

    try (
      BufferedWriter writer = new BufferedWriter(
        new FileWriter(
          outputFile,
          StandardCharsets.UTF_8
        )
      )
    ) {

      LOGGER.debug("Start generate CSV file {}", outputFile.getAbsoluteFile());

      for (int i = 0; i < numberOfLines; i++) {
        Product product = Product.createProduct();
        writer.write(product.toCSV(","));
      }

      LOGGER.debug("End generate CSV file {}", outputFile.getAbsoluteFile());
    } catch (IOException fileWriteException) {
      LOGGER.error("Error on generate CSV file {}", ExceptionUtils.getStackTrace(fileWriteException));
    }
  }
}
