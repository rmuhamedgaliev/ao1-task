package io.github.rmuhamedgaliev.services.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class CommonGenerateCSV implements GenerateService {
  private final static Logger LOGGER = LoggerFactory.getLogger(CommonGenerateCSV.class);

  private final int COUNT_OF_THREADS = 20;
  private final ExecutorService executorService = Executors.newFixedThreadPool(COUNT_OF_THREADS);

  private final long csvFileNumberPerLine;
  private final String csvFilePath;
  private final int timeOut;

  public CommonGenerateCSV(long csvFileNumberPerLine, String csvFilePath, int timeOut) {
    this.csvFileNumberPerLine = csvFileNumberPerLine;
    this.csvFilePath = csvFilePath;
    this.timeOut = timeOut;
  }

  @Override
  public void generateCSVFiles(int countOfFiles) {
    LOGGER.debug("Start generate CSV {} files.", countOfFiles);
    try {

      File csvFileFolders = createFilePath();

      IntStream.range(0, countOfFiles).forEach(
        index -> {
          File file = new File(
            csvFileFolders.getAbsolutePath() + "/" + index + ".csv"
          );
          executorService.submit(new CreateFileTask(file, csvFileNumberPerLine));
        }
      );
      executorService.shutdown();
      executorService.awaitTermination(timeOut, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private File createFilePath() throws IOException {
    Path path = Paths.get(csvFilePath);
    if (!path.toFile().exists()
      && !path.toFile().canRead()
    ) {
      if (path.toFile().mkdir()) {
        LOGGER.info("Create folder for CSV files");
      } else {
        throw new IOException("Can't create folder " + csvFilePath);
      }
    }
    return path.toFile();
  }

}
