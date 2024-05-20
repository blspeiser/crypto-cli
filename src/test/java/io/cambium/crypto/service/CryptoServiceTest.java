package io.cambium.crypto.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import io.cambium.crypto.keys.KeyService;
import io.cambium.crypto.service.impl.AsymmetricCryptoService;
import io.cambium.crypto.service.impl.SymmetricCryptoService;

public class CryptoServiceTest {
  
  @Test
  public void testSymmetric() throws NoSuchAlgorithmException {
    byte[] input = "The quick brown fox jumped over the lazy dog.".getBytes(StandardCharsets.UTF_8);
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    
    CryptoParameters params = new CryptoParameters();
    params.secretKey = KeyService.generateSecretKey().getEncoded();
    params.initializationVector = new byte[16];
    params.input = new ByteArrayInputStream(input);
    params.output = bout;
    SecureRandom.getInstanceStrong().nextBytes(params.initializationVector);
    SymmetricCryptoService service = new SymmetricCryptoService();
    
    
    service.encrypt(params);
    assertFalse(Arrays.equals(input, bout.toByteArray()));
    
    params.input = new ByteArrayInputStream(bout.toByteArray());
    bout = new ByteArrayOutputStream();
    params.output = bout;
    
    service.decrypt(params);
    assertTrue(Arrays.equals(input, bout.toByteArray()));
  }

  @Test
  public void testAsymmetric() throws NoSuchAlgorithmException {
    byte[] input = "The quick brown fox jumped over the lazy dog.".getBytes(StandardCharsets.UTF_8);
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    
    KeyPair pair = KeyService.generateKeyPair();
    CryptoParameters params = new CryptoParameters();
    params.publicKey = pair.getPublic().getEncoded();
    params.privateKey = pair.getPrivate().getEncoded();
    params.initializationVector = new byte[16];
    params.input = new ByteArrayInputStream(input);
    params.output = bout;
    SecureRandom.getInstanceStrong().nextBytes(params.initializationVector);
    AsymmetricCryptoService service = new AsymmetricCryptoService();
    
    service.encrypt(params);
    assertFalse(Arrays.equals(input, bout.toByteArray()));
    assertNotNull(params.secretKey);
    assertNotNull(params.encryptedKey);
    assertNotEquals(0, params.secretKey.length);
    assertNotEquals(0, params.encryptedKey.length);
    
    params.input = new ByteArrayInputStream(bout.toByteArray());
    bout = new ByteArrayOutputStream();
    params.output = bout;
    
    service.decrypt(params);
    assertTrue(Arrays.equals(input, bout.toByteArray()));
  }
  
}
