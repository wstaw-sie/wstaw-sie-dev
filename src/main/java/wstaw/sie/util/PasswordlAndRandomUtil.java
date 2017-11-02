package wstaw.sie.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import wstaw.sie.model.entity.Credentials;
import wstaw.sie.model.entity.User;

public class PasswordlAndRandomUtil {

	private final SecureRandom secureRandom = new SecureRandom();
	/**
	 * @return String hex encoded of length 8 chars.
	 */
	public static String generateSaltHex() {
		int noBytesOfSalt = 4;
		return DatatypeConverter.printHexBinary(getRandomBytes(noBytesOfSalt));
	}
	
	private static byte[] getRandomBytes(int noBytes) {
		SecureRandom secureRandom = new SecureRandom();
		byte[] saltBytes = new byte[noBytes];
		secureRandom.nextBytes(saltBytes);
		return saltBytes;
	}
	
 /**
  * 
  * @param toHash String in any format
  * @return Byte array of data given in hex string.
  */
 public static byte[] hashSHA256(String toHash) {
   byte[] hash = null;
   MessageDigest md;
   try {
     //byte[] data = DatatypeConverter.parseHexBinary(toHash);
     md = MessageDigest.getInstance("SHA-256");
     md.update(toHash.getBytes());
     hash = md.digest();
   } catch (NoSuchAlgorithmException e) {
     e.printStackTrace();
   }
   return hash;
 }
 
	public static byte[] hashSHA256(byte[] toHash) {
		byte[] hash = null;
		MessageDigest md;
		try {
			// byte[] data = DatatypeConverter.parseHexBinary(toHash);
			md = MessageDigest.getInstance("SHA-256");
			md.update(toHash);
			hash = md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hash;
	}
	
	public static byte[] concat2ByteArrays(byte[] saltBytes, byte[] passwordPlainBytes) {
		byte[] both = new byte[saltBytes.length + passwordPlainBytes.length];
		System.arraycopy(saltBytes, 0, both, 0, saltBytes.length);
		System.arraycopy(passwordPlainBytes, 0, both, saltBytes.length, passwordPlainBytes.length);
		return both;
	}
	
	public static String getHashedPasswordAfterMigrationGivenGivenSaltAndPlainPassword(String saltHex, String plainPassword)
	{
		byte[] saltBytes = DatatypeConverter.parseHexBinary(saltHex);
		byte[] passwordPlainBytes = plainPassword.getBytes();
		return DatatypeConverter.printHexBinary(hashSHA256(concat2ByteArrays(saltBytes, passwordPlainBytes)));
	}
	
	  public static boolean isPasswordOfGivenUserBeforeMigrationValid(User user, String passwordPlain) {
	    byte[] password = PasswordlAndRandomUtil.hashSHA256(user.getSalt()+passwordPlain);
	    byte[] passwordFromDatabase = DatatypeConverter.parseHexBinary(user.getPassword().toLowerCase());
	    boolean passwordsAreSame = true;
	    for(int i =0 ; i< password.length; i++)
	    {
	      if(password[i] != passwordFromDatabase[i])
	      {
	        passwordsAreSame= false;
	      }
	    }
	    return passwordsAreSame;
	  }

	  public static boolean isPasswordOfGivenUserAfterMigrationValid(User user, String passwordPlain) {
			byte[] both = PasswordlAndRandomUtil.concat2ByteArrays(DatatypeConverter.parseHexBinary(user.getSalt()), passwordPlain.getBytes());
			byte[] passwordFromInput = PasswordlAndRandomUtil.hashSHA256(both);
			byte[] passwordFromDatabase = DatatypeConverter.parseHexBinary(user.getPassword());
			boolean passwordsAreSame = true;
			for (int i = 0; i < passwordFromInput.length; i++) {
				if (passwordFromInput[i] != passwordFromDatabase[i]) {
					passwordsAreSame = false;
				}
			}
			return passwordsAreSame;
		}
	  
	  public static boolean isPasswordOfGivenUserValid(User user, String passwordPlain)
	  {
		  return user.getIsMigrated() ? isPasswordOfGivenUserAfterMigrationValid(user, passwordPlain) : isPasswordOfGivenUserBeforeMigrationValid(user, passwordPlain);
	  }
	  
		public static String getRandomStringBase64(int noBytes)
		{
			SecureRandom secureRandom = new SecureRandom();
			byte[] randomBytes = new byte[noBytes];
			secureRandom.nextBytes(randomBytes);
			return DatatypeConverter.printBase64Binary(randomBytes);
		}
		
		public static String getRandomStringHexEncoded(int noBytes)
		{
			SecureRandom secureRandom = new SecureRandom();
			byte[] randomBytes = new byte[noBytes];
			secureRandom.nextBytes(randomBytes);
			return DatatypeConverter.printHexBinary(randomBytes);
		}
}
