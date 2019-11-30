package com.strong.strongloginlib;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

/**
 * @author BuMingYang
 * @des FaceBook 登录
 */
public class GoogleLogin {

    private Activity activity;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 101;
    private String personGivenName;
    private String personFamilyName;
    private String personPhoto;

    public GoogleLogin(Activity activity, int idRes) {
        this.activity = activity;
        //region google sign in
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(idRes))
                .requestId()
                .requestEmail()
                .requestProfile()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    /**
     * 登录
     */
    public void login() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * 退出
     */
    public void logout() {
        mGoogleSignInClient.signOut();
    }

    public void handleResult(Intent data, GoogleLoginListener listener) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        if (listener != null) {
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                listener.handleResult(account);
            } catch (ApiException e) {
                e.printStackTrace();
                listener.handleResult(null);
            }
        }
    }

    public interface GoogleLoginListener {
        void handleResult(GoogleSignInAccount account);
    }
}