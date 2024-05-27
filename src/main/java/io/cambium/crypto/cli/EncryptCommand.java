package io.cambium.crypto.cli;

import java.io.File;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.FileConverter;

@Parameters(commandNames={EncryptCommand.NAME}, commandDescription="Encrypt a file")
public class EncryptCommand {
  public static final String NAME = "encrypt";
  
  @Parameter(names={"-sym", "--symmetric"}, 
      description="Use symmetric encryption with a single secret key")
  public boolean symmetric = false;
  
  @Parameter(names={"-asym", "--asymmetric"}, 
      description="Use asymmetric encryption with a public key")
  public boolean asymmetric = false;
    
  @Parameter(names={"-k", "--key"}, 
      description="Specify the key for symmetric encryption, "
          + "or where to create the encrypted secret key for asymmetric encryption",
      converter=FileConverter.class)
  public File key = null;
  
  @Parameter(names={"-pub", "--public-key"}, 
      description="The file for the asymmetric public key",
      converter=FileConverter.class)
  public File publicKey = null;
  
  @Parameter(names={"-iv", "--initialization-vector"},
      description="16-byte Initialization vector, represented as a 32-character hex string;"
          + " example: c6069ad81f8a6b8b281cb5627288c1e2 ")
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
