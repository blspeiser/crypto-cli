package io.cambium.crypto.service;

import java.io.InputStream;

public interface HashService {
  
  public byte[] hash(byte[] salt, InputStream is);
  
}
