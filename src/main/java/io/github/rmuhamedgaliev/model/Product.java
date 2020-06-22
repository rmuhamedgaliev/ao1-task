package io.github.rmuhamedgaliev.model;

import org.apache.commons.lang3.RandomUtils;

import java.util.Objects;

//product ID (integer), Name (string), Condition (string), State (string), Price (float)
public class Product {
  private final int id;
  private final String name;
  private final String condition;
  private final String state;
  private final Float price;

  private Product(int id, String name, String condition, String state, Float price) {
    this.id = id;
    this.name = name;
    this.condition = condition;
    this.state = state;
    this.price = price;
  }

  public static Product of(String[] productElements) {
    int id = Integer.parseInt(productElements[0]);
    String name = productElements[1];
    String condition = productElements[2];
    String state = productElements[3];
    Float price = Float.parseFloat(productElements[4]);

    return new Product(
      id,
      name,
      condition,
      state,
      price
    );
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

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCondition() {
    return condition;
  }

  public String getState() {
    return state;
  }

  public Float getPrice() {
    return price;
  }

  public String toCSV(String separator) {
    return this.id + separator
      + name + separator
      + condition + separator
      + state + separator
      + price + "\n";
  }

  @Override
  public String toString() {
    return
      id + "\t" +
        name + "\t" +
        condition + "\t" +
        state + "\t" +
        price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Product product = (Product) o;
    return id == product.id &&
      name.equals(product.name) &&
      condition.equals(product.condition) &&
      state.equals(product.state) &&
      price.equals(product.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, condition, state, price);
  }
}
