package io.cambium.crypto.service;

import java.io.InputStream;

public interface HashService {

  public Hash hash(InputStream is);
  
  public Hash hash(byte[] salt, InputStream is);
  
}
