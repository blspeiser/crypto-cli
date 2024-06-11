package io.cambium.crypto.cli;

import com.beust.jcommander.Parameter;

public class Arguments {
  
  @Parameter(names = {"-?", "--help"}, help=true, 
      description="Display system usage")
  public boolean help = false;
  
}
