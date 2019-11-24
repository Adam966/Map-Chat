package com.project.mapchat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class Login extends AppCompatActivity {

    private LoginButton loginButton;
    private CircleImageView circleImageView;
    private TextView txtName,txtEmail;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_button);
        txtName = findViewById(R.id.name);
        txtEmail = findViewById(R.id.email);

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("public_profile email");
        checkLogin();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
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
                txtName.setText("");
                txtEmail.setText("");
                Toast.makeText(Login.this,"User Logged out",Toast.LENGTH_LONG);
            }else{
                loadUserProfile(currentAccessToken);
            }
        }
    };

    private void loadUserProfile(AccessToken newAccessToken){

        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response)
            {
                try {
                    String name = object.getString("name");
                    String email = object.getString("email");
                    String id = object.getString("id");

                    SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                    editor.putString("id", id);
                    editor.apply();

                    // String imageUrl = "https://graph.facebook.com/"+id+"/picture?type=normal";
                    txtName.setText(name);
                    txtEmail.setText(email);
                    // Glide.with(Login.this).load(imageUrl).into(circleImageView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*Log.i("FACEBOOK", object.toString());
                Log.i("NAME",name);
                Log.i("EMAIL",email);*/

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void checkLogin(){
        if(AccessToken.getCurrentAccessToken() != null){
            loadUserProfile(AccessToken.getCurrentAccessToken());
        }
    }

}
