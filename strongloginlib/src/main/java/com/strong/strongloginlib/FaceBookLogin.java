package com.strong.strongloginlib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * @author BuMingYang
 * @des FaceBook 登录
 */
public class FaceBookLogin {

    private Activity activity;
    private CallbackManager callbackManager;
    private FacebookListener facebookListener;
    private List<String> permissions;
    private LoginManager loginManager;

    public FaceBookLogin(Activity activity) {
        this.activity = activity;

        //初始化facebook登录服务
        callbackManager = CallbackManager.Factory.create();
        getLoginManager().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // login success
                AccessToken accessToken = loginResult.getAccessToken();
                getLoginInfo(accessToken);
            }

            @Override
            public void onCancel() {
                //取消登录
                if (facebookListener != null) {
                    facebookListener.facebookLoginCancel();
                }
            }

            @Override
            public void onError(FacebookException error) {
                //登录出错
                if (facebookListener != null) {
                    facebookListener.facebookLoginFail(error.getMessage());
                }
            }
        });

        permissions = Arrays
                .asList("email", "user_likes", "user_status", "user_photos", "user_birthday", "public_profile", "user_friends");
    }

    /**
     * 登录
     */
    public void login() {
        getLoginManager().logOut();
        getLoginManager().logInWithReadPermissions(
                activity, permissions);
    }

    /**
     * 退出
     */
    public void logout() {
        String logout = activity.getResources().getString(
                com.facebook.R.string.com_facebook_loginview_log_out_action);
        String cancel = activity.getResources().getString(
                com.facebook.R.string.com_facebook_loginview_cancel_action);
        String message;
        Profile profile = Profile.getCurrentProfile();
        if (profile != null && profile.getName() != null) {
            message = String.format(
                    activity.getResources().getString(
                            com.facebook.R.string.com_facebook_loginview_logged_in_as),
                    profile.getName());
        } else {
            message = activity.getResources().getString(
                    com.facebook.R.string.com_facebook_loginview_logged_in_using_facebook);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton(logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FaceBookLogin.this.getLoginManager().logOut();
                    }
                })
                .setNegativeButton(cancel, null);
        builder.create().show();
    }

    /**
     * 获取登录信息
     *
     * @param accessToken
     */
    public void getLoginInfo(final AccessToken accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (object != null) {
                    if (facebookListener != null) {
                        // 此处参数根需要自己修改
                        facebookListener.facebookLoginSuccess(accessToken, object);
                    }
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,email,picture,locale,updated_time,timezone,age_range,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();

    }

    /**
     * 获取loginMananger
     *
     * @return
     */
    private LoginManager getLoginManager() {
        if (loginManager == null) {
            loginManager = LoginManager.getInstance();
        }
        return loginManager;
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    /**
     * 设置登录简体器
     *
     * @param facebookListener
     */
    public void setFacebookListener(FacebookListener facebookListener) {

        this.facebookListener = facebookListener;
    }

    public interface FacebookListener {

        /**
         * 登录成功
         *
         * @param accessToken
         * @param object      用户信息 JSONObject
         */
        void facebookLoginSuccess(AccessToken accessToken, JSONObject object);

        /**
         * 登录失败
         *
         * @param message 失败信息
         */
        void facebookLoginFail(String message);

        /**
         * 取消登录
         */
        void facebookLoginCancel();
    }
}