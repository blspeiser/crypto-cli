package io.cambium.crypto.service.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.cambium.crypto.keys.KeyService;
import io.cambium.crypto.service.CryptoParameters;
import io.cambium.crypto.service.CryptoService;

public class AsymmetricCryptoService implements CryptoService {
  private static final String CIPHER_ALGORITHM = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
  private SymmetricCryptoService delegate = new SymmetricCryptoService();
  
  @Override
  public void encrypt(CryptoParameters params) {
    if(null == params.publicKey || params.publicKey.length == 0) {
      throw new IllegalArgumentException("Empty public key");
    }
    //First, generate a random key that we will eventually encrypt using the public key,
    // and perform symmetric encryption
    params.secretKey = KeyService.generateSecretKey().getEncoded();
    delegate.encrypt(params);
    //Now encrypt the secret key using a public key: 
    try {
      PublicKey key = KeyService.getPublicKey(params.publicKey);
      Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      params.encryptedKey = cipher.doFinal(params.secretKey);
    } catch(NoSuchAlgorithmException | NoSuchPaddingException impossible) {
      throw new IllegalStateException("Failed to retrieve cipher", impossible);
    } catch(InvalidKeyException e) {
      throw new IllegalStateException("Invalid key", e);
    } catch(IllegalBlockSizeException | BadPaddingException e) {
      throw new IllegalArgumentException("Invalid input", e);
    }  
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
    try {
      PrivateKey key = KeyService.getPrivateKey(params.privateKey);
      Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, key);
      params.secretKey = cipher.doFinal(params.encryptedKey);
    } catch(NoSuchAlgorithmException | NoSuchPaddingException impossible) {
      throw new IllegalStateException("Failed to retrieve cipher", impossible);
    } catch(InvalidKeyException e) {
      throw new IllegalStateException("Invalid key", e);
    } catch(IllegalBlockSizeException | BadPaddingException e) {
      throw new IllegalArgumentException("Invalid input", e);
    }
    //Now decrypt the file using the decrypted secret key:
    delegate.decrypt(params);
  }
  
}
