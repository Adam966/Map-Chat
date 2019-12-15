package com.project.mapchat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;

public class Login extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private SharedPrefs appSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appSharedPrefs = new SharedPrefs(this);

        loginButton = findViewById(R.id.login_button);

        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("public_profile email");

        checkLogin();

        // prihlásenie cez facebook
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        setViewInvisible();
                       Log.i("SUCC","On success");
                       Intent i = new Intent(Login.this,OnboardingActivity.class);
                       startActivity(i);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
        {
            if(currentAccessToken==null){
               // Toast.makeText(Login.this,"User Logged out",Toast.LENGTH_LONG).show();
               if(AccessToken.getCurrentAccessToken() == null){
                   setViewVisible();
                   Toast.makeText(Login.this, "TOKEN REMOVED", Toast.LENGTH_SHORT).show();
               }
            }else{
                loadUserProfile(currentAccessToken);
            }
        }
    };

    // callback pre request
    private void loadUserProfile(AccessToken newAccessToken){

        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response)
            {
                try {
                    String id = object.getString("id");
                    Toast.makeText(Login.this, "loadUserProfile"+AccessToken.getCurrentAccessToken().toString() , Toast.LENGTH_SHORT).show();
                    appSharedPrefs.setId(id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void checkLogin(){

        if(AccessToken.getCurrentAccessToken() != null){
            setViewInvisible();
            Intent i = new Intent(Login.this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            Toast.makeText(this, "checkLogin() "+AccessToken.getCurrentAccessToken().toString(), Toast.LENGTH_SHORT).show();
        }
    }


    private void setViewInvisible(){
        findViewById(R.id.loadingBar).setVisibility(View.VISIBLE);
        findViewById(R.id.login_button).setVisibility(GONE);
    }

    private void setViewVisible() {
        findViewById(R.id.loadingBar).setVisibility(GONE);
        findViewById(R.id.login_button).setVisibility(View.VISIBLE);
    }

}
