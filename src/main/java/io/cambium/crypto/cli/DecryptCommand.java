package io.cambium.crypto.cli;

import java.io.File;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.FileConverter;

@Parameters(commandNames={DecryptCommand.NAME}, commandDescription="Decrypt a file")
public class DecryptCommand {
  public static final String NAME = "decrypt";
  
  @Parameter(names={"-sym", "--symmetric"}, 
      description="Use symmetric decryption with a single secret key")
  public boolean symmetric = false;
  
  @Parameter(names={"-asym", "--asymmetric"}, 
      description="Use asymmetric decryption with a private key")
  public boolean asymmetric = false;
    
  @Parameter(names={"-k", "--key"}, 
      description="Specify the key (or encrypted key) for decryption",
      converter=FileConverter.class)
  public File key = null;
  
  @Parameter(names={"-priv", "--private-key"}, 
      description="The file for the private key",
      converter=FileConverter.class)
  public File privateKey = null;
  
  @Parameter(names={"-iv", "--initialization-vector"},
      description="16-byte Initialization vector, represented as a 32-character hex string;"
          + " example: A1BF39D2FFE6123A90BCD9225F6B2A71")
  public String initializationVector = null;
  
  @Parameter(names={"-i", "--input-file"}, 
      description="File to be encrypted",
      converter=FileConverter.class)
  public File input = null;
  
  @Parameter(names={"-o", "--output-file"}, 
      description="File to store the encrypted output",
      converter=FileConverter.class)
  public File output = null;
}
