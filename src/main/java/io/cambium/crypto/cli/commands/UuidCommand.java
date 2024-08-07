package io.cambium.crypto.cli.commands;

import com.beust.jcommander.Parameters;

@Parameters(commandNames={UuidCommand.NAME}, 
            commandDescription="Generate universally unique IDs (UUIDs)")
public class UuidCommand {
  public static final String NAME = "uuid";  
}
