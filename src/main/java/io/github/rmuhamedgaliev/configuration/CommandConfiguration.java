package io.github.rmuhamedgaliev.configuration;

import io.github.rmuhamedgaliev.commands.Command;
import io.github.rmuhamedgaliev.commands.GenerateCommand;
import io.github.rmuhamedgaliev.commands.ParseCommand;
import io.github.rmuhamedgaliev.services.generator.CommonGenerateCSV;
import io.github.rmuhamedgaliev.services.generator.GenerateService;
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

  @Value("${app.csv.number-per-line}")
  private long csvFileNumberPerLine;

  @Value("${app.csv.file-path}")
  private String csvFilePath;

  @Value("${app.csv.timeout-in-minutes}")
  private int timeOut;

  @Bean("commandRegistry")
  public HashMap<String, Command> commandRegistry() {
    HashMap<String, Command> commandHashMap = new HashMap<>();
    Collection<Command> commands = applicationContext.getBeansOfType(Command.class).values();
    commands.forEach(command -> {
      commandHashMap.put(command.getCommandName(), command);
    });

    return commandHashMap;
  }

  @Bean
  public Command generateCommand() {
    return new GenerateCommand(generateService());
  }

  @Bean
  public Command parseCommand() {
    return new ParseCommand();
  }

  @Bean
  public GenerateService generateService() {
    return new CommonGenerateCSV(csvFileNumberPerLine, csvFilePath, timeOut);
  }

}
