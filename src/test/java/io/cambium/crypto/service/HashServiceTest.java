package io.cambium.crypto.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.Test;

import io.cambium.crypto.service.impl.Md5HashService;
import io.cambium.crypto.service.impl.Sha256HashService;

class HashServiceTest {

  @Test
  void testMD5() {
    byte[] input = "The quick brown fox jumped over the lazy dog.".getBytes(StandardCharsets.UTF_8);
    HashService service = new Md5HashService();
    Hash result = service.hash(new ByteArrayInputStream(input));
    assertNotNull(result);
    assertNotNull(result.hash);
    assertNull(result.salt);
    assertTrue(result.hash.length > 0);
    assertEquals("XG/73UDZVWtzoh5jw+DpBA==", Base64.getEncoder().encodeToString(result.hash));
  }
  
  @Test
  void testSHA256() {
    byte[] input = "The quick brown fox jumped over the lazy dog.".getBytes(StandardCharsets.UTF_8);
    byte[] salt = "rocksalt".getBytes(StandardCharsets.UTF_8);
    
    HashService service = new Sha256HashService();
    
    Hash result = service.hash(salt, new ByteArrayInputStream(input));
    assertNotNull(result);
    assertNotNull(result.hash);
    assertNotNull(result.salt);
    assertTrue(result.salt.length > 0);
    assertTrue(result.hash.length > 0);
    assertEquals("cm9ja3NhbHQ=", Base64.getEncoder().encodeToString(salt));
    assertEquals("cm9ja3NhbHQ=", Base64.getEncoder().encodeToString(result.salt));
    assertEquals("SaEwFWScCLpiYi+1ubmXadQfdvXksmBkX6O+KJ0BEao=", Base64.getEncoder().encodeToString(result.hash));
    
    //verify that changing the salt changes the hash
    salt = "sodium chloride".getBytes(StandardCharsets.UTF_8);
    result = service.hash(salt, new ByteArrayInputStream(input));
    assertNotNull(result);
    assertNotNull(result.hash);
    assertNotNull(result.salt);
    assertTrue(result.salt.length > 0);
    assertTrue(result.hash.length > 0);
    assertEquals("c29kaXVtIGNobG9yaWRl", Base64.getEncoder().encodeToString(salt));
    assertEquals("c29kaXVtIGNobG9yaWRl", Base64.getEncoder().encodeToString(result.salt));
    assertEquals("0OvAjbe4zln4mIk+/LYfD/ulMKFcxe+WMQsONCHDvKo=", Base64.getEncoder().encodeToString(result.hash));
  }

}
