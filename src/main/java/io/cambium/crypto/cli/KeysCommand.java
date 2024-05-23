package io.cambium.crypto.cli;

import java.io.File;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.FileConverter;

@Parameters(commandNames={KeysCommand.NAME}, commandDescription="Generate cryptographic keys or key pairs")
public class KeysCommand {
  public static final String NAME = "keys";
  
  @Parameter(names={"-sym", "--symmetric"}, 
      description="Create a symmetric secret key")
  public boolean symmetric = false;
  
  @Parameter(names={"-asym", "--asymmetric"}, 
      description="Create a set of asymmetric public and private keys")
  public boolean asymmetric = false;
  
  @Parameter(names={"-p", "--password"}, 
      description="When generating symmetric keys, optionally create one from a password",
      password=true)
  public String password = null;
  
  @Parameter(names={"-s", "--salt"}, 
      description="When generating symmetric keys with a password, optionally specify the salt")
  public String salt = null;
  
  @Parameter(names={"-k", "--key"}, 
      description="The file for the symmetric secret key",
      converter=FileConverter.class)
  public File key = null;
  
  @Parameter(names={"-pub", "--public-key"}, 
      description="The file for the asymmetric public key",
      converter=FileConverter.class)
  public File publicKey = null;
  
  @Parameter(names={"-priv", "--private-key"}, 
      description="The file for the asymmetric private key",
      converter=FileConverter.class)
  public File privateKey = null;
  
}
