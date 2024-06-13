package io.cambium.crypto.service.impl;

public class Sha1HashService extends BaseHashService {
  private static final String HASHING_ALGORITHM = "SHA-1";
  
  public Sha1HashService() {
    super(HASHING_ALGORITHM);
  }

}
