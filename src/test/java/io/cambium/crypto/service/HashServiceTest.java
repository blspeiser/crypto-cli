package io.cambium.crypto.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.Test;

import io.cambium.crypto.service.impl.Md5HashService;
import io.cambium.crypto.service.impl.Sha256HashService;
import io.cambium.crypto.service.impl.Sha512HashService;

class HashServiceTest {

  @Test
  void testMD5() {
    byte[] input = "The quick brown fox jumped over the lazy dog.".getBytes(StandardCharsets.UTF_8);
    HashService service = new Md5HashService();
    byte[] hash = service.hash(null, new ByteArrayInputStream(input));
    assertNotNull(hash);
    assertTrue(hash.length > 0);
    assertEquals("XG/73UDZVWtzoh5jw+DpBA==", Base64.getEncoder().encodeToString(hash));
  }
  
  @Test
  void testSHA256() {
    byte[] input = "The quick brown fox jumped over the lazy dog.".getBytes(StandardCharsets.UTF_8);
    byte[] salt = "rocksalt".getBytes(StandardCharsets.UTF_8);
    
    HashService service = new Sha256HashService();
    
    byte[] hash = service.hash(salt, new ByteArrayInputStream(input));
    assertNotNull(hash);
    assertTrue(hash.length > 0);
    assertEquals("cm9ja3NhbHQ=", Base64.getEncoder().encodeToString(salt));
    assertEquals("SaEwFWScCLpiYi+1ubmXadQfdvXksmBkX6O+KJ0BEao=", Base64.getEncoder().encodeToString(hash));
    
    //verify that changing the salt changes the hash
    salt = "sodium chloride".getBytes(StandardCharsets.UTF_8);
    hash = service.hash(salt, new ByteArrayInputStream(input));
    assertNotNull(hash);
    assertTrue(hash.length > 0);
    assertEquals("c29kaXVtIGNobG9yaWRl", Base64.getEncoder().encodeToString(salt));
    assertEquals("0OvAjbe4zln4mIk+/LYfD/ulMKFcxe+WMQsONCHDvKo=", Base64.getEncoder().encodeToString(hash));
  }

  @Test
  void testSHA512() {
    byte[] input = "The quick brown fox jumped over the lazy dog.".getBytes(StandardCharsets.UTF_8);
   
    HashService service = new Sha512HashService();
    
    byte[] hash = service.hash(null, new ByteArrayInputStream(input));
    assertNotNull(hash);
    assertTrue(hash.length > 0);
    assertEquals("mZUO21D8pGbcY0fxAFoaXpHJ4+cDDbiyMujDUzGVBIdKAhYh1fXYe7OgQshmvtY8jZ8O2UKlt8mrQB0sYjxWEw==", Base64.getEncoder().encodeToString(hash));
  }
  
}
