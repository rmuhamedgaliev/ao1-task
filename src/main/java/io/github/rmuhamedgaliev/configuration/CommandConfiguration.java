package io.github.rmuhamedgaliev.configuration;

import io.github.rmuhamedgaliev.commands.Command;
import io.github.rmuhamedgaliev.commands.GenerateCommand;
import io.github.rmuhamedgaliev.commands.ParseCommand;
import io.github.rmuhamedgaliev.services.generator.CommonGenerateCSV;
import io.github.rmuhamedgaliev.services.generator.GenerateService;
import io.github.rmuhamedgaliev.services.parser.CSVPrinter;
import io.github.rmuhamedgaliev.services.parser.CommonParserService;
import io.github.rmuhamedgaliev.services.parser.ConsoleCSVPrinter;
import io.github.rmuhamedgaliev.services.parser.FileCSVPrinter;
import io.github.rmuhamedgaliev.services.parser.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Collection;
import java.util.HashMap;

@Configuration
@ComponentScan({"io.github.rmuhamedgaliev"})
@PropertySource("${spring.config.location}")
public class CommandConfiguration {

  @Autowired
  private ApplicationContext applicationContext;

  @Value("${app.csv.number-per-line:10}")
  private long csvFileNumberPerLine;

  @Value("${app.csv.file-path}")
  private String csvFilePath;

  @Value("${app.csv.separator:,}")
  private String csvSeparator;

  @Value("${app.csv.timeout-in-minutes:30}")
  private int timeOut;

  @Value("${app.printer.type:console}")
  private String printerType;

  @Value("${app.printer.file.out:out.csv}")
  private String outputFileName;

  @Value("${app.csv.generate.count-of-files:10}")
  private int countOfGenerateFiles;

  @Value("${app.products.count-of-duplicates:20}")
  private int countOfDuplicatesProduct;

  @Value("${app.products.count-of-products:1000}")
  private int countOfProducts;

  @Bean("commandRegistry")
  public HashMap<String, Command> commandRegistry() {
    HashMap<String, Command> commandHashMap = new HashMap<>();
    Collection<Command> commands = applicationContext.getBeansOfType(Command.class).values();
    commands.forEach(command -> commandHashMap.put(command.getCommandName(), command));

    return commandHashMap;
  }

  @Bean
  public Command generateCommand() {
    return new GenerateCommand(
      this.countOfGenerateFiles,
      generateService()
    );
  }

  @Bean
  public ParserService parserService() {
    return new CommonParserService(
      this.csvFilePath,
      csvPrinter(),
      countOfDuplicatesProduct,
      countOfProducts
    );
  }

  @Bean(name = "console")
  public CSVPrinter consolePrinter() {
    return new ConsoleCSVPrinter();
  }

  @Bean(name = "file")
  public CSVPrinter filePrinter() {
    return new FileCSVPrinter(
      this.outputFileName,
      this.csvSeparator
    );
  }

  @Bean
  CSVPrinter csvPrinter() {
    CSVPrinter csvPrinter = switch (this.printerType) {
      case "console" -> consolePrinter();
      case "file" -> filePrinter();
      default -> consolePrinter();
    };

    return csvPrinter;
  }

  @Bean(name = "parse")
  public Command parseCommand() {
    return new ParseCommand(parserService());
  }

  @Bean
  public GenerateService generateService() {
    return new CommonGenerateCSV(
      this.csvFileNumberPerLine,
      this.csvFilePath,
      this.timeOut
    );
  }

}
