package io.cambium.crypto.service;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class KeyService {
  private static final int SECRET_KEY_GENERATION_ITERATIONS = 65536;
  public static final String SECRET_KEY_GENERATION_ALGORITHM = "PBKDF2WithHmacSHA256";
  public static final String SECRET_KEY_ALGORITHM = "AES";
  public static final String PUBLIC_KEY_ALGORITHM = "RSA";
  public static final int    PUBLIC_KEY_LENGTH = 2048;
  public static final int    SECRET_KEY_SIZE = 256;
  
  public static KeyPair generateKeyPair() {
    try {
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance(PUBLIC_KEY_ALGORITHM);
      keyGen.initialize(PUBLIC_KEY_LENGTH);
      return keyGen.generateKeyPair();
    } catch(NoSuchAlgorithmException impossible) {
      throw new IllegalStateException("Could not retrieve key pair generator", impossible);
    }
  }
  
  public static SecretKey generateSecretKey() {
    try {
      KeyGenerator keyGen = KeyGenerator.getInstance(SECRET_KEY_ALGORITHM);
      keyGen.init(SECRET_KEY_SIZE);
      return keyGen.generateKey();
    } catch(NoSuchAlgorithmException impossible) {
      throw new IllegalStateException("Could not retrieve key generator", impossible);
    }
  }
  
  public static SecretKey generateSecretKey(String password, String salt) {  
    if(null == password || password.trim().isEmpty()) throw new IllegalArgumentException("Empty password");
    if(null == salt || salt.isBlank()) throw new IllegalArgumentException("Empty salt");
    try {
      KeySpec spec = new PBEKeySpec(
          password.toCharArray(), 
          salt.getBytes(), 
          SECRET_KEY_GENERATION_ITERATIONS, 
          SECRET_KEY_SIZE);
      SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_GENERATION_ALGORITHM);
      SecretKey secret = factory.generateSecret(spec);
      return new SecretKeySpec(secret.getEncoded(), SECRET_KEY_ALGORITHM);
    } catch(NoSuchAlgorithmException | InvalidKeySpecException impossible) {
      throw new IllegalStateException("Could not retrieve key generator", impossible);
    }
  }
  
  public static PublicKey getPublicKey(byte[] publicKeyData) {
    try {
      KeyFactory keyFactory = KeyFactory.getInstance(PUBLIC_KEY_ALGORITHM);
      return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyData));
    } catch(NoSuchAlgorithmException impossible) {
      throw new IllegalStateException("Could not retrieve key factory", impossible);
    } catch(InvalidKeySpecException e) {
      throw new IllegalArgumentException("Invalid public key data", e);
    }
  }

  public static PrivateKey getPrivateKey(byte[] privateKeyData) {
    try {
      KeyFactory keyFactory = KeyFactory.getInstance(PUBLIC_KEY_ALGORITHM);
      return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyData));
    } catch(NoSuchAlgorithmException impossible) {
      throw new IllegalStateException("Could not retrieve key factory", impossible);
    } catch(InvalidKeySpecException e) {
      throw new IllegalArgumentException("Invalid private key data", e);
    }
  }
  
  public static SecretKey getSecretKey(byte[] secretKey) {
    if(null == secretKey || secretKey.length == 0) throw new IllegalArgumentException("Empty secret key");
    return new SecretKeySpec(secretKey, 0, secretKey.length, SECRET_KEY_ALGORITHM);
  }

  public static IvParameterSpec getIvParameterSpec(byte[] ivSpec) {
    if(null == ivSpec || ivSpec.length != 16) throw new IllegalArgumentException("IV parameter specification must be exactly 16 bytes");
    return new IvParameterSpec(ivSpec);
  }


}
