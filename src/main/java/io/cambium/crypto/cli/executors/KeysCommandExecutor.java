package io.cambium.crypto.cli.executors;

import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

import io.cambium.crypto.cli.commands.KeysCommand;
import io.cambium.crypto.keys.KeyService;

public class KeysCommandExecutor extends BaseCommandExecutor<KeysCommand> {
  
  public KeysCommandExecutor(boolean quiet) {
    super(quiet);
  }

  public void execute(KeysCommand arguments) throws NoSuchAlgorithmException, IOException {
    if(arguments.asymmetric) {
      KeyPair pair = KeyService.generateKeyPair();
      Files.write(arguments.publicKey.toPath(),  pair.getPublic().getEncoded(),  WRITE_OPTIONS);
      Files.write(arguments.privateKey.toPath(), pair.getPrivate().getEncoded(), WRITE_OPTIONS);
    } else
    if(arguments.symmetric) {
      byte[] key;
      if(arguments.password == null || arguments.password.isBlank()) { 
        key = KeyService.generateSecretKey().getEncoded();
      } else {
        String salt = arguments.salt;
        if(salt == null || salt.isBlank()) {
          byte[] temp = new byte[256];
          SecureRandom.getInstanceStrong().nextBytes(temp);
          salt = HexFormat.of().formatHex(temp);
        } 
        key = KeyService.generateSecretKey(arguments.password, salt).getEncoded();
      }
      Files.write(arguments.key.toPath(), key, WRITE_OPTIONS);
    }
  }
  
}
