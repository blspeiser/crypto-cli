package io.cambium.crypto.keys;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyPair;
import java.util.Arrays;

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
    String password = "iL0ve@vocado$";
    String salt = "dslj~khg6~43lk~ksdf~asdD";
    //verify it is deterministic!
    SecretKey key1 = KeyService.generateSecretKey(password, salt);
    SecretKey key2 = KeyService.generateSecretKey(password, salt);
    assertNotNull(key1);
    assertNotNull(key2);
    assertTrue(Arrays.equals(key1.getEncoded(), key2.getEncoded()));
  }
  
}
