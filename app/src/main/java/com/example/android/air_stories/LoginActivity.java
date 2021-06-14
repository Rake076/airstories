package com.example.android.air_stories;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.air_stories.Model.User;
import com.example.android.air_stories.Retrofit.INodeJS;
import com.example.android.air_stories.Retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    TextInputEditText edit_password, edit_email;
    MaterialButton btn_login, btn_goto_register;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.airstories_login);


        Intent intent = getIntent();

        // Init API
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        // View
        btn_login = findViewById(R.id.login_button);
        btn_goto_register = findViewById(R.id.goto_register_button);

        edit_email = findViewById(R.id.email_edit_text);
        edit_password = findViewById(R.id.password_edit_text);

        // Event
        btn_login.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                }
                else
                    connected = false;
                if(connected) {
                    if(edit_email.getText().toString().contains("@"))
                        loginUser(edit_email.getText().toString(), edit_password.getText().toString());
                    else
                        edit_email.setError("Invalid Email");
                } else
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

            }
        });


        btn_goto_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);

            }
        });



    }


    private void  loginUser(String email, String password) {
        compositeDisposable.add(myAPI.loginUser(email, password)
                .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            if(s.contains("email")){


                                JSONObject jsonObject = new JSONObject(s);
                                SaveSharedPreference.setUserName(getApplicationContext(), jsonObject.getString("username"));
                                SaveSharedPreference.setJSONString(getApplicationContext(), s);
//                                Toast.makeText(LoginActivity.this, "Login Successful " + jsonObject.getString("username"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                                intent.putExtra("stringuserdata", s);
                                startActivity(intent);




                            }
                            else
                                Toast.makeText(LoginActivity.this, ""+s, Toast.LENGTH_SHORT).show();
                        }
                    }));
    }


    }
