package io.cambium.crypto.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class CliTest {
  
  @Test
  public void testSymmetric() throws IOException {
    CryptoCLI.throwReturnCode = true;
    
    String[] args1 = {"-g", "-s", "-o", "./target/secret.key" };
    CryptoCLI.main(args1);
    
    String[] args2 = {"-e", "-s", "-k", "./target/secret.key", "-iv", "A1BF39D2FFE6123A90BCD9225F6B2A71", 
                      "-i", "./pom.xml", "-o", "./target/encrypted.bin" };
    CryptoCLI.main(args2);
    
    String[] args3 = {"-d", "-s", "-k", "./target/secret.key", "-iv", "A1BF39D2FFE6123A90BCD9225F6B2A71", 
                      "-i", "./target/encrypted.bin", "-o", "./target/decrypted.xml" };
    CryptoCLI.main(args3);
    
    byte[] i = Files.readAllBytes(Paths.get("./pom.xml"));
    byte[] o = Files.readAllBytes(Paths.get("./target/decrypted.xml"));
    assertTrue(Arrays.equals(i, o));
  }
  
  @Test
  public void testAsymmetric() throws IOException {
    CryptoCLI.throwReturnCode = true;
    
    String[] args1 = {"-g", "-a", "-pub", "./target/public.key", "-priv", "./target/private.key" };
    CryptoCLI.main(args1);
    
    String[] args2 = {"-e", "-a", "-k", "./target/encrypted.key", "-pub", "./target/public.key",   
                      "-iv", "A1BF39D2FFE6123A90BCD9225F6B2A71",
                      "-i", "./pom.xml", "-o", "./target/encrypted.bin" };
    CryptoCLI.main(args2);
    
    String[] args3 = {"-d", "-a", "-k", "./target/encrypted.key", "-priv", "./target/private.key", 
                      "-iv", "A1BF39D2FFE6123A90BCD9225F6B2A71",
                      "-i", "./target/encrypted.bin", "-o", "./target/decrypted.xml" };
    CryptoCLI.main(args3);
    
    byte[] i = Files.readAllBytes(Paths.get("./pom.xml"));
    byte[] o = Files.readAllBytes(Paths.get("./target/decrypted.xml"));
    assertTrue(Arrays.equals(i, o));
  }

}
