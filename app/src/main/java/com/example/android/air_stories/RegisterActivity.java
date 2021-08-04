package com.example.android.air_stories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.air_stories.Retrofit.INodeJS;
import com.example.android.air_stories.Retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

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


    TextInputEditText edit_password_register, edit_email_register, edit_username_register;
    MaterialButton btn_register, btn_goto_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.airstories_signup);

        // Init API
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        // View
        btn_goto_login = findViewById(R.id.goto_login_button);
        btn_register = findViewById(R.id.register_button);

        edit_email_register = findViewById(R.id.email_edit_text_register);
        edit_password_register = findViewById(R.id.password_edit_text_register);
        edit_username_register = findViewById(R.id.username_edit_text_register);



        btn_goto_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!edit_email_register.getText().toString().isEmpty() &&
                                !edit_username_register.getText().toString().isEmpty() &&
                                !edit_password_register.getText().toString().isEmpty() ){

                    String email = edit_email_register.getText().toString();
                    if(email.contains("@") && email.matches(".*[A-Za-z]*.") && email.contains(".com")){
 
                        if(edit_password_register.getText().toString().length()<8){
                            edit_password_register.setError("Password must be at last 8 characters");
                        }
                        else{
                            registerUser(edit_email_register.getText().toString()
                                    , edit_username_register.getText().toString()
                                    , edit_password_register.getText().toString());
                        }

                    }
                    else
                        edit_email_register.setError("Invalid Email");

                }else{

                    Toast.makeText(RegisterActivity.this, "Please fill all the text fields", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private void  registerUser(String email, String username, String password) {
        compositeDisposable.add(myAPI.registerUser(email, username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                            Toast.makeText(RegisterActivity.this, ""+s, Toast.LENGTH_SHORT).show();
                    }
                }));
    }

}




