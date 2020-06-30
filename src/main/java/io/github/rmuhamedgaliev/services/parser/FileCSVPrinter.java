package io.github.rmuhamedgaliev.services.parser;

import io.github.rmuhamedgaliev.model.Product;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class FileCSVPrinter implements CSVPrinter {
  private final Logger LOGGER = LoggerFactory.getLogger(FileCSVPrinter.class);

  private final String outFileName;
  private final String separator;

  public FileCSVPrinter(String outFileName, String separator) {
    this.outFileName = outFileName;
    this.separator = separator;
  }

  @Override
  public void printCSV(Set<Product> products) {

    File outFile = null;

    try {
      outFile = this.prepareOutPutFile(this.outFileName);
    } catch (IOException e) {
      LOGGER.error(ExceptionUtils.getStackTrace(e));
    }

    try (
      FileWriter fw = new FileWriter(
        outFile,
        StandardCharsets.UTF_8
      )
    ) {
      for (Product product : products) {
        fw.write(product.toCSV(separator));
      }
      LOGGER.info("End write result to file with name {}", outFileName);
    } catch (IOException e) {
      LOGGER.error("Error on write result to output csv file with exception {}", ExceptionUtils.getStackTrace(e));
    }
  }

  private File prepareOutPutFile(String outFileName) throws IOException {
    File file = new File(outFileName);

    if (file.exists()) {
      boolean isOutputFileDeleted = file.delete();

      if (!isOutputFileDeleted) {
        throw new IOException("Can't initialise output file with name " + this.outFileName);
      }
    }

    return file;
  }
}
