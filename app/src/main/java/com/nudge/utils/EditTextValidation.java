package com.nudge.utils;

import java.util.regex.Pattern;

import android.annotation.TargetApi;
import android.os.Build;
import android.widget.EditText;

public class EditTextValidation {

	@SuppressWarnings("unused")
	private static final Pattern EMAIL_PATTERN = Pattern
			.compile("[a-zA-Z0-9+._%-+]{1,100}" + "@"
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,10}" + "(" + "."
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,20}"+
		              ")+");
	private static final Pattern USERNAME_PATTERN = Pattern
			.compile("^[a-z0-9._-]{2,25}$");
	private static final Pattern PASSWORD_PATTERN = Pattern
			.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$");
    @TargetApi(Build.VERSION_CODES.GINGERBREAD) public static boolean isNotEmpty(EditText aEditText){
        boolean empty = false;
        if(!(aEditText.getText().toString().trim().isEmpty()) ){
            empty = true;
        }
        return empty;
    }
    @TargetApi(Build.VERSION_CODES.GINGERBREAD) public static boolean isNotEmptytxt(String aEditText){
        boolean empty = false;
        if(!(aEditText.trim().isEmpty()) ){
            empty = true;
        }
        return empty;
    }
    public static boolean isValidEmail(EditText aEditText){
        boolean valid = false;
        boolean is_notempti= isNotEmpty(aEditText);
        if(is_notempti==true)
        {
        String emailStr = aEditText.getText().toString().trim();
        valid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches();
        }
        return valid;
    }
    public static boolean isValidemail_txt(String str)
    {
    	 boolean valid = false;
         boolean is_notempti= isNotEmptytxt(str);
         if(is_notempti==true)
         {
         String emailStr =str.trim();
         valid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches();
         }
         return valid;
    }
    public static boolean isValidPhoneNo(EditText aEditText){
        boolean valid = false;
        boolean is_notempti= isNotEmpty(aEditText);
        if(is_notempti==true)
        {
        String emailStr = aEditText.getText().toString().trim();
        valid = android.util.Patterns.PHONE.matcher(emailStr).matches();
        }
        return valid;
    }
    public static boolean isValidUserName(EditText aEditText){
        boolean valid = false;
        boolean is_notempti= isNotEmpty(aEditText);
        if(is_notempti==true)
        {
        String emailStr = aEditText.getText().toString().trim();
        valid = USERNAME_PATTERN.matcher(emailStr).matches();
        }
        
        
        return valid;
    }
    public static boolean isValidPassword(EditText aEditText){
        boolean valid = false;
        boolean is_notempti= isNotEmpty(aEditText);
        if(is_notempti==true)
        {
        String emailStr = aEditText.getText().toString().trim();
        valid = PASSWORD_PATTERN.matcher(emailStr).matches();
        }
        return valid;
    }
   @TargetApi(Build.VERSION_CODES.GINGERBREAD) public boolean isValidNumber(EditText aEditText, int aMaxSize, int aMinSize) {
        String text = aEditText.getText().toString().trim();
        boolean valid = false;
        if (text.isEmpty()) {
            aEditText.setError("Please Enter Number");
        } else {
            if (text.length() < aMinSize) {
                aEditText.setError("Too Short Number");
            } else {
                if (text.length() > aMaxSize) {
                    aEditText.setError("Too Long Number");
                } else {
                    valid = true;
                }
            }
        }
        return valid;
    }

}
