package io.cambium.crypto.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.cambium.crypto.service.Hash;
import io.cambium.crypto.service.HashService;

public class Md5HashService implements HashService {
  private static final String HASHING_ALGORITHM = "MD5";
  private static final int BUFFER_SIZE = 4096;
  
  @Override
  public Hash hash(InputStream is) {
    try {
      MessageDigest md5 = MessageDigest.getInstance(HASHING_ALGORITHM);
      int read = 0;
      byte[] buffer = new byte[BUFFER_SIZE];
      while((read = is.read(buffer)) != -1){
        md5.update(buffer, 0, read);
      }
      is.close();
      Hash result = new Hash();
      result.hash = md5.digest(); 
      return result;
    } catch (NoSuchAlgorithmException impossible) {
      throw new IllegalStateException("Failed to retrieve digest", impossible);
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public Hash hash(byte[] salt, InputStream is) {
    return hash(is); //ignore the salt
  }

}
