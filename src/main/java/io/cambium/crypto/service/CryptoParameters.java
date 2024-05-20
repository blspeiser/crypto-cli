package io.cambium.crypto.service;

import java.io.InputStream;
import java.io.OutputStream;

public class CryptoParameters {
  public byte[] secretKey = null;
  public byte[] publicKey = null;
  public byte[] privateKey = null;
  public byte[] encryptedKey = null;
  public byte[] initializationVector = null;
  public InputStream input = null;
  public OutputStream output = null;
}
