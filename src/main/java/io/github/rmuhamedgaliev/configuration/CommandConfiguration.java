package io.github.rmuhamedgaliev.configuration;

import io.github.rmuhamedgaliev.commands.Command;
import io.github.rmuhamedgaliev.commands.GenerateCommand;
import io.github.rmuhamedgaliev.commands.ParseCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.HashMap;


@Configuration
public class CommandConfiguration {

  @Autowired
  private ApplicationContext applicationContext;

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
    return new GenerateCommand();
  }

  @Bean
  public Command parseCommand() {
    return new ParseCommand();
  }

}
