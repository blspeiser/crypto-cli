package io.cambium.crypto.service;

public interface CryptoService {
  
  public void encrypt(CryptoParameters params);
  public void decrypt(CryptoParameters params);
  
}
