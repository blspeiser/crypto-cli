package io.cambium.crypto.cli.executors;

import java.io.IOException;
import java.util.UUID;

import io.cambium.crypto.cli.commands.UuidCommand;

public class UuidCommandExecutor extends BaseCommandExecutor<UuidCommand> {

  public UuidCommandExecutor(boolean quiet) {
    super(quiet);
  }

  public void execute(UuidCommand arguments) throws IOException {
    UUID uuid = UUID.randomUUID();
    if(!this.quiet) {
      System.out.println(uuid.toString());
    }
  }
  
}
