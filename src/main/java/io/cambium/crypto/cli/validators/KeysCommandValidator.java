package io.cambium.crypto.cli.validators;

import java.util.Map;

import com.beust.jcommander.ParameterException;

public class KeysCommandValidator extends BaseValidator {

  @Override
  public void validate(Map<String, Object> parameters) throws ParameterException {
    boolean symmetric = contains(parameters, "-sym", "--symmetric");
    boolean asymmetric = contains(parameters, "-asym", "--asymmetric");
    if(!(symmetric ^ asymmetric)) { //boolean XOR
      throw new ParameterException("You must specify either symmetric or asymmetric key generation");
    }
    if(symmetric) {
      if(isNotValued(parameters, "-k", "--key")) {
        throw new ParameterException("You must specify the symmetric key output file");
      }
    } else { //asymmetric
      if(isNotValued(parameters, "-pub", "--public-key") 
      || isNotValued(parameters, "-priv", "--private-key")) 
      {
        throw new ParameterException("You must specify the asymmetric public and private key output files");
      }  
    }
  }

}
