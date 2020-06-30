package io.github.rmuhamedgaliev.services.parser;

import io.github.rmuhamedgaliev.model.Product;

import java.util.Set;

public interface CSVPrinter {

  void printCSV(Set<Product> products);
}
