package io.cambium.crypto.keys;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyPair;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;

public class KeyServiceTest {

  @Test
  public void testKeyPairGeneration() throws IOException {
    KeyPair pair = KeyService.generateKeyPair();
    Files.write(Paths.get("./target/public.key"), pair.getPublic().getEncoded(), StandardOpenOption.CREATE);
    Files.write(Paths.get("./target/private.key"), pair.getPrivate().getEncoded(), StandardOpenOption.CREATE);
  }
  
  @Test
  public void testPasswordBasedKeyGeneration() {
    SecretKey key = KeyService.generateSecretKey("iL0ve@vocado$", "dslj~khg6~43lk~ksdf~asdD");
    assertNotNull(key);
  }
  
}
