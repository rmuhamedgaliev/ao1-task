package io.github.rmuhamedgaliev.services.parser;

import io.github.rmuhamedgaliev.model.Product;

import java.util.Comparator;

public class ProductComparator implements Comparator<Product> {
  @Override
  public int compare(Product firstProduct, Product secondProduct) {

    if (firstProduct.getPrice() > secondProduct.getPrice()) {
      return 1;
    }

    if (firstProduct.getPrice() < secondProduct.getPrice()) {
      return -1;
    }

    return firstProduct.getName()
      .compareTo(
        secondProduct.getName()
      );
  }
}
