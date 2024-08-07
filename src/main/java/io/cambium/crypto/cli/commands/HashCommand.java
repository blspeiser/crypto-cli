package io.cambium.crypto.cli.commands;

import java.io.File;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.FileConverter;

import io.cambium.crypto.cli.validators.HashCommandValidator;

@Parameters(commandNames={HashCommand.NAME}, 
            commandDescription="Generate hashes",
            parametersValidators={HashCommandValidator.class})
public class HashCommand {
  public static final String NAME = "hash";
  
  @Parameter(names={"-md5"}, 
      description="Hash using MD5")
  public boolean md5 = false;
  
  @Parameter(names={"-sha1"}, 
      description="Hash using SHA-1")
  public boolean sha1 = false;
  
  @Parameter(names={"-sha256"}, 
      description="Hash using SHA-256")
  public boolean sha256 = false;
  
  @Parameter(names={"-sha512"}, 
      description="Hash using SHA3-512")
  public boolean sha512 = false;
  
  @Parameter(names={"-s", "--salt"}, 
      description="Optionally specify the salt")
  public String salt = null;
  
  @Parameter(names={"-i", "--input-file"}, 
      description="File to be hashed",
      converter=FileConverter.class)
  public File input = null;
  
  @Parameter(names={"-o", "--output-file"}, 
      description="Optionally store the output as a binary file",
      converter=FileConverter.class)
  public File output = null;
  
  @Parameter(names={"-in", "--stdin"}, 
      description="Read standard input instead of using an input file")
  public boolean stdin = false;
  
  @Parameter(names={"-t", "--text"}, description="Text to be hashed instead of using an input file")
  public String text = null;
}
