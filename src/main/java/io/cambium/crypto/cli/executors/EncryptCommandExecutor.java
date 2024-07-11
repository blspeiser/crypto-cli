package io.cambium.crypto.cli.executors;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HexFormat;

import io.cambium.crypto.cli.commands.EncryptCommand;
import io.cambium.crypto.service.CryptoParameters;
import io.cambium.crypto.service.CryptoService;
import io.cambium.crypto.service.impl.AsymmetricCryptoService;
import io.cambium.crypto.service.impl.HybridCryptoService;
import io.cambium.crypto.service.impl.SymmetricCryptoService;

public class EncryptCommandExecutor extends BaseCommandExecutor<EncryptCommand> {
  
  public EncryptCommandExecutor(boolean quiet) {
    super(quiet);
  }

  public void execute(EncryptCommand arguments) throws IOException {
    CryptoParameters params = new CryptoParameters();
    params.input  = new BufferedInputStream(arguments.stdin
        ? System.in
        : new FileInputStream(arguments.input));
    params.output = new BufferedOutputStream(new FileOutputStream(arguments.output));
    if(!arguments.asymmetric) {
      params.initializationVector = HexFormat.of().parseHex(
          arguments.initializationVector.toLowerCase());
    }
    CryptoService service = null;
    if(arguments.symmetric) {
      params.secretKey = Files.readAllBytes(arguments.key.toPath());
      if(params.secretKey.length == 0) throw new IllegalArgumentException("Secret key is empty");
      service = new SymmetricCryptoService();
    } else
    if(arguments.asymmetric) {
      params.publicKey = Files.readAllBytes(arguments.publicKey.toPath());
      if(params.publicKey.length == 0) throw new IllegalArgumentException("Public key is empty");
      service = new AsymmetricCryptoService();
    } else      
    if(arguments.hybrid) {
      params.publicKey = Files.readAllBytes(arguments.publicKey.toPath());
      if(params.publicKey.length == 0) throw new IllegalArgumentException("Public key is empty");
      service = new HybridCryptoService();
    } 
    //if we got here, we have everything we need to do the encryption:
    service.encrypt(params);
    if(arguments.hybrid) {
      //also need to write out the generated encrypted secret key
      Files.write(arguments.key.toPath(), params.encryptedKey, WRITE_OPTIONS);
    }
  }
  
}
