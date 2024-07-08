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
    exec("keys",  
        "-sym", "-k", "./target/secret.key");
    
    exec("encrypt",  
        "-sym", "-k", "./target/secret.key", 
        "-iv", "c6069ad81f8a6b8b281cb5627288c1e2", 
        "-i", "./pom.xml", "-o", "./target/encrypted.bin");
    
    exec("decrypt",  
        "-sym", "-k", "./target/secret.key", 
        "-iv", "c6069ad81f8a6b8b281cb5627288c1e2", 
        "-i", "./target/encrypted.bin", "-o", "./target/decrypted.xml");
    
    byte[] i = Files.readAllBytes(Paths.get("./pom.xml"));
    byte[] o = Files.readAllBytes(Paths.get("./target/decrypted.xml"));
    assertTrue(Arrays.equals(i, o));
  }
  
  @Test
  public void testAsymmetric() throws IOException {
    exec("keys",  
        "-asym", "-pub", "./target/public.key", "-priv", "./target/private.key");
    
    exec("encrypt",  
        "-asym", "-pub", "./target/public.key", 
        "-i", ".gitignore",  //only 39 bytes; it is small enough to encrypt with RSA
        "-o", "./target/encrypted.bin");
    
    exec("decrypt",  
        "-asym", "-priv", "./target/private.key", 
        "-i", "./target/encrypted.bin", 
        "-o", "./target/decrypted.txt");
    
    byte[] i = Files.readAllBytes(Paths.get(".gitignore"));
    byte[] o = Files.readAllBytes(Paths.get("./target/decrypted.txt"));
    assertTrue(Arrays.equals(i, o));
  }
  
  @Test
  public void testHybrid() throws IOException {
    exec("keys",  
        "-asym", "-pub", "./target/public.key", "-priv", "./target/private.key");
    
    exec("encrypt", 
        "-hyb", "-k", "./target/encrypted.key", "-pub", "./target/public.key",   
        "-iv", "73167796f05554da6b9d1a39ada99b1f",
        "-i", "./pom.xml", "-o", "./target/encrypted.bin");
    
    exec("decrypt", 
        "-hyb", "-k", "./target/encrypted.key", "-priv", "./target/private.key", 
        "-iv", "73167796f05554da6b9d1a39ada99b1f",
        "-i", "./target/encrypted.bin", "-o", "./target/decrypted.xml");
    
    byte[] i = Files.readAllBytes(Paths.get("./pom.xml"));
    byte[] o = Files.readAllBytes(Paths.get("./target/decrypted.xml"));
    assertTrue(Arrays.equals(i, o));
  }
  
  @Test
  public void testHybridMultifile() throws IOException {
    exec("keys", 
        "-asym", "-pub", "./target/public.key", "-priv", "./target/private.key");
    exec("keys", 
        "-sym", "-k", "./target/secret.key");
    exec("encrypt",
        "-sym", "-k", "./target/secret.key", 
        "-iv", "73167796f05554da6b9d1a39ada99b1f", 
        "-i", "./pom.xml", "-o", "./target/1.bin");
    exec("encrypt", 
        "-sym", "-k", "./target/secret.key", 
        "-iv", "73167796f05554da6b9d1a39ada99b1f", 
        "-i", "./README.md", "-o", "./target/2.bin");
    exec("encrypt",  
        "-asym", "-pub", "./target/public.key",
        "-i", "./target/secret.key", "-o", "./target/encrypted.key");
    
    //now test using hybrid decryption, to verify that it can be mirrored
    exec("decrypt",  
        "-hyb", "-k", "./target/encrypted.key", 
        "-priv", "./target/private.key", 
        "-iv", "73167796f05554da6b9d1a39ada99b1f",
        "-i", "./target/1.bin", "-o", "./target/1.xml");
    exec("decrypt", 
        "-hyb", "-k", "./target/encrypted.key", 
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
    exec("hash", "-md5", "-t", "Hello World!");
    exec("hash", "-md5", "-i", "./pom.xml");
    exec("hash", "-md5", "-i", "./pom.xml", "-o", "./target/md5.bin");
  }

  @Test
  public void testSHA1() throws IOException {
    exec("hash", "-sha1", "-i", "./pom.xml");
  }
  
  @Test
  public void testSHA256() throws IOException {
    exec("hash", "-sha256", "-i", "./pom.xml");
    exec("hash", "-sha256", "--salt", "A1BF39D2FFE6123A90BCD9225F6B2A71", "-i", "./pom.xml");
    exec("hash", "-sha256", "--salt", "A1BF39D2FFE6123A90BCD9225F6B2A71", "-i", "./pom.xml", "-o", "./target/sha256.bin");
  }
  
  @Test
  public void testSHA512() throws IOException {
    exec("hash", "-sha512", "-i", "./pom.xml");
  }

  @Test
  public void testBytes() {
    exec("bytes");
    exec("bytes", "-n", "64");
  }
  
  @Test
  public void testText() {
    exec("bytes", "--text", "Hello World!");
  }
  
  @Test
  public void testUuid() {
    exec("uuid");
  }
  
  @Test
  public void testHelp() throws IOException {
    try {
      exec();
      fail("did not throw return code");
    } catch(Exception e) {
      //expected
    }
    exec("-?");
    exec("--help");
  }
  
  private static void exec(String... args) {
    String[] extended = new String[args.length + 2];
    extended[0] = "-q";
    extended[1] = "-x";
    System.arraycopy(args, 0, extended, 2, args.length);
    CryptoCLI.main(extended);
  }
  
}
