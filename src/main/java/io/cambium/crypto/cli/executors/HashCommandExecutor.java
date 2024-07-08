package io.cambium.crypto.cli.executors;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HexFormat;

import com.beust.jcommander.Strings;

import io.cambium.crypto.cli.commands.HashCommand;
import io.cambium.crypto.service.HashService;
import io.cambium.crypto.service.impl.Md5HashService;
import io.cambium.crypto.service.impl.Sha1HashService;
import io.cambium.crypto.service.impl.Sha256HashService;
import io.cambium.crypto.service.impl.Sha512HashService;

public class HashCommandExecutor extends BaseCommandExecutor<HashCommand> {

  public HashCommandExecutor(boolean quiet) {
    super(quiet);
  }

  public void execute(HashCommand arguments) throws IOException {
    InputStream is = null;
    if(arguments.input != null) {
      is = new BufferedInputStream(new FileInputStream(arguments.input));
    } else 
    if(!Strings.isStringEmpty(arguments.text)) {
      is = new ByteArrayInputStream(arguments.text.getBytes());
    } else 
    if(arguments.stdin) {
      is = new BufferedInputStream(System.in);
    }
    byte[] salt = Strings.isStringEmpty(arguments.salt)
        ? null
        : arguments.salt.getBytes(StandardCharsets.UTF_8);
    HashService service = null;
    if(arguments.md5) { 
      service = new Md5HashService();
    } else
    if(arguments.sha1) { 
      service = new Sha1HashService();
    } else
    if(arguments.sha256) {
      service = new Sha256HashService();
    } else
    if(arguments.sha512) {
      service = new Sha512HashService();
    } 
    byte[] hash = service.hash(salt, is);
    if(arguments.output == null) {
      //The default encoding for Unix-based md5sum, sha256sum, etc. is hex output, so use that:
      String hex = HexFormat.of().formatHex(hash);
      if(!this.quiet) {
        System.out.println(hex);
      }
    } else {
      Files.write(arguments.output.toPath(), hash, WRITE_OPTIONS);
    }
  }
  
}
