package com.example.ShriTailors.model;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;  

@Converter
public class CryptoConverter implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final byte[] KEY = "MySuperSecretKey".getBytes();

    @Override
    public String convertToDatabaseColumn(String custName) {
      // do some encryption
      Key key = new SecretKeySpec(KEY, "AES");
      try {
         Cipher c = Cipher.getInstance(ALGORITHM);
         c.init(Cipher.ENCRYPT_MODE, key);
         //return Base64.encodeBytes(c.doFinal(custName.getBytes()));
         return Base64.getEncoder().encodeToString(custName.getBytes());
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
    }

    @Override
    public String convertToEntityAttribute(String custName) {
      // do some decryption
      Key key = new SecretKeySpec(KEY, "AES");
      try {
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        return new String(c.doFinal(Base64.getDecoder().decode(custName)));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
}