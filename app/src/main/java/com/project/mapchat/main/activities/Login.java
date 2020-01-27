package com.project.mapchat.main.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.project.mapchat.R;
import com.project.mapchat.SharedPrefs;
import com.project.mapchat.service.ServerService;
import com.project.mapchat.tutorial.OnboardingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class Login extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private SharedPrefs appSharedPrefs;
    TextView loginError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appSharedPrefs = new SharedPrefs(this);

        loginButton = findViewById(R.id.login_button);
        loginError = findViewById(R.id.loginError);

        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("public_profile email user_friends");

        checkLogin();

        // prihlásenie cez facebook
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                       // when we have access token on result
                        Log.i("SUCC","On success");
                        loginAuthRequest(loginResult.getAccessToken());
                        setViewInvisible();
                    }

                    @Override
                    public void onCancel() {
                        loginError.setText("Login Canceled");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        loginError.setText("Login Error");
                    }
                });
    }

    private void loginAuthRequest(AccessToken accessToken){
        Call<ResponseBody> call = null;
        // getting access token to string token
        String token = String.valueOf(accessToken.getToken());

        HashMap<String, String> params = new HashMap<>();
        // putting token to parameters of request
        params.put("token", token);

        // making Request Body through Gson Library
        String strRequestBody = new Gson().toJson(params);
        Log.wtf("loginAuthToken", strRequestBody);

        final RequestBody requestBody = RequestBody.create(MediaType.
                parse("application/json"), strRequestBody);

        Log.wtf("requestBody", requestBody.toString());
        call = ServerService
                .getInstance()
                .getloginAuthReq()
                .loginAuthRequest(requestBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.wtf("ResponseCode",String.valueOf(response.code()));

                if(response.isSuccessful()){
                    try {
                        String responseData = response.body().string();

                        JSONObject jsonobject = new JSONObject(responseData);
                        String serverToken = jsonobject.getString("token");
                        String fbToken = jsonobject.getString("facebookToken");

                        Log.wtf("serverToken",serverToken);
                        Log.wtf("facebookToken",fbToken);

                        appSharedPrefs.setServerToken(serverToken);
                        appSharedPrefs.setFbToken(fbToken);

                        Intent i = new Intent(Login.this, OnboardingActivity.class);
                        startActivity(i);

                    } catch (ConnectException ex){
                        ex.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    switch(response.code()){
                        case 401:{
                            Log.wtf("401","Unauthorized");
                            setViewVisible();
                            LoginManager.getInstance().logOut();
                        }
                        break;

                        case 500:{
                            Log.wtf("500","Server broken");
                            setViewVisible();
                            LoginManager.getInstance().logOut();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.wtf("Failure","Server not response");
                setViewVisible();
                LoginManager.getInstance().logOut();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    //pri zmene tokenu napr. pri jeho vymazaní
    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
        {
            if(currentAccessToken==null){
               // Toast.makeText(Login.this,"User Logged out",Toast.LENGTH_LONG).show();
               if(AccessToken.getCurrentAccessToken() == null && appSharedPrefs.getServerToken() == null
                  && appSharedPrefs.getFbToken() == null){
                   setViewVisible();

                   Toast.makeText(Login.this, "TOKENS REMOVED", Toast.LENGTH_SHORT).show();

               }

            }else{
                loadUserProfile(currentAccessToken);
                // getFbFriends(currentAccessToken);
            }
        }
    };

    // request pre profil uzivatela
    private void loadUserProfile(AccessToken newAccessToken){

        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response)
            {
                Log.wtf("RESPONSE",response.toString());
                Toast.makeText(Login.this, "loadUserProfile" , Toast.LENGTH_SHORT).show();
                //loginAuthRequest(AccessToken.getCurrentAccessToken());
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","id,name,link,email,picture,friends");
        request.setParameters(parameters);
        request.executeAsync();

    }

    // request pre friendov
    /*
    private void getFbFriends(AccessToken newAccessToken){
        GraphRequest request2 = GraphRequest.newMyFriendsRequest(newAccessToken, new GraphRequest.GraphJSONArrayCallback() {
            @Override
            public void onCompleted(JSONArray jsonArray, GraphResponse graphResponse) {
                Log.i("RESPONSE2",jsonArray.toString());
            }
        });
        request2.executeAsync();
    }*/

    private void checkLogin(){
        if(appSharedPrefs.getServerToken() != null && appSharedPrefs.getFbToken() != null){
            setViewInvisible();
            Intent i = new Intent(Login.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            Toast.makeText(this, "checkLogin()", Toast.LENGTH_SHORT).show();
        }else{
            if(AccessToken.getCurrentAccessToken() != null){
                LoginManager.getInstance().logOut();
            }
        }
    }

    private void setViewInvisible(){
        findViewById(R.id.loadingBar).setVisibility(View.VISIBLE);
        findViewById(R.id.loginLayoutWrap).setBackgroundColor(Color.parseColor("#293896"));
        findViewById(R.id.loginLayout).setVisibility(GONE);
        findViewById(R.id.login_button).setVisibility(GONE);
        findViewById(R.id.logoText).setVisibility(GONE);
    }

    private void setViewVisible() {
        findViewById(R.id.loadingBar).setVisibility(GONE);
        findViewById(R.id.loginLayoutWrap).setBackgroundColor(Color.parseColor("#FFFFFF"));
        findViewById(R.id.loginLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.login_button).setVisibility(View.VISIBLE);
        findViewById(R.id.logoText).setVisibility(View.VISIBLE );
    }
}
