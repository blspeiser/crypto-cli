package io.cambium.crypto.cli;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.UUID;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Strings;

import io.cambium.crypto.keys.KeyService;
import io.cambium.crypto.service.CryptoParameters;
import io.cambium.crypto.service.CryptoService;
import io.cambium.crypto.service.HashService;
import io.cambium.crypto.service.impl.AsymmetricCryptoService;
import io.cambium.crypto.service.impl.Md5HashService;
import io.cambium.crypto.service.impl.Sha1HashService;
import io.cambium.crypto.service.impl.Sha256HashService;
import io.cambium.crypto.service.impl.Sha512HashService;
import io.cambium.crypto.service.impl.SymmetricCryptoService;

public class CryptoCLI {
  //These two flags are to make it easier to properly run tests. 
  public static boolean throwReturnCode = false;
  public static boolean suppressOutput = false;

  public static void main(String... args) {
    int returnCode = 0; 
    try {  
      Arguments arguments = new Arguments();
      KeysCommand keysCommand = new KeysCommand();
      HashCommand hashCommand = new HashCommand();
      UuidCommand uuidCommand = new UuidCommand();
      BytesCommand bytesCommand = new BytesCommand();
      EncryptCommand encryptCommand = new EncryptCommand();
      DecryptCommand decryptCommand = new DecryptCommand();
      
      JCommander parser = JCommander.newBuilder()
          .addObject(arguments)
          .addCommand(keysCommand)
          .addCommand(hashCommand)
          .addCommand(uuidCommand)
          .addCommand(bytesCommand)
          .addCommand(encryptCommand)
          .addCommand(decryptCommand)
          .build();
      parser.setProgramName("crypto-cli");
      if(null != args) parser.parse(args);
      String command = parser.getParsedCommand();
      if(KeysCommand.NAME.equals(command)) {
        generateKeys(parser, keysCommand);
      } else
      if(HashCommand.NAME.equals(command)) {
        generateHash(parser, hashCommand);
      } else
      if(UuidCommand.NAME.equals(command)) {
        generateUuid(parser, uuidCommand);
      } else
      if(BytesCommand.NAME.equals(command)) {
        generateBytes(parser, bytesCommand);
      } else
      if(EncryptCommand.NAME.equals(command)) {
        encrypt(parser, encryptCommand);
      } else
      if(DecryptCommand.NAME.equals(command)) {
        decrypt(parser, decryptCommand);
      } 
      else {
        if(!suppressOutput) parser.usage(); //specifically optionally suppress this output only, just for tests
        returnCode = arguments.help ? 0 : 1; //if the user specifically asked for help, then this case is not an error
      }
    } catch(ParameterException e) {
      System.err.println(e.getMessage());
      e.getJCommander().usage();
      returnCode = 1;
    } catch(IllegalArgumentException | UnsupportedOperationException e) {
      System.err.println(e.getMessage());
      returnCode = 1;
    } catch(Exception e) {
      e.printStackTrace();
      returnCode = -1;
    }
    if(throwReturnCode) {
      if(returnCode != 0) throw new RuntimeException(Integer.toString(returnCode));
    } else {
      System.exit(returnCode);
    }
  }

  private static void generateBytes(JCommander parser, BytesCommand arguments) throws NoSuchAlgorithmException {
    if(Strings.isStringEmpty(arguments.text)) {
      if(arguments.numberOfBytes == null) arguments.numberOfBytes = 16;
      if(arguments.numberOfBytes < 1) error(parser, "Invalid number of bytes");
      byte[] bytes = new byte[arguments.numberOfBytes];
      SecureRandom.getInstanceStrong().nextBytes(bytes);
      if(!suppressOutput) {
        System.out.println(HexFormat.of().formatHex(bytes));
      }
    } else {
      byte[] bytes = arguments.text.getBytes();
      if(!suppressOutput) {
        System.out.println(HexFormat.of().formatHex(bytes));
      }
    }
  }

  private static void generateUuid(JCommander parser, UuidCommand arguments) throws IOException {
    UUID uuid = UUID.randomUUID();
    if(!suppressOutput) {
      System.out.println(uuid.toString());
    }
  }
  
