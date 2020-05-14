package com.dacasa.sdakitidistrict.Commoners;



import com.dacasa.sdakitidistrict.POJOS.User;

import java.util.List;

public interface LoginListener {

        //void signUpEmail(User user, String password);
        //void signInEmail(String email, String password);
        //boolean pageLoginSignUp();
        //boolean pageZeroProceed(boolean validated);
        //boolean pageZeroGoogle();
        //boolean pageZeroSignIn();
        boolean pageTwoProceed(User user);
        boolean pageThreeProceed(boolean validated, List<String> groups);
    }