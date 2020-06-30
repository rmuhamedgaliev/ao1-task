package io.github.rmuhamedgaliev.services.parser;

import io.github.rmuhamedgaliev.model.Product;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CommonParserService implements ParserService {
  private final Logger LOGGER = LoggerFactory.getLogger(CommonParserService.class);

  private final File csvFileFolder;
  private final CSVPrinter printer;
  private final int countOfDuplicatesProduct;
  private final int countOfProducts;

  public CommonParserService(String csvFileFolderPath, CSVPrinter printer, int countOfDuplicatesProduct, int countOfProducts) {
    this.csvFileFolder = new File(csvFileFolderPath);
    this.printer = printer;
    this.countOfDuplicatesProduct = countOfDuplicatesProduct;
    this.countOfProducts = countOfProducts;
  }

  @Override
  public void processFiles() {
    LOGGER.info("Start process CSV files");

    ProductComparator comparator = new ProductComparator();

    SortedSet<Product> products = new ConcurrentSkipListSet<>(comparator);

    try (
      DirectoryStream<Path> stream = Files.newDirectoryStream(csvFileFolder.toPath(), "*.csv")
    ) {
      Stream<Path> pathStream = StreamSupport.stream(stream.spliterator(), true);

      pathStream.parallel().forEach(path -> {

        try (Stream<String> lines = Files.lines(path)) {
          lines
            .map(Product::of)
            .forEach(product -> {

              products.add(product);

              long countOfDuplicates = products.parallelStream()
                .filter(
                  productElement -> productElement.getId() == product.getId()
                )
                .count();

              if (countOfDuplicates > countOfDuplicatesProduct) {
                products.stream()
                  .parallel()
                  .filter(productElement -> productElement.getId() == product.getId())
                  .skip(countOfDuplicatesProduct)
                  .findAny()
                  .ifPresent(products::remove);
              }

              if (products.size() > countOfProducts) {
                products.remove(products.last());
              }
            });
        } catch (IOException e) {
          LOGGER.error("Error on parsing CSV file with exception {}", ExceptionUtils.getStackTrace(e));
        }
      });

      pathStream.close();

      printer.printCSV(products);

    } catch (IOException e) {
      LOGGER.error("Error on read csv files in folder {}", csvFileFolder.toPath());
    }
  }

}
