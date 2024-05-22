package io.cambium.crypto.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.cambium.crypto.service.Hash;
import io.cambium.crypto.service.HashService;

public class Sha256HashService implements HashService {
  private static final String HASHING_ALGORITHM = "SHA-256";
  private static final int BUFFER_SIZE = 4096;

  @Override
  public Hash hash(InputStream is) {
    return hash(null, is); //assume no salt
  }

  @Override
  public Hash hash(byte[] salt, InputStream is) {
    try {
      MessageDigest sha256 = MessageDigest.getInstance(HASHING_ALGORITHM);
      if(null != salt && salt.length > 0) {
        sha256.update(salt);
      }
      int read = 0;
      byte[] buffer = new byte[BUFFER_SIZE];
      while((read = is.read(buffer)) != -1){
        sha256.update(buffer, 0, read);
      }
      is.close();
      Hash result = new Hash();
      result.salt = salt;
      result.hash = sha256.digest(); 
      return result;
    } catch (NoSuchAlgorithmException impossible) {
      throw new IllegalStateException("Failed to retrieve digest", impossible);
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}