  private static void generateHash(JCommander parser, HashCommand arguments) throws IOException {
    InputStream is = null;
    if(arguments.input != null) {
      is = new BufferedInputStream(new FileInputStream(arguments.input));
    } else 
    if(!Strings.isStringEmpty(arguments.text)) {
      is = new ByteArrayInputStream(arguments.text.getBytes());
    } else 
    if(arguments.stdin) {
      is = new BufferedInputStream(System.in);
    }
    if(null == is) error(parser, "Must specify the input");
    byte[] salt = Strings.isStringEmpty(arguments.salt)
        ? null
        : arguments.salt.getBytes(StandardCharsets.UTF_8);
    HashService service = null;
    if(arguments.md5) { 
      service = new Md5HashService();
    } else
    if(arguments.sha1) { 
      service = new Sha1HashService();
    } else
    if(arguments.sha256) {
      service = new Sha256HashService();
    } else
    if(arguments.sha512) {
      service = new Sha512HashService();
    } 
    else {
      error(parser, "Must specify type of hash");  //no fall-through, this throws an exception
    }
    byte[] hash = service.hash(salt, is);
    if(arguments.output == null) {
      //The default encoding for Unix-based md5sum and sha256sum is hex output, so use that:
      String hex = HexFormat.of().formatHex(hash);
      if(!suppressOutput) {
        System.out.println(hex);
      }
    } else {
      Files.write(arguments.output.toPath(), hash, StandardOpenOption.CREATE);
    }
  }

  private static void generateKeys(JCommander parser, KeysCommand arguments) 
  throws NoSuchAlgorithmException, IOException {
    if(arguments.asymmetric) {
      if(arguments.publicKey == null) {
        error(parser, "Must specify the output file for the public key");
      }
      if(arguments.privateKey == null) {
        error(parser, "Must specify the output file for the private key");
      }
      KeyPair pair = KeyService.generateKeyPair();
      Files.write(arguments.publicKey.toPath(),  pair.getPublic().getEncoded(),  StandardOpenOption.CREATE);
      Files.write(arguments.privateKey.toPath(), pair.getPrivate().getEncoded(), StandardOpenOption.CREATE);
    }else
    if(arguments.symmetric) {
      if(arguments.key == null) {
        error(parser, "Must specify output file for the secret key");
      }
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
      Files.write(arguments.key.toPath(), key, StandardOpenOption.CREATE);
    } 
    else { 
      error(parser, "Must specify symmetric or asymmetric key generation");
    }
  }
  
  private static void encrypt(JCommander parser, EncryptCommand arguments) throws IOException {
    if(arguments.input  == null && !arguments.stdin) error(parser, "Must specify the input");
    if(arguments.output == null) error(parser, "Must specify the output");
    if(arguments.key    == null) error(parser, "Must specify the secret key");
    if(arguments.initializationVector == null || arguments.initializationVector.isBlank()) {
      error(parser, "Must specify the initialization vector");
    }
    CryptoParameters params = new CryptoParameters();
    params.input  = new BufferedInputStream(arguments.stdin
        ? System.in
        : new FileInputStream(arguments.input));
    params.output = new BufferedOutputStream(new FileOutputStream(arguments.output));
    params.initializationVector = HexFormat.of().parseHex(
        arguments.initializationVector.toLowerCase());
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
    } 
    else { 
      error(parser, "Must specify symmetric or asymmetric encryption");
    }
    //if we got here, we have everything we need to do the encryption:
    service.encrypt(params);
    if(arguments.asymmetric) {
      //also need to write out the generated encrypted secret key
      Files.write(arguments.key.toPath(), params.encryptedKey, StandardOpenOption.CREATE);
    }
  }

  private static void decrypt(JCommander parser, DecryptCommand arguments) throws IOException {
    if(arguments.input  == null && !arguments.stdin) error(parser, "Must specify the input");
    if(arguments.output == null) error(parser, "Must specify the output");
    if(arguments.key    == null) error(parser, "Must specify the secret key");
    if(arguments.initializationVector == null || arguments.initializationVector.isBlank()) {
      error(parser, "Must specify the initialization vector");
    }
    CryptoParameters params = new CryptoParameters();
    params.input  = new BufferedInputStream(arguments.stdin
        ? System.in
        : new FileInputStream(arguments.input));
    params.output = new BufferedOutputStream(new FileOutputStream(arguments.output));
    params.initializationVector = HexFormat.of().withUpperCase().parseHex(
        arguments.initializationVector.toUpperCase());
    CryptoService service = null;
    if(arguments.symmetric) {
      params.secretKey = Files.readAllBytes(arguments.key.toPath());
      if(params.secretKey.length == 0) throw new IllegalArgumentException("Secret key is empty");
      service = new SymmetricCryptoService();
    } else 
    if(arguments.asymmetric) {
      params.encryptedKey = Files.readAllBytes(arguments.key.toPath());
      if(params.encryptedKey.length == 0) throw new IllegalArgumentException("Encrypted key is empty");
      params.privateKey = Files.readAllBytes(arguments.privateKey.toPath());
      if(params.privateKey.length == 0) throw new IllegalArgumentException("Private key is empty");
      service = new AsymmetricCryptoService();
    }
    else { 
      error(parser, "Must specify symmetric or asymmetric decryption");
    }
    //if we got here, we have everything we need to do the decryption:
    service.decrypt(params);
  }

  private static void error(JCommander parser, String error) throws ParameterException {
    ParameterException ex = new ParameterException(error);
    ex.setJCommander(parser);
    throw ex;
  }

}
