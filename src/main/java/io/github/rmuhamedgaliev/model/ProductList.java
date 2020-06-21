package io.github.rmuhamedgaliev.model;

import io.github.rmuhamedgaliev.services.parser.CSVPrinter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class ProductList {
  private final CSVPrinter csvPrinter;
  private final int countOfDuplicates;

  public Map<Integer, List<Product>> products = new ConcurrentHashMap<>();

  public ProductList(CSVPrinter csvPrinter, int countOfDuplicates) {
    this.csvPrinter = csvPrinter;
    this.countOfDuplicates = countOfDuplicates;
  }

  public void printSortedByPriceProducts() {
    Map<Integer, List<Product>> products = this.sortByLowPrice();
    csvPrinter.printCSV(products);
  }

  private Map<Integer, List<Product>> sortByLowPrice() {
    Comparator<Integer> valueComparator =
      (currentProduct, nextProduct) -> {
        int compareResult =
          this.products.get(currentProduct).get(0).getPrice().compareTo(this.products.get(nextProduct).get(0).getPrice());

        if (compareResult == 0)
          return 1;
        else
          return compareResult;
      };

    Map<Integer, List<Product>> sortedByValues =
      new TreeMap<>(valueComparator);
    sortedByValues.putAll(this.products);
    return sortedByValues;
  }

  public void addProduct(Product product) {
    if (this.products.get(product.getId()) != null) {
      List<Product> products = this.products.get(product.getId());
      if (products.size() < countOfDuplicates) {
        products.add(product);
      }
      this.products.put(product.getId(), products);
    } else {
      List<Product> products = new ArrayList<>();
      products.add(product);
      this.products.put(product.getId(), products);
    }
  }
}
