package io.cambium.crypto.cli.executors;

import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

public abstract class BaseCommandExecutor<T> {
  protected final boolean quiet;
  
  protected static final OpenOption[] WRITE_OPTIONS = {
      StandardOpenOption.CREATE,
      StandardOpenOption.TRUNCATE_EXISTING, 
      StandardOpenOption.WRITE
  };
  
  protected BaseCommandExecutor(boolean quiet) {
    this.quiet = quiet;
  }
  
  public abstract void execute(T command) throws Exception;

}
