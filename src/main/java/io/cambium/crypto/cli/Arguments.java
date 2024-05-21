package io.cambium.crypto.cli;

import java.io.File;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

public class Arguments {
  
  @Parameter(names = {"-h", "-?", "--help"}, help=true, 
      description="Display system usage")
  public boolean help = false;
  
  @Parameter(names={"-e", "--encrypt"}, 
      description="Option required for encryption")
  public boolean encrypt = false;
  
  @Parameter(names={"-d", "--decrypt"}, 
      description="Option required for decryption")
  public boolean decrypt = false;
  
  @Parameter(names={"-g", "--generate"}, 
      description="Generate keys for encryption/decryption")
  public boolean generateKeys = false;
  
  @Parameter(names={"-s", "--symmetric"}, 
      description="When generating keys, produce a symmetric secret key")
  public boolean symmetric = false;
  
  @Parameter(names={"-a", "--asymmetric"}, 
      description="When generating keys, produce a set of asymmetric public and private keys")
  public boolean asymmetric = false;
  
  @Parameter(names={"-p", "--password"}, 
      description="When generating symmetric keys, optionally create one from a password",
      password=true)
  public String password = null;
  
  @Parameter(names={"-x", "--salt"}, 
      description="When generating symmetric keys from a password, optionally specify the salt")
  public String salt = null;
  
  @Parameter(names={"-k", "--key"}, 
      description="The file for the secret key or the asymmetrically encrypted key",
      converter=FileConverter.class)
  public File key = null;
  
  @Parameter(names={"-iv", "--initialization-vector"},
      description="16-byte Initialization vector, represented as a 32-character hex string;"
          + " example: A1BF39D2FFE6123A90BCD9225F6B2A71")
  public String initializationVector = null;
  
  @Parameter(names={"-i", "--input-file"}, 
      description="File to be encrypted/decrypted",
      converter=FileConverter.class)
  public File input = null;
  
  @Parameter(names={"-o", "--output-file"}, 
      description="File to store the encrypted/decrypted output",
      converter=FileConverter.class)
  public File output = null;
  
  @Parameter(names={"-pub", "--public-key"}, 
      description="The file for the public key",
      converter=FileConverter.class)
  public File publicKey = null;
  
  @Parameter(names={"-priv", "--private-key"}, 
      description="The file for the private key",
      converter=FileConverter.class)
  public File privateKey = null;
  
}
