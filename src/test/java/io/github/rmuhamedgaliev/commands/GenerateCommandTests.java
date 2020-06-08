package io.github.rmuhamedgaliev.commands;

import io.github.rmuhamedgaliev.configuration.CommandConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Profile("test")
@SpringJUnitConfig(classes = CommandConfiguration.class)
@DisplayName("Check generate CSV command")
class GenerateCommandTests {

  @Autowired
  private Command generateCommand;

  @Test
  @DisplayName("Check valid display name")
  public void checkCallGenerateCommand() {
    assertEquals("generate", generateCommand.getCommandName());
  }

}
