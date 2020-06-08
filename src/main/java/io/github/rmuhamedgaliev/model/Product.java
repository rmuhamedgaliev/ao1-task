package io.github.rmuhamedgaliev.model;

import org.apache.commons.lang3.RandomUtils;

//product ID (integer), Name (string), Condition (string), State (string), Price (float)
public class Product {
  private int id;
  private String name;
  private String condition;
  private String state;
  private Float price;

  public Product(int id, String name, String condition, String state, Float price) {
    this.id = id;
    this.name = name;
    this.condition = condition;
    this.state = state;
    this.price = price;
  }

  public String toCSV(String separator) {
    return this.id + separator
      + name + separator
      + condition + separator
      + state + separator
      + price + "\n";
  }

  public static Product createProduct() {
    int id = RandomUtils.nextInt(0, 1000000);
    return new Product(
      id,
      "product #" + id,
      "condition #" + id,
      "state #" + id,
      RandomUtils.nextFloat(0, 1000000)
    );
  }
}
