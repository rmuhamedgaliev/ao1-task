package io.github.rmuhamedgaliev.commands;

public interface Command {

  String getCommandName();

  void printHelp();

  void run();
}
