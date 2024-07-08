package io.cambium.crypto.cli.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import io.cambium.crypto.cli.validators.BytesCommandValidator;

@Parameters(commandNames={BytesCommand.NAME}, 
            commandDescription="Generate byte strings for cryptographic inputs",
            parametersValidators={BytesCommandValidator.class})
public class BytesCommand {
  public static final String NAME = "bytes";

  @Parameter(names={"-n", "--length"}, description="Number of bytes to generate in a random byte string (16 if not specified)")
  public Integer numberOfBytes = 16;
    
  @Parameter(names={"-t", "--text"}, description="Text to be rendered as a hexadecimal byte string")
  public String text = null;
}
