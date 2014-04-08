package uk.bris.esserver.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import uk.bris.esserver.repository.entities.Users;
import uk.bris.esserver.service.UserService;

public class ESSecurity {

	public static String getSecurePassword(String passwordToHash, String salt)
	{
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(salt.getBytes());
			byte[] bytes = md.digest(passwordToHash.getBytes());
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< bytes.length ;i++)
			{
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		}
		return generatedPassword;
	}

	//Add salt
	public static String getSalt()
	{
		SecureRandom sr;
		byte[] salt = new byte[16];

		try {
			sr = SecureRandom.getInstance("SHA1PRNG");
			sr.nextBytes(salt);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return salt.toString();
	}

	public static boolean validatePassword(Users user, String originalPassword)
	{
		String generatedPassword = null;
		if(user != null){
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				md.update(user.getSalt().getBytes()); //lastname is the salt
				byte[] bytes = md.digest(originalPassword.getBytes());
				StringBuilder sb = new StringBuilder();
				for(int i=0; i< bytes.length ;i++)
				{
					sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
				}
				generatedPassword = sb.toString();
			} 
			catch (NoSuchAlgorithmException e) 
			{
				e.printStackTrace();
			}
			if(generatedPassword.matches(user.getPassword())){
				return true;
			}
		}
		return false;
	}
}