package io.cambium.crypto.service.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.cambium.crypto.service.CryptoParameters;
import io.cambium.crypto.service.CryptoService;
import io.cambium.crypto.service.KeyService;

public class AsymmetricCryptoService implements CryptoService {
  private static final String CIPHER_ALGORITHM = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
  private static final int RSA_FILE_SIZE_LIMIT = 256;
  
  @Override
  public void encrypt(CryptoParameters params) {
    if(null == params.publicKey || params.publicKey.length == 0) {
      throw new IllegalArgumentException("Empty public key");
    }
    if(null == params.input) throw new IllegalArgumentException("No input content");
    if(null == params.output) throw new IllegalArgumentException("No output destination");
    try {
      byte[] bytes = params.input.readAllBytes();
      if(bytes.length > RSA_FILE_SIZE_LIMIT) {
        throw new IllegalArgumentException("RSA encryption is limited to files of " 
            + RSA_FILE_SIZE_LIMIT + " bytes or less; it is only suitable for encrypting "
            + "small files. If you want public key encrytpion for larger files, consider "
            + "using hybrid encryption instead.");
      }
      PublicKey key = KeyService.getPublicKey(params.publicKey);
      Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      byte[] encrypted = cipher.doFinal(bytes);
      params.output.write(encrypted);
      params.output.flush();
      params.output.close();
      params.input.close();
    } catch(NoSuchAlgorithmException | NoSuchPaddingException impossible) {
      throw new IllegalStateException("Failed to retrieve cipher", impossible);
    } catch(InvalidKeyException e) {
      throw new IllegalStateException("Invalid key", e);
    } catch(IllegalBlockSizeException | BadPaddingException e) {
      throw new IllegalArgumentException("Invalid input", e);
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    }  
  }

  @Override
  public void decrypt(CryptoParameters params) {
    if(null == params.privateKey || params.privateKey.length == 0) {
      throw new IllegalArgumentException("Empty public key");
    }
    if(null == params.input) throw new IllegalArgumentException("No input content");
    if(null == params.output) throw new IllegalArgumentException("No output destination");
    try {
      PrivateKey key = KeyService.getPrivateKey(params.privateKey);
      byte[] encrypted = params.input.readAllBytes(); 
      Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, key);
      byte[] decrypted = cipher.doFinal(encrypted);
      params.output.write(decrypted);
      params.output.flush();
      params.output.close();
      params.input.close();
    } catch(NoSuchAlgorithmException | NoSuchPaddingException impossible) {
      throw new IllegalStateException("Failed to retrieve cipher", impossible);
    } catch(InvalidKeyException e) {
      throw new IllegalStateException("Invalid key", e);
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(IllegalBlockSizeException | BadPaddingException e) {
      throw new IllegalArgumentException("Invalid input", e);
    } 
  }
  
}
