package io.cambium.crypto.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames={BytesCommand.NAME}, commandDescription="Generate random byte strings for cryptographic inputs")
public class BytesCommand {
  public static final String NAME = "bytes";

  @Parameter(names={"-n", "--length"}, description="Number of bytes to generate in a byte string (16 if not specified)")
  public Integer numberOfBytes = 16;
    
}
