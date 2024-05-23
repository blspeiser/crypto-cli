package io.cambium.crypto.cli;

import java.io.File;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

public class Arguments {
  
  @Parameter(names = {"-?", "--help"}, help=true, 
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
  
  @Parameter(names={"-h", "--hash"}, 
      description="Generate hash")
  public boolean generateHash = false;
  
  @Parameter(names={"-b", "--bytes", "--hex"}, 
      description="Generate hex bytestring")
  public boolean generateBytes = false;
  
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
      description="Specify the salt for generating keys or hashes")
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
  
  @Parameter(names={"-md5"}, 
      description="Hash using MD5")
  public boolean md5 = false;
  
  @Parameter(names={"-sha256"}, 
      description="Hash using SHA-256")
  public boolean sha256 = false;
  
  @Parameter(names={"-sha512"}, 
      description="Hash using SHA3-512")
  public boolean sha512 = false;
  
  @Parameter(names={"-n", "--byte-count"},
      description="Number of bytes to generate in a bytestring")
  public Integer numberOfBytes = 16;
  
}
