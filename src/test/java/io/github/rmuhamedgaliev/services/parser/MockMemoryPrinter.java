package io.github.rmuhamedgaliev.services.parser;

import io.github.rmuhamedgaliev.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MockMemoryPrinter implements CSVPrinter {

  private final List<Product> productList = new ArrayList<>();

  @Override
  public void printCSV(Map<Integer, List<Product>> products) {
    for (Map.Entry<Integer, List<Product>> entry : products.entrySet()) {
      productList.addAll(entry.getValue());
    }
  }

  public List<Product> getProductList() {
    return this.productList;
  }
}
