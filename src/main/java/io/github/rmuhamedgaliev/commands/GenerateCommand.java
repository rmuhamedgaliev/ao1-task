package io.github.rmuhamedgaliev.commands;

import io.github.rmuhamedgaliev.services.generator.GenerateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateCommand implements Command {
  private final static Logger LOGGER = LoggerFactory.getLogger(GenerateCommand.class);

  private final static String COMMAND_NAME = "generate";

  private final GenerateService generateService;
  private final int countOfFiles;

  public GenerateCommand(int count_of_files, GenerateService generateService) {
    countOfFiles = count_of_files;
    this.generateService = generateService;
  }

  @Override
  public String getCommandName() {
    return COMMAND_NAME;
  }

  @Override
  public void printHelp() {
    String helpMessage = "Please use command `generate` for create CSV files.";
    LOGGER.info(helpMessage);
  }

  @Override
  public void run() {
    LOGGER.debug("Entered to generate CSV files command.\nStart generate {} files", countOfFiles);
    generateService.generateCSVFiles(countOfFiles);
    LOGGER.debug("End generate CSV file command");
  }
}
