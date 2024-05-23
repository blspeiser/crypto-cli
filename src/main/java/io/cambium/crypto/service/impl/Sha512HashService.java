package io.cambium.crypto.service.impl;

public class Sha512HashService extends BaseHashService {
  private static final String HASHING_ALGORITHM = "SHA3-512";
  
  public Sha512HashService() {
    super(HASHING_ALGORITHM);
  }
}
