package io.github.rmuhamedgaliev.services.parser;

import io.github.rmuhamedgaliev.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ConsoleCSVPrinter implements CSVPrinter {
  private final Logger LOGGER = LoggerFactory.getLogger(ConsoleCSVPrinter.class);

  @Override
  public void printCSV(Map<Integer, List<Product>> products) {
    for (Map.Entry<Integer, List<Product>> entry : products.entrySet()) {
      entry.getValue().forEach(product -> {
        LOGGER.info("{}", product);
      });
    }
  }
}
