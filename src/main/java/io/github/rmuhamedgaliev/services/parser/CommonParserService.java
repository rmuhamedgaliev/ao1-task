package io.github.rmuhamedgaliev.services.parser;

import io.github.rmuhamedgaliev.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CommonParserService implements ParserService {
  private final Logger LOGGER = LoggerFactory.getLogger(CommonParserService.class);

  private final File csvFileFolder;
  private final CSVPrinter printer;
  private final int countOfDuplicatesProduct;

  public CommonParserService(String csvFileFolderPath, CSVPrinter printer, int countOfDuplicatesProduct) {
    this.csvFileFolder = new File(csvFileFolderPath);
    this.printer = printer;
    this.countOfDuplicatesProduct = countOfDuplicatesProduct;
  }

  @Override
  public void processFiles() {
    LOGGER.info("Start process CSV files");

    List<Product> productStream = new ArrayList<>();

    try (
      DirectoryStream<Path> stream = Files.newDirectoryStream(csvFileFolder.toPath(), "*.csv")
    ) {
      Stream<Path> pathStream = StreamSupport.stream(stream.spliterator(), true);

      List<Product> finalProductStream = productStream;
      pathStream.forEach(path -> {

        try (Stream<String> lines = Files.lines(path)) {
          lines
            .map(Product::of)
            .forEach(finalProductStream::add);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });

      pathStream.close();

      productStream = productStream.stream().parallel().sorted(Comparator.comparing(Product::getPrice))
        .collect(Collectors.toList());

      Map<Integer, List<Product>> resultProducts = new LinkedHashMap<>();

      productStream.stream().parallel().forEachOrdered(product -> {
        if (resultProducts.get(product.getId()) != null) {
          List<Product> products = resultProducts.get(product.getId());
          if (products.size() < countOfDuplicatesProduct) {
            products.add(product);
          }
        } else {
          List<Product> products = new ArrayList<>();
          products.add(product);
          resultProducts.put(product.getId(), products);
        }
      });

      printer.printCSV(resultProducts);

    } catch (IOException e) {
      LOGGER.error("Error on read csv files in folder {}", csvFileFolder.toPath());
    }
  }

}
