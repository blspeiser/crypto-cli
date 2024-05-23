package io.cambium.crypto.service.impl;

import io.cambium.crypto.service.HashService;

public class Md5HashService extends BaseHashService implements HashService {
  static final String HASHING_ALGORITHM = "MD5";

  public Md5HashService() {
    super(HASHING_ALGORITHM);
  }

}
