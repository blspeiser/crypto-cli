package io.cambium.crypto.service.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import io.cambium.crypto.keys.KeyService;
import io.cambium.crypto.service.CryptoParameters;
import io.cambium.crypto.service.CryptoService;

public class SymmetricCryptoService implements CryptoService {
  private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
  private static final int BUFFER_SIZE = 4096;

  @Override
  public void encrypt(CryptoParameters params) {
    validate(params);
    Cipher cipher = getCipher(params, Cipher.ENCRYPT_MODE);
    CipherOutputStream cos = new CipherOutputStream(params.output, cipher);
    try {
      byte[] buffer = new byte[BUFFER_SIZE];
      int read = -1;
      while((read = params.input.read(buffer)) != -1) {
        cos.write(buffer, 0, read);
      }
      cos.close();
      params.input.close();
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public void decrypt(CryptoParameters params) {
    validate(params);
    Cipher cipher = getCipher(params, Cipher.DECRYPT_MODE);
    CipherInputStream cis = new CipherInputStream(params.input, cipher);
    try {
      byte[] buffer = new byte[BUFFER_SIZE];
      int read = -1;
      while((read = cis.read(buffer)) != -1) {
        params.output.write(buffer, 0, read);
      }
      params.output.close();
      cis.close();
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    }
  }
 
  private void validate(CryptoParameters params) {
    if(null == params.secretKey || params.secretKey.length == 0) {
      throw new IllegalArgumentException("Empty secret key");
    }
    if(null == params.initializationVector || params.initializationVector.length == 0) throw new IllegalArgumentException("Empty IV parameter spec");
    if(null == params.input) throw new IllegalArgumentException("No input content");
    if(null == params.output) throw new IllegalArgumentException("No output destination");
  }

  private Cipher getCipher(CryptoParameters params, int mode) {
    try {
      SecretKey secretKey = KeyService.getSecretKey(params.secretKey);
      IvParameterSpec ivSpec = KeyService.getIvParameterSpec(params.initializationVector);
      Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
      cipher.init(mode, secretKey, ivSpec);
      return cipher;
    } catch(InvalidKeyException e) {
      throw new IllegalArgumentException("Invalid key", e);
    } catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException impossible) {
      throw new IllegalStateException("Failed to retrieve cipher", impossible);
    }
  }
 
}
