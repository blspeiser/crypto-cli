package io.cambium.crypto.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import io.cambium.crypto.cli.commands.BytesCommand;
import io.cambium.crypto.cli.commands.DecryptCommand;
import io.cambium.crypto.cli.commands.EncryptCommand;
import io.cambium.crypto.cli.commands.HashCommand;
import io.cambium.crypto.cli.commands.KeysCommand;
import io.cambium.crypto.cli.commands.UuidCommand;
import io.cambium.crypto.cli.executors.BytesCommandExecutor;
import io.cambium.crypto.cli.executors.DecryptCommandExecutor;
import io.cambium.crypto.cli.executors.EncryptCommandExecutor;
import io.cambium.crypto.cli.executors.HashCommandExecutor;
import io.cambium.crypto.cli.executors.KeysCommandExecutor;
import io.cambium.crypto.cli.executors.UuidCommandExecutor;

/**
 * CryptoCLI.
 *
 * @author Baruch Speiser, Cambium.
 */
public class CryptoCLI {

  public static void main(String... args) {
    int returnCode = 0;
    Arguments arguments = new Arguments();
    try {  
      KeysCommand keysCommand = new KeysCommand();
      HashCommand hashCommand = new HashCommand();
      UuidCommand uuidCommand = new UuidCommand();
      BytesCommand bytesCommand = new BytesCommand();
      EncryptCommand encryptCommand = new EncryptCommand();
      DecryptCommand decryptCommand = new DecryptCommand();
      
      JCommander parser = JCommander.newBuilder()
          .addObject(arguments)
          .addCommand(keysCommand)
          .addCommand(hashCommand)
          .addCommand(uuidCommand)
          .addCommand(bytesCommand)
          .addCommand(encryptCommand)
          .addCommand(decryptCommand)
          .build();
      parser.setProgramName("crypto-cli");
      if(null != args) parser.parse(args);
      String command = parser.getParsedCommand();
      if(KeysCommand.NAME.equals(command)) {
        new KeysCommandExecutor(arguments.quiet).execute(keysCommand);
      } else
      if(HashCommand.NAME.equals(command)) {
        new HashCommandExecutor(arguments.quiet).execute(hashCommand);
      } else
      if(UuidCommand.NAME.equals(command)) {
        new UuidCommandExecutor(arguments.quiet).execute(uuidCommand);
      } else
      if(BytesCommand.NAME.equals(command)) {
        new BytesCommandExecutor(arguments.quiet).execute(bytesCommand);
      } else
      if(EncryptCommand.NAME.equals(command)) {
        new EncryptCommandExecutor(arguments.quiet).execute(encryptCommand);
      } else
      if(DecryptCommand.NAME.equals(command)) {
        new DecryptCommandExecutor(arguments.quiet).execute(decryptCommand);
      } 
      else {
        if(!arguments.quiet) parser.usage(); 
        returnCode = arguments.help ? 0 : 1; //if the user specifically asked for help, then this case is not an error
      }
    } catch(ParameterException | IllegalArgumentException | UnsupportedOperationException e) {
      System.err.println(e.getMessage());
      returnCode = 1;
    } catch(Exception e) {
      e.printStackTrace();
      returnCode = -1;
    }
    if(arguments.throwInsteadOfExit) {
      if(returnCode != 0) throw new RuntimeException(Integer.toString(returnCode));
    } else {
      System.exit(returnCode);   
    }
  }

}
