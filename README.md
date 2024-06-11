Command-line tool for performing common cryptographic tasks, like generating keys, hashing, encryption, decryption, and producing UUIDs. It was designed primarily to be specifically lightweight with minimal dependencies, and to only rely on the JCA implementations for encryption. 

It is well-suited for use in CI/CD scripts or as a basic command line tool.  It packages all of its relevant dependencies as a single jar, and is provided with shell scripts for both Linux and Windows. 

Download the code, run `mvn clean package`, and it will create `target/crypto-cli.jar` and `target/crypto.zip`.  You can use the jar directly; the zip file contains the jar file alongside the shell scripts if that's easier for you. 

```
Usage: crypto-cli [options] [command] [command options]
  Options:
    -?, --help
      Display system usage
  Commands:
    keys      Generate cryptographic keys or key pairs
      Usage: keys [options]
        Options:
          -asym, --asymmetric
            Create a set of asymmetric public and private keys
            Default: false
          -k, --key
            The file for the symmetric secret key
          -p, --password
            When generating symmetric keys, optionally create one from a 
            password 
          -priv, --private-key
            The file for the asymmetric private key
          -pub, --public-key
            The file for the asymmetric public key
          -s, --salt
            When generating symmetric keys with a password, optionally specify 
            the salt
          -sym, --symmetric
            Create a symmetric secret key
            Default: false

    hash      Generate hashes
      Usage: hash [options]
        Options:
          -i, --input-file
            File to be hashed
          -o, --output-file
            Optionally store the output as a binary file
          -s, --salt
            Optionally specify the salt
          -in, --stdin
            Read standard input instead of using an input file
            Default: false
          -md5
            Hash using MD5
            Default: false
          -sha256
            Hash using SHA-256
            Default: false
          -sha512
            Hash using SHA3-512
            Default: false

    uuid      Generate universally unique IDs (UUIDs)
      Usage: uuid

    bytes      Generate byte strings for cryptographic inputs
      Usage: bytes [options]
        Options:
          -n, --length
            Number of bytes to generate in a random byte string (16 if not 
            specified) 
            Default: 16
          -t, --text
            Text to be rendered as a hexadecimal byte string

    encrypt      Encrypt a file
      Usage: encrypt [options]
        Options:
          -asym, --asymmetric
            Use asymmetric encryption with a public key
            Default: false
          -iv, --initialization-vector
            16-byte Initialization vector, represented as a 32-character hex 
            string; example: c6069ad81f8a6b8b281cb5627288c1e2
          -i, --input-file
            File to be encrypted
          -k, --key
            Specify the key for symmetric encryption, or where to create the 
            encrypted secret key for asymmetric encryption
          -o, --output-file
            File to store the encrypted output
          -pub, --public-key
            The file for the asymmetric public key
          -in, --stdin
            Read standard input instead of using an input file
            Default: false
          -sym, --symmetric
            Use symmetric encryption with a single secret key
            Default: false

    decrypt      Decrypt a file
      Usage: decrypt [options]
        Options:
          -asym, --asymmetric
            Use asymmetric decryption with a private key
            Default: false
          -iv, --initialization-vector
            16-byte Initialization vector, represented as a 32-character hex 
            string; example: c6069ad81f8a6b8b281cb5627288c1e2
          -i, --input-file
            File to be encrypted
          -k, --key
            Specify the key (or encrypted key) for decryption
          -o, --output-file
            File to store the encrypted output
          -priv, --private-key
            The file for the private key
          -in, --stdin
            Read standard input instead of using an input file
            Default: false
          -sym, --symmetric
            Use symmetric decryption with a single secret key
            Default: false
```
