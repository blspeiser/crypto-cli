package io.cambium.crypto.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class CliTest {
  
  @Test
  public void testSymmetric() throws IOException {
    CryptoCLI.throwReturnCode = true;
    
    CryptoCLI.main("-g", "-s", "-o", "./target/secret.key");
    
    CryptoCLI.main("-e", "-s", "-k", "./target/secret.key", 
        "-iv", "A1BF39D2FFE6123A90BCD9225F6B2A71", 
        "-i", "./pom.xml", "-o", "./target/encrypted.bin");
    
    CryptoCLI.main("-d", "-s", "-k", "./target/secret.key", 
        "-iv", "A1BF39D2FFE6123A90BCD9225F6B2A71", 
        "-i", "./target/encrypted.bin", "-o", "./target/decrypted.xml");
    
    byte[] i = Files.readAllBytes(Paths.get("./pom.xml"));
    byte[] o = Files.readAllBytes(Paths.get("./target/decrypted.xml"));
    assertTrue(Arrays.equals(i, o));
  }
  
  @Test
  public void testAsymmetric() throws IOException {
    CryptoCLI.throwReturnCode = true;
    
    CryptoCLI.main("-g", "-a", "-pub", "./target/public.key", "-priv", "./target/private.key");
    
    CryptoCLI.main("-e", "-a", "-k", "./target/encrypted.key", "-pub", "./target/public.key",   
        "-iv", "A1BF39D2FFE6123A90BCD9225F6B2A71",
        "-i", "./pom.xml", "-o", "./target/encrypted.bin");
    
    CryptoCLI.main("-d", "-a", "-k", "./target/encrypted.key", "-priv", "./target/private.key", 
        "-iv", "A1BF39D2FFE6123A90BCD9225F6B2A71",
        "-i", "./target/encrypted.bin", "-o", "./target/decrypted.xml");
    
    byte[] i = Files.readAllBytes(Paths.get("./pom.xml"));
    byte[] o = Files.readAllBytes(Paths.get("./target/decrypted.xml"));
    assertTrue(Arrays.equals(i, o));
  }

  @Test
  public void testHelp() throws IOException {
    CryptoCLI.throwReturnCode = true;
    CryptoCLI.suppressOutput = true;
    try {
      CryptoCLI.main((String[])null);
      fail("did not throw return code");
    } catch(Exception e) {
      //expected
    }
    CryptoCLI.main("--help");
  }
  
}
