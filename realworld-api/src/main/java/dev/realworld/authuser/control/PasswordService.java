package dev.realworld.authuser.control;

import jakarta.enterprise.context.ApplicationScoped;
import java.security.*;import java.security.spec.*;import java.util.Base64;import javax.crypto.SecretKeyFactory;import javax.crypto.spec.PBEKeySpec;

@ApplicationScoped
public class PasswordService {
    public String algorithm(){return "PBKDF2WithHmacSHA256";}
    public String newSalt(){ byte[] b=new byte[16]; new SecureRandom().nextBytes(b); return Base64.getEncoder().encodeToString(b);}
    public String hash(String password,String salt){try{var spec=new PBEKeySpec(password.toCharArray(),Base64.getDecoder().decode(salt),65536,256);return Base64.getEncoder().encodeToString(SecretKeyFactory.getInstance(algorithm()).generateSecret(spec).getEncoded());}catch(Exception e){throw new IllegalStateException(e);}}
    public boolean matches(String password,String salt,String hash){return MessageDigest.isEqual(hash(password,salt).getBytes(), hash.getBytes());}
}
