package fr.epione.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import fr.epione.entity.Horaires;
import fr.epione.entity.Journee;
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
	public static boolean validateTime(int heure,int minute,Date date){
		Calendar newDate = Calendar.getInstance();
		newDate.set(Calendar.HOUR_OF_DAY, heure);
		newDate.set(Calendar.MINUTE, minute);
		Calendar nowDate = Calendar.getInstance();
		Calendar dateFromUser = Calendar.getInstance();
		dateFromUser.setTime(date);
		if ((newDate.get(Calendar.HOUR_OF_DAY) >= nowDate.get(Calendar.HOUR_OF_DAY)
				&& newDate.get(Calendar.MINUTE) - nowDate.get(Calendar.MINUTE) > 15)
				|| (newDate.get(Calendar.HOUR_OF_DAY) > nowDate.get(Calendar.HOUR_OF_DAY)) 
				||(dateFromUser.after(nowDate))){
			return true;
		}else{
			return false;
		}
	}

	public static String[] getOsBrowserUser(String browserDetails) {
		String userAgent = browserDetails;
		String user = userAgent.toLowerCase();

		String os = "";
		String browser = "";

		// =================OS=======================
		if (userAgent.toLowerCase().indexOf("windows") >= 0) {
			os = "Windows";
		} else if (userAgent.toLowerCase().indexOf("mac") >= 0) {
			os = "Mac";
		} else if (userAgent.toLowerCase().indexOf("x11") >= 0) {
			os = "Unix";
		} else if (userAgent.toLowerCase().indexOf("android") >= 0) {
			os = "Android";
		} else if (userAgent.toLowerCase().indexOf("iphone") >= 0) {
			os = "IPhone";
		} else {
			os = "UnKnown, More-Info: " + userAgent;
		}
		// ===============Browser===========================
		if (user.contains("msie")) {
			String substring = userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
			browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
		} else if (user.contains("safari") && user.contains("version")) {
			browser = (userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0] + "-"
					+ (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
		} else if (user.contains("opr") || user.contains("opera")) {
			if (user.contains("opera"))
				browser = (userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0] + "-"
						+ (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
			else if (user.contains("opr"))
				browser = ((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-"))
						.replace("OPR", "Opera");
		} else if (user.contains("chrome")) {
			browser = (userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
		} else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)
				|| (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1)
				|| (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1)) {
			// browser=(userAgent.substring(userAgent.indexOf("MSIE")).split("
			// ")[0]).replace("/", "-");
			browser = "Netscape-?";

		} else if (user.contains("firefox")) {
			browser = (userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
		} else if (user.contains("rv")) {
			browser = "IE-" + user.substring(user.indexOf("rv") + 3, user.indexOf(")"));
		} else {
			browser = "UnKnown, More-Info: " + userAgent;
		}
		String[] tabs = { os, browser };
		return tabs;

	}

}
