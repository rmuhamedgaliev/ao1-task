package io.github.rmuhamedgaliev.services.parser;

import io.github.rmuhamedgaliev.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class ConsoleCSVPrinter implements CSVPrinter {
  private final Logger LOGGER = LoggerFactory.getLogger(ConsoleCSVPrinter.class);

  @Override
  public void printCSV(Set<Product> products) {
    products.forEach(product -> LOGGER.info("{}", product));
  }
}
