package com.nudge.Validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by harshita on 6/7/2017.
 */
public class Validation {

    public Pattern pattern;
    public Matcher matcher;
    public Pattern pattern2;
    public Matcher matcher2;


    public Validation(){

    }
    //validation for Email:-

    public boolean isValidEmail(String email)
    {
        /*String patt="^[a-zA-Z0-9]+@[a-zA-Z]+.[a-z]{3}+$";
        pattern=Pattern.compile(patt);
        matcher=pattern.matcher(email);*/
        if (email==null)
        {
            return false;
        }

        else if (email!=null & android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()==false){
            return false;
        }
        return true;
    }

    //validation for Password:-

    public boolean isValidPassword(String password)
    {

        if (password==null){
            return false;
        }
        else if (password!=null & password.length()<6  )
        {
            return false;

        }

        else if (password!=null & password.contains(" ") )
        {
            return false;
        }
        return true;
    }


    //validation for Name:-
     public boolean isValidName(String name){

        //CASE 1: FOR Harshita Jagtap:-

         String patt="^[a-zA-Z\\s]*$";                //[A-Za-z]+\s[A-Za-z]+$
         pattern=Pattern.compile(patt);
         matcher=pattern.matcher(name);

         //CASE 2: FOR Harshita Pramod Jagtap:-

       /*  String patt2="^[A-Za-z]+\\s[A-Za-z]+\\s[A-Za-z]+$";
         pattern2=Pattern.compile(patt2);
         matcher2=pattern2.matcher(name);*/

         if(name==null||name.equalsIgnoreCase("")){
             return false;
         }


         else if (name!=null & matcher.matches()!=true)

         {
             return false;
         }

        /* else if (name!=null & matcher2.matches()!=true)
         {
             return false;
         }*/

         return true;
     }
}
