package io.cambium.crypto.cli.executors;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

import com.beust.jcommander.Strings;

import io.cambium.crypto.cli.commands.BytesCommand;

public class BytesCommandExecutor extends BaseCommandExecutor<BytesCommand> {
  
  public BytesCommandExecutor(boolean quiet) {
    super(quiet);
  }

  public void execute(BytesCommand arguments) throws NoSuchAlgorithmException {
    if(Strings.isStringEmpty(arguments.text)) {
      if(arguments.numberOfBytes == null) arguments.numberOfBytes = 16;
      byte[] bytes = new byte[arguments.numberOfBytes];
      SecureRandom.getInstanceStrong().nextBytes(bytes);
      if(!this.quiet) {
        System.out.println(HexFormat.of().formatHex(bytes));
      }
    } else {
      byte[] bytes = arguments.text.getBytes();  
      String hex = HexFormat.of().formatHex(bytes);
      if(arguments.numberOfBytes != null) {
        if(bytes.length > arguments.numberOfBytes) {
          //Truncate to fit. Each byte is represented as two characters:
          hex = hex.substring(0, arguments.numberOfBytes * 2);
        } else 
        if(bytes.length < arguments.numberOfBytes) {
          //Pad to fit. Each byte is represented as two characters:
          int padding = arguments.numberOfBytes * 2;
          hex = String.format("%" + padding + "s", hex).replace(' ', '0');
        }
      }
      if(!this.quiet) {
        System.out.println(hex);
      }
    }
  }
  
}
