package fr.epione.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import fr.epione.entity.User;

public class Utils {

	public static String toMD5(String passwordToHash) throws NoSuchAlgorithmException {

		String generatedPassword = null;
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(passwordToHash.getBytes());
		byte[] bytes = md.digest();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		generatedPassword = sb.toString();

		return generatedPassword;
	}

	public static boolean emailValidator(String email) {
		boolean isValid = false;
		try {
			InternetAddress internetAddress = new InternetAddress(email);
			internetAddress.validate();
			isValid = true;
		} catch (AddressException e) {
			isValid = false;
		}
		return isValid;
	}

	public static List<String> passwordValidator(String password) {
		Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
		Pattern lowerCasePatten = Pattern.compile("[a-z ]");
		Pattern digitCasePatten = Pattern.compile("[0-9 ]");
		boolean flag = true;
		List<String> listError = new ArrayList<>();
		if (!UpperCasePatten.matcher(password).find()) {
			listError.add("Le mot de passe doit contenir au moins un caractere en majuscule");
			flag = false;
		}
		if (!digitCasePatten.matcher(password).find()) {
			listError.add("Le mot de passe doit contenir au moins un caractere numerique");
			flag = false;
		}
		if (password.length() < 8) {
			listError.add("Le mot de passe doit etre superieur ou egale a 8 caracteres");
			flag = false;
		}

		if (flag)
			return null;
		return listError;

	}

}
