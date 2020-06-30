package io.github.rmuhamedgaliev.services.parser;

import io.github.rmuhamedgaliev.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserServiceTest {

  private ParserService parserService;
  private MockMemoryPrinter csvPrinter;

  private final static int COUNT_OF_PRODUCTS = 1000;
  private final static int COUNT_OF_DUPLICATE_PRODUCTS = 20;

  @BeforeEach
  void setUp() {

    String csvFilePath = "src/test/resources/csv";

    this.csvPrinter = new MockMemoryPrinter();


    this.parserService = new CommonParserService(
      csvFilePath,
      this.csvPrinter,
      COUNT_OF_DUPLICATE_PRODUCTS,
      COUNT_OF_PRODUCTS
    );
  }

  @Test
  public void checkSortedProductsSize() {
    parserService.processFiles();

    assertEquals(COUNT_OF_PRODUCTS, this.csvPrinter.getProductList().size());
  }

  @Test
  public void checkLowestPriceProduct() {

    String pr = "994485,product #994485,condition #994485,state #994485,276.62515";

    Product product = Product.of(pr);

    parserService.processFiles();

    Product firstSortedProduct = this.csvPrinter.getProductList().get(0);

    assertEquals(product, firstSortedProduct);

  }

  @Test
  public void checkDuplicatesInProducts() {

    int productId = 994485;

    parserService.processFiles();

    long countOfDuplicates = this.csvPrinter.getProductList().parallelStream()
      .filter(
        productElement -> productElement.getId() == productId
      )
      .count();

    assertEquals(COUNT_OF_DUPLICATE_PRODUCTS, countOfDuplicates);
  }
}
