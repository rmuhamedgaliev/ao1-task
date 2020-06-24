package io.github.rmuhamedgaliev.services.parser;

import io.github.rmuhamedgaliev.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserServiceTest {

  private ParserService parserService;
  private MockMemoryPrinter csvPrinter;

  @BeforeEach
  void setUp() {

    String csvFilePath = "src/test/resources/csv";

    this.csvPrinter = new MockMemoryPrinter();


    this.parserService = new CommonParserService(
      csvFilePath,
      this.csvPrinter,
      10
    );
  }

  @Test
  public void checkSortedProductsSize() {
    parserService.processFiles();

    assertEquals(2000, this.csvPrinter.getProductList().size());
  }

  @Test
  public void checkLowestPriceProduct() {

    String pr = "994485,product #994485,condition #994485,state #994485,276.62515";

    Product product = Product.of(pr);

    parserService.processFiles();

    assertEquals(product, this.csvPrinter.getProductList().get(0));
  }
}
