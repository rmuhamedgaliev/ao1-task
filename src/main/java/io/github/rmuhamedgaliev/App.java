package io.github.rmuhamedgaliev;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.rmuhamedgaliev.commands.Command;
import io.github.rmuhamedgaliev.configuration.CommandConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.Optional;

@Configuration
@ComponentScan(basePackages = "io.github.rmuhamedgaliev")
@Import(CommandConfiguration.class)
public class App {

  private static HashMap<String, Command> commandRegistry;

  @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
  @Autowired
  private App(HashMap<String, Command> commandRegistryBean) {
    commandRegistry = commandRegistryBean;
  }

  public static void main(String[] args) {
    if (args.length > 0) {
      parseCommand(args);
    } else {
      System.out.println("Invalid command");
    }
  }

  private static void parseCommand(String[] args) {
    Optional<Command> command = Optional.ofNullable(
      commandRegistry.get(args[0]
      )
    );

    if (command.isPresent()) {
      System.out.println(command.get().getCommandName());
    } else {
      System.err.println("Invalid command");
    }
  }

}
