package io.cambium.crypto.cli.validators;

import java.util.Map;

import com.beust.jcommander.ParameterException;

public class HashCommandValidator extends BaseValidator {

  @Override
  public void validate(Map<String, Object> parameters) throws ParameterException {
    int type = 0;
    if(contains(parameters, "-md5"))    type++;
    if(contains(parameters, "-sha1"))   type++;
    if(contains(parameters, "-sha256")) type++;
    if(contains(parameters, "-sha512")) type++;
    if(type != 1) {
      throw new ParameterException("You must specify exactly one hashing algorithm");
    }
    int input = 0;
    if(contains(parameters, "-in", "--stdin"))      input++;
    if(contains(parameters, "-i",  "--input-file")) input++;
    if(contains(parameters, "-t",  "--text"))       input++;
    if(input != 1) {
      throw new ParameterException("You must specify only one of the following: "
          + "input file, text, or standard input");      
    }    
  }

}
