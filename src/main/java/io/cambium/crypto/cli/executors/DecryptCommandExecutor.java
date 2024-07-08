package io.cambium.crypto.cli.executors;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HexFormat;

import io.cambium.crypto.cli.commands.DecryptCommand;
import io.cambium.crypto.service.CryptoParameters;
import io.cambium.crypto.service.CryptoService;
import io.cambium.crypto.service.impl.AsymmetricCryptoService;
import io.cambium.crypto.service.impl.HybridCryptoService;
import io.cambium.crypto.service.impl.SymmetricCryptoService;

public class DecryptCommandExecutor extends BaseCommandExecutor<DecryptCommand> {

  public DecryptCommandExecutor(boolean quiet) {
    super(quiet);
  }

  public void execute(DecryptCommand arguments) throws IOException {
    CryptoParameters params = new CryptoParameters();
    params.input  = new BufferedInputStream(arguments.stdin
        ? System.in
        : new FileInputStream(arguments.input));
    params.output = new BufferedOutputStream(new FileOutputStream(arguments.output));
    if(!arguments.asymmetric) {
      params.initializationVector = HexFormat.of().withUpperCase().parseHex(
          arguments.initializationVector.toUpperCase());
    }
    CryptoService service = null;
    if(arguments.symmetric) {
      params.secretKey = Files.readAllBytes(arguments.key.toPath());
      if(params.secretKey.length == 0) throw new IllegalArgumentException("Secret key is empty");
      service = new SymmetricCryptoService();
    } else 
    if(arguments.asymmetric) {
      params.privateKey = Files.readAllBytes(arguments.privateKey.toPath());
      if(params.privateKey.length == 0) throw new IllegalArgumentException("Private key is empty");
      service = new AsymmetricCryptoService();
    } else
    if(arguments.hybrid) {
      params.encryptedKey = Files.readAllBytes(arguments.key.toPath());
      if(params.encryptedKey.length == 0) throw new IllegalArgumentException("Encrypted key is empty");
      params.privateKey = Files.readAllBytes(arguments.privateKey.toPath());
      if(params.privateKey.length == 0) throw new IllegalArgumentException("Private key is empty");
      service = new HybridCryptoService();
    }
    //if we got here, we have everything we need to do the decryption:
    service.decrypt(params);
  }
  
}
