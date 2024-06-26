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
    CryptoCLI.suppressOutput = true;
    
    CryptoCLI.main("keys", "-sym", "-k", "./target/secret.key");
    
    CryptoCLI.main("encrypt", "-sym", "-k", "./target/secret.key", 
        "-iv", "c6069ad81f8a6b8b281cb5627288c1e2", 
        "-i", "./pom.xml", "-o", "./target/encrypted.bin");
    
    CryptoCLI.main("decrypt", "-sym", "-k", "./target/secret.key", 
        "-iv", "c6069ad81f8a6b8b281cb5627288c1e2", 
        "-i", "./target/encrypted.bin", "-o", "./target/decrypted.xml");
    
    byte[] i = Files.readAllBytes(Paths.get("./pom.xml"));
    byte[] o = Files.readAllBytes(Paths.get("./target/decrypted.xml"));
    assertTrue(Arrays.equals(i, o));
  }
  
  @Test
  public void testAsymmetric() throws IOException {
    CryptoCLI.throwReturnCode = true;
    CryptoCLI.suppressOutput = true;
    
    CryptoCLI.main("keys", "-asym", "-pub", "./target/public.key", "-priv", "./target/private.key");
    
    CryptoCLI.main("encrypt", "-asym", "-pub", "./target/public.key", 
        "-i", ".gitignore",  //only 39 bytes; it is small enough to encrypt with RSA
        "-o", "./target/encrypted.bin");
    
    CryptoCLI.main("decrypt", "-asym", "-priv", "./target/private.key", 
        "-i", "./target/encrypted.bin", 
        "-o", "./target/decrypted.txt");
    
    byte[] i = Files.readAllBytes(Paths.get(".gitignore"));
    byte[] o = Files.readAllBytes(Paths.get("./target/decrypted.txt"));
    assertTrue(Arrays.equals(i, o));
  }
  
  @Test
  public void testHybrid() throws IOException {
    CryptoCLI.throwReturnCode = true;
    CryptoCLI.suppressOutput = true;
    
    CryptoCLI.main("keys", "-asym", "-pub", "./target/public.key", "-priv", "./target/private.key");
    
    CryptoCLI.main("encrypt", "-hyb", "-k", "./target/encrypted.key", "-pub", "./target/public.key",   
        "-iv", "73167796f05554da6b9d1a39ada99b1f",
        "-i", "./pom.xml", "-o", "./target/encrypted.bin");
    
    CryptoCLI.main("decrypt", "-hyb", "-k", "./target/encrypted.key", "-priv", "./target/private.key", 
        "-iv", "73167796f05554da6b9d1a39ada99b1f",
        "-i", "./target/encrypted.bin", "-o", "./target/decrypted.xml");
    
    byte[] i = Files.readAllBytes(Paths.get("./pom.xml"));
    byte[] o = Files.readAllBytes(Paths.get("./target/decrypted.xml"));
    assertTrue(Arrays.equals(i, o));
  }
  
  @Test
  public void testHybridMultifile() throws IOException {
    CryptoCLI.throwReturnCode = true;
    CryptoCLI.suppressOutput = true;
    
    CryptoCLI.main("keys", "-asym", "-pub", "./target/public.key", "-priv", "./target/private.key");
    CryptoCLI.main("keys", "-sym", "-k", "./target/secret.key");
    CryptoCLI.main("encrypt", "-sym", "-k", "./target/secret.key", 
        "-iv", "73167796f05554da6b9d1a39ada99b1f", 
        "-i", "./pom.xml", "-o", "./target/1.bin");
    CryptoCLI.main("encrypt", "-sym", "-k", "./target/secret.key", 
        "-iv", "73167796f05554da6b9d1a39ada99b1f", 
        "-i", "./README.md", "-o", "./target/2.bin");
    CryptoCLI.main("encrypt", "-asym", "-pub", "./target/public.key",
        "-i", "./target/secret.key", "-o", "./target/encrypted.key");
    
    //now test using hybrid decryption, to verify that it can be mirrored
    CryptoCLI.main("decrypt", "-hyb", "-k", "./target/encrypted.key", 
        "-priv", "./target/private.key", 
        "-iv", "73167796f05554da6b9d1a39ada99b1f",
        "-i", "./target/1.bin", "-o", "./target/1.xml");
    CryptoCLI.main("decrypt", "-hyb", "-k", "./target/encrypted.key", 
        "-priv", "./target/private.key", 
        "-iv", "73167796f05554da6b9d1a39ada99b1f",
        "-i", "./target/2.bin", "-o", "./target/2.md");
        
    byte[] i = Files.readAllBytes(Paths.get("./pom.xml"));
    byte[] o = Files.readAllBytes(Paths.get("./target/1.xml"));
    assertTrue(Arrays.equals(i, o));
    i = Files.readAllBytes(Paths.get("./README.md"));
    o = Files.readAllBytes(Paths.get("./target/2.md"));
    assertTrue(Arrays.equals(i, o));
  }
  

  @Test
  public void testMD5() throws IOException {
    CryptoCLI.throwReturnCode = true;
    CryptoCLI.suppressOutput = true;
    CryptoCLI.main("hash", "-md5", "-i", "./pom.xml");
    CryptoCLI.main("hash", "-md5", "-i", "./pom.xml", "-o", "./target/md5.bin");
    CryptoCLI.main("hash", "-md5", "-t", "Hello World!");
  }

  @Test
  public void testSHA1() throws IOException {
    CryptoCLI.throwReturnCode = true;
    CryptoCLI.suppressOutput = true;
    CryptoCLI.main("hash", "-sha1", "-i", "./pom.xml");
  }
  
  @Test
  public void testSHA256() throws IOException {
    CryptoCLI.throwReturnCode = true;
    CryptoCLI.suppressOutput = true;
    CryptoCLI.main("hash", "-sha256", "-i", "./pom.xml");
    CryptoCLI.main("hash", "-sha256", "--salt", "A1BF39D2FFE6123A90BCD9225F6B2A71", "-i", "./pom.xml");
    CryptoCLI.main("hash", "-sha256", "--salt", "A1BF39D2FFE6123A90BCD9225F6B2A71", "-i", "./pom.xml", "-o", "./target/sha256.bin");
  }
  
  @Test
  public void testSHA512() throws IOException {
    CryptoCLI.throwReturnCode = true;
    CryptoCLI.suppressOutput = true;
    CryptoCLI.main("hash", "-sha512", "-i", "./pom.xml");
  }

  @Test
  public void testBytes() {
    CryptoCLI.throwReturnCode = true;
    CryptoCLI.suppressOutput = true;
    CryptoCLI.main("bytes");
    CryptoCLI.main("bytes", "-n", "64");
  }
  
  @Test
  public void testText() {
    CryptoCLI.throwReturnCode = true;
    CryptoCLI.suppressOutput = true;
    CryptoCLI.main("bytes", "--text", "Hello World!");
  }
  
  @Test
  public void testUuid() {
    CryptoCLI.throwReturnCode = true;
    CryptoCLI.suppressOutput = true;
    CryptoCLI.main("uuid");
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
    CryptoCLI.main("-?");
    CryptoCLI.main("--help");
  }
  
}
