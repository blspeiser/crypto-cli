package io.cambium.crypto.cli;

import com.beust.jcommander.Parameter;

public class Arguments {
  
  @Parameter(names = {"-?", "--help"}, help=true, 
      description="Display system usage")
  public boolean help = false;
  
  @Parameter(names = {"-q", "--quiet"}, hidden=true, 
      description="Suppress output (useful for unit testing)")
  public boolean quiet = false;

  @Parameter(names = {"-x", "--noexit"}, hidden=true, 
      description="Throw exceptions and do not exit (useful for unit testing)")
  public boolean throwInsteadOfExit = false;
  
  
}
