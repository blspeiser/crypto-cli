package io.cambium.crypto.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import io.cambium.crypto.service.CryptoParameters;
import io.cambium.crypto.service.CryptoService;
import io.cambium.crypto.service.KeyService;

public class HybridCryptoService implements CryptoService {
  private CryptoService symmetricDelegate = new SymmetricCryptoService();
  private CryptoService asymmetricDelegate = new AsymmetricCryptoService();
  
  @Override
  public void encrypt(CryptoParameters params) {
    if(null == params.publicKey || params.publicKey.length == 0) {
      throw new IllegalArgumentException("Empty public key");
    }
    //First, generate a random key that we will eventually encrypt using the public key,
    // and perform symmetric encryption
    params.secretKey = KeyService.generateSecretKey().getEncoded();
    symmetricDelegate.encrypt(params);
    //Now encrypt the secret key using a public key:
    CryptoParameters keyParams = new CryptoParameters();
    keyParams.publicKey = params.publicKey;
    keyParams.input = new ByteArrayInputStream(params.secretKey);
    keyParams.output = new ByteArrayOutputStream();
    asymmetricDelegate.encrypt(keyParams);
    params.encryptedKey = ((ByteArrayOutputStream)keyParams.output).toByteArray();
  }

  @Override
  public void decrypt(CryptoParameters params) {
    if(null == params.privateKey || params.privateKey.length == 0) {
      throw new IllegalArgumentException("Empty public key");
    }
    if(null == params.encryptedKey || params.encryptedKey.length == 0) {
      throw new IllegalArgumentException("Empty encrypted key");
    }
    //First, decrypt the secret key using the private key:
    CryptoParameters keyParams = new CryptoParameters();
    keyParams.privateKey = params.privateKey;
    keyParams.input = new ByteArrayInputStream(params.encryptedKey);
    keyParams.output = new ByteArrayOutputStream();
    asymmetricDelegate.decrypt(keyParams);
    params.secretKey = ((ByteArrayOutputStream)keyParams.output).toByteArray();
    
    //Now decrypt the file using the decrypted secret key:
    symmetricDelegate.decrypt(params);
  }
  
}
