package com.example.android.quakereport;

public class LoginFailException extends Exception{
    public LoginFailException(){
        super("Incorrect login credentials.");
    }
}
