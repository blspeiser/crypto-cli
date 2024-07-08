package io.cambium.crypto.cli.validators;

import java.util.Map;

import com.beust.jcommander.IParametersValidator;

public abstract class BaseValidator implements IParametersValidator {
  
  protected boolean contains(Map<String, Object> parameters, String... keys) {
    for(String key : keys) {
      if(parameters.get(key) != null) return true;
    }
    return false;
  }
  
  protected boolean isValued(Map<String, Object> parameters, String... keys) {
    for(String key : keys) {
      Object value = parameters.get(key); 
      if(null == value) continue;
      if(value instanceof String s) {
        if(s.isBlank()) continue;
        //otherwise fall through
      }
      return true; //object is not null and is not an empty string
    }
    return false;
  }
  protected boolean isNotValued(Map<String, Object> parameters, String... keys) {
    return !isValued(parameters, keys);
  }
  
  protected int getInteger(Map<String, Object> parameters, String... keys) {
    for(String key : keys) {
      Object value = parameters.get(key); 
      if(value instanceof Integer i) return i.intValue();
    }
    return -1;
  }
  protected String getString(Map<String, Object> parameters, String... keys) {
    for(String key : keys) {
      Object value = parameters.get(key); 
      if(value instanceof String s) return s;
    }
    return null;
  }

}
