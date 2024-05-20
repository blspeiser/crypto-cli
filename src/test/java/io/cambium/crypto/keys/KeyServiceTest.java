package io.cambium.crypto.keys;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyPair;

import org.junit.jupiter.api.Test;

public class KeyServiceTest {

  @Test
  public void test() throws IOException {
    KeyPair pair = KeyService.generateKeyPair();
    Files.write(Paths.get("./target/public.key"), pair.getPublic().getEncoded(), StandardOpenOption.CREATE);
    Files.write(Paths.get("./target/private.key"), pair.getPrivate().getEncoded(), StandardOpenOption.CREATE);
  }
  
}
