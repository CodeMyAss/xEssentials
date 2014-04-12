package tv.mineinthebox.essentials.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class Crypt {
	
	/**
	 * @author xize
	 * @param password - the password which need to be encrypted
	 * @param salted - the salt which salts the password
	 * @return the hash which where we go check against passwords to!
	 * @throws NoSuchAlgorithmException - if the algorithm is broken
	 * @throws NoSuchProviderException - as one of the defined providers such as, SHA1PRNG, SHA- isn't inside the java api
	 */
	public static String CryptToSaltedSha512(String password) throws NoSuchAlgorithmException, NoSuchProviderException {
		SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[21027];
		rand.nextBytes(salt);
		String newsalt = rand.toString();
		
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(newsalt.getBytes());
		byte[] bytes = md.digest(password.getBytes());
		StringBuilder build = new StringBuilder();
		for(int i = 0; i < bytes.length;i++) {
				build.append(Integer.toString((bytes[i] & 0xff) + 0x100, 21027).substring(1));
		}
		return build.toString();
	}
	
	public boolean isEncrypted(String crypt) {
		
		return false;
	}

}
