package io.cambium.crypto.cli.validators;

import java.util.HexFormat;
import java.util.Map;

import com.beust.jcommander.ParameterException;

public class EncryptCommandValidator extends BaseValidator {

  @Override
  public void validate(Map<String, Object> parameters) throws ParameterException {
    boolean symmetric  = contains(parameters, "-sym",  "--symmetric");
    boolean asymmetric = contains(parameters, "-asym", "--asymmetric");
    boolean hybrid     = contains(parameters, "-hyb",  "--hybrid");
    int type = 0;
    if(symmetric)  type++;
    if(asymmetric) type++;
    if(hybrid)     type++;
    if(type != 1) {
      throw new ParameterException("You must specify exactly one type - "
          + "symmetric, asymmetric, or hybrid encryption");
    }
    if(symmetric || hybrid) {
      if(isNotValued(parameters, "-k", "--key")) {
        throw new ParameterException("You must specify the symmetric key file");
      }
      if(isNotValued(parameters, "-iv", "--initialization-vector")) {
        throw new ParameterException("You must specify the initialization vector");
      }
      String iv = getString(parameters, "-iv", "--initialization-vector");
      try {
        byte[] bytes = HexFormat.of().parseHex(iv.toLowerCase());
        if(bytes.length != 16) {
          throw new ParameterException("Initialization vector must represent exactly 16 bytes");
        }
      } catch(IllegalArgumentException parseFailure) {
        throw new ParameterException("Initialization vector was not a valid byte string: "
            + parseFailure.getMessage());
      }
    } 
    if(asymmetric || hybrid) {
      if(isNotValued(parameters, "-pub", "--public-key")) {
        throw new ParameterException("You must specify the public key file");
      }  
    }
    boolean stdin = contains(parameters, "-in", "--stdin");
    boolean input = contains(parameters, "-i",  "--input-file");
    if(!(stdin ^ input)) { //boolean XOR
      throw new ParameterException("You must specify input from either a file or from standard input");
    }
    if(isNotValued(parameters, "-o", "--output-file")) {
      throw new ParameterException("You must specify the output file");
    }
  }

}
