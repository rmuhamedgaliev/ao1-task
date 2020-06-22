package io.github.rmuhamedgaliev.services.parser;

import io.github.rmuhamedgaliev.model.Product;
import io.github.rmuhamedgaliev.model.ProductList;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CommonParserService implements ParserService {
  private final Logger LOGGER = LoggerFactory.getLogger(CommonParserService.class);

  private final ExecutorService pool;
  private final File csvFileFolder;
  private final String separator;
  private final ProductList productList;
  private final int timeOut;

  public CommonParserService(int countOfParallelReadFiles, String csvFileFolderPath, String separator, ProductList productList, int timeOut) {
    this.pool = Executors.newFixedThreadPool(countOfParallelReadFiles);
    this.csvFileFolder = new File(csvFileFolderPath);
    this.separator = separator;
    this.productList = productList;
    this.timeOut = timeOut;
  }

  @Override
  public void processFiles() {
    LOGGER.info("Start process CSV files");

    try {
      List<String> fileContents = getFilesContent();

      fileContents.parallelStream().forEach(productLine -> {
        Product product = Product.of(productLine.split(separator));
        this.productList.addProduct(product);
      });

      this.productList.printSortedByPriceProducts();

      pool.shutdown();
      pool.awaitTermination(timeOut, TimeUnit.MINUTES);

      LOGGER.info("End processing CSV file and sort products by price. Please check output file");

    } catch (InterruptedException e) {
      LOGGER.error("Error on process CSV files with exception {}", ExceptionUtils.getStackTrace(e));
    }
  }

  private List<String> getFilesContent() {
    List<String> fileContents = new ArrayList<>();

    Set<Future<List<String>>> parseFileTasks = prepareReadFileTasks(csvFileFolder);

    for (Future<List<String>> future : parseFileTasks) {
      try {
        fileContents.addAll(future.get());
      } catch (InterruptedException | ExecutionException e) {
        LOGGER.error("Error on parse file with exception {}", ExceptionUtils.getStackTrace(e));
      }
    }

    return fileContents;
  }

  private Set<Future<List<String>>> prepareReadFileTasks(File csvFileFolder) {
    Set<Future<List<String>>> parseFileTasks = new HashSet<>();

    DirectoryStream<Path> stream = null;

    if (csvFileFolder.exists()) {
      try {
        stream = Files.newDirectoryStream(csvFileFolder.toPath(), "*.csv");

        for (Path filePath : stream) {
          Future<List<String>> readFileFuture = this.pool.submit(
            new ParseFileTask(csvFileFolder, filePath.toFile().getName())
          );

          parseFileTasks.add(readFileFuture);
        }

        if (parseFileTasks.size() == 0) {
          LOGGER.error("Files in folder with csv not exists");
        }

      } catch (IOException e) {
        LOGGER.error("Error on read csv files in folder {}", csvFileFolder.toPath());
      } finally {
        try {
          if (stream != null) {
            stream.close();
          }
        } catch (IOException e) {
          LOGGER.error("Can't close list folder files stream wit exception {}", ExceptionUtils.getStackTrace(e));
        }
      }
    } else {
      LOGGER.error("Folder with CSV files not exists");
    }


    return parseFileTasks;
  }

}
