package io.cambium.crypto.service;

import java.io.InputStream;

public interface HashService {
  
  /**
   * Hash a given input. 
   * @param salt [Optional - pass null to not use a salt]
   * @param is
   * @return hash, in bytes
   */
  public byte[] hash(byte[] salt, InputStream is);
  
}
