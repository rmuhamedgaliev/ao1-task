package io.github.rmuhamedgaliev.commands;

import io.github.rmuhamedgaliev.services.parser.ParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParseCommand implements Command {
  private final static Logger LOGGER = LoggerFactory.getLogger(ParseCommand.class);

  private final static String COMMAND_NAME = "parse";

  private final ParserService parserService;

  public ParseCommand(ParserService parserService) {
    this.parserService = parserService;
  }

  @Override
  public String getCommandName() {
    return COMMAND_NAME;
  }

  @Override
  public void printHelp() {
    String helpMessage = "Please use command `parse` for parse CSV files and sort products";
    LOGGER.info(helpMessage);
  }

  @Override
  public void run() {
    this.parserService.processFiles();
  }
}
