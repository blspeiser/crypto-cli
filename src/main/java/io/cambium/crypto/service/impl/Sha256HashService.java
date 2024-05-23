package io.cambium.crypto.service.impl;

public class Sha256HashService extends BaseHashService {
  private static final String HASHING_ALGORITHM = "SHA-256";

  public Sha256HashService() {
    super(HASHING_ALGORITHM);
  }

}
