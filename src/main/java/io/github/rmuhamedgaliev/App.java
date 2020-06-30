package io.github.rmuhamedgaliev;

import io.github.rmuhamedgaliev.commands.Command;
import io.github.rmuhamedgaliev.configuration.CommandConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Configuration
@Import(CommandConfiguration.class)
public class App {
  private final static Logger LOGGER = LoggerFactory.getLogger(App.class);

  private static HashMap<String, Command> commandRegistry;

  @SuppressWarnings("unchecked")
  public static void main(String[] args) {

    checkExistsConfigFile();

    ApplicationContext context = new AnnotationConfigApplicationContext(App.class);

    commandRegistry = (HashMap<String, Command>) context.getBean("commandRegistry");

    if (args.length > 0) {
      parseCommand(args);
    } else {
      LOGGER.error("Invalid command. Please use follow commands.\n");
      commandRegistry.forEach((key, command) -> command.printHelp());
    }
  }

  private static void checkExistsConfigFile() {
    RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();

    List<String> listOfArguments = runtimeMxBean.getInputArguments();

    String argument = listOfArguments
      .stream()
      .filter(args -> args.contains("spring.config.location"))
      .findFirst()
      .orElse(null);

    if (argument == null) {
      LOGGER.error("Please set config file path with JVM argument.\nExample: -Dspring.config.location=file:./config/application.properties");
      System.exit(1);
    }
  }

  private static void parseCommand(String[] args) {
    Optional<Command> command = Optional.ofNullable(
      commandRegistry.get(args[0]
      )
    );

    if (command.isPresent()) {
      command.get().run();
    } else {
      LOGGER.error("Invalid command. Please use follow commands.");
      commandRegistry.forEach((key, operation) -> operation.printHelp());
    }
  }

}
