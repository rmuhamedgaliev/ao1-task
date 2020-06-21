package io.github.rmuhamedgaliev.services.parser;

import io.github.rmuhamedgaliev.model.Product;

import java.util.List;
import java.util.Map;

public interface CSVPrinter {

  void printCSV(Map<Integer, List<Product>> products);
}
