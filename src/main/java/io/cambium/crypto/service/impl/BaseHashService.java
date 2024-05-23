package io.cambium.crypto.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.cambium.crypto.service.HashService;

public class BaseHashService implements HashService {
  private static final int BUFFER_SIZE = 4096;
  private final String algorithm;

  public BaseHashService(String algorithm) {
    this.algorithm = algorithm;
  }

  @Override
  public byte[] hash(byte[] salt, InputStream is) {
    try {
      MessageDigest digest = MessageDigest.getInstance(this.algorithm);
      if(null != salt && salt.length > 0) {
        digest.update(salt);
      }
      int read = 0;
      byte[] buffer = new byte[BUFFER_SIZE];
      while((read = is.read(buffer)) != -1){
        digest.update(buffer, 0, read);
      }
      is.close();
      byte[] hash = digest.digest(); 
      return hash;
    } catch (NoSuchAlgorithmException impossible) {
      throw new IllegalStateException("Failed to retrieve digest", impossible);
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}