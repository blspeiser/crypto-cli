package io.cambium.crypto.cli.validators;

import java.util.Map;

import com.beust.jcommander.ParameterException;

public class BytesCommandValidator extends BaseValidator {

  @Override
  public void validate(Map<String, Object> parameters) throws ParameterException {
    if(isValued(parameters, "-n", "--length")) {
      int length = getInteger(parameters, "-n", "--length");
      if(length <= 0) {
        throw new ParameterException("If you indicate that you want to specify the length, it must be at least 1");
      }
    }
  }

}
