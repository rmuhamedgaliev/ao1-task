package io.github.rmuhamedgaliev.services.parser;

import io.github.rmuhamedgaliev.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MockMemoryPrinter implements CSVPrinter {

  private final List<Product> productSet = new ArrayList<>();

  @Override
  public void printCSV(Set<Product> products) {
    productSet.addAll(products);
  }

  public List<Product> getProductList() {
    return this.productSet;
  }
}
