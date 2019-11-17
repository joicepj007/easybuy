package easybuy.com.easybuy.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

import easybuy.com.easybuy.Controller.AppSession;
import easybuy.com.easybuy.Controller.Constants;
import easybuy.com.easybuy.Interfaces.onApiFinish;
import easybuy.com.easybuy.R;
import easybuy.com.easybuy.ResponseClass.LoginResponse;
import easybuy.com.easybuy.WebServices.Backgroundtask.CallWebServiceTask;
import easybuy.com.easybuy.util.Fonts;

public class LoginActivity extends RootActivity implements onApiFinish {

    LinearLayout ll_back_arrow;
    TextView tv_head_login, tv_login;
    ImageView img_ebuy_logo;
    EditText edt_username, edt_password;
    String userName, password;
    private final String FROM_LOGIN = "login";
    private Context mContext;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mContext = this;
        tv_head_login =(TextView)findViewById(R.id.tv_head_login);
        tv_login =(TextView)findViewById(R.id.tv_login);

        edt_username =(EditText) findViewById(R.id.edt_username);
        edt_password =(EditText) findViewById(R.id.edt_password);

        img_ebuy_logo =(ImageView) findViewById(R.id.img_ebuy_logo);

        ll_back_arrow =(LinearLayout)findViewById(R.id.ll_back_arrow);

        tv_head_login.setTypeface(Fonts.getTypefaceTwo(LoginActivity.this, "Lato-Regular.ttf"));
        tv_login.setTypeface(Fonts.getTypefaceTwo(LoginActivity.this, "Lato-Regular.ttf"));
        edt_username.setTypeface(Fonts.getTypefaceTwo(LoginActivity.this, "Lato-Regular.ttf"));
        edt_password.setTypeface(Fonts.getTypefaceTwo(LoginActivity.this, "Lato-Regular.ttf"));

        tv_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                userName = edt_username.getText().toString().trim();
                password = edt_password.getText().toString().trim();
                if (edt_username.getText().toString().trim().length() != 0 ||edt_password.getText().toString().trim().length() != 0 ){

                    if (edt_username.getText().toString().length()==0){
                        try {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        showSnackBar("Please enter your username");
                    }
                    else if (edt_password.getText().toString().length() == 0) {

                        try {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                        showSnackBar("Please enter valid Password ");

                    }
                    else {

                        try {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                           login();



                    }

                }

                else {

                    try {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    showSnackBar("Please fill all fields");

                }

              /* Intent intent = new Intent(LoginActivity.this, OrderActivity.class);
                startActivity(intent);
                finish();*/
            }
        });

        ll_back_arrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });
    }

    private void login() {

        String URL = Constants.API.LOGIN;
        HashMap<String, String> values = new HashMap<>();
        values.put("username", userName);
        values.put("password", password);
        CallWebServiceTask objCallWebServiceTask = new CallWebServiceTask(LoginActivity.this, values);
        objCallWebServiceTask.execute(URL, FROM_LOGIN, "POST");
    }

    @Override
    public void onSuccess(String tag, String response) {
        if (tag.equals(FROM_LOGIN)) {


            Gson gson = new Gson();
            LoginResponse loginResponse = gson.fromJson(response, LoginResponse.class);
            if (loginResponse.getStatus().equals("success")) {

                AppSession.getInstance().setUserId(loginResponse.getUserId());
                AppSession.getInstance().setStoreId(loginResponse.getStoreId());
                AppSession.getInstance().setGrocery_name(loginResponse.getStoreName());
                AppSession.getInstance().setCustomer_address(loginResponse.getStoreAddress());
                AppSession.getInstance().setLoggedInStatus(true);
                AppSession.getInstance().setOrderListFlag(true);

                Intent intent = new Intent(LoginActivity.this, OrderActivity.class);
                startActivity(intent);
                finish();
            }

            else {
                showSnackBar(loginResponse.getMessage());
            }
        }
    }

    @Override
    public void onFailed(String tag, String response) {
        Gson gson = new Gson();
        LoginResponse loginResponse = gson.fromJson(response, LoginResponse.class);
        //Toast.makeText(mContext,response, Toast.LENGTH_SHORT).show();
        showSnackBar("No match for Email or Password");
    }

    @Override
    public void onJsonError(String tag, String response) {

        Gson gson = new Gson();
        LoginResponse loginResponse = gson.fromJson(response, LoginResponse.class);
        //Toast.makeText(mContext,response, Toast.LENGTH_SHORT).show();
        showSnackBar(loginResponse.getMessage());
    }

    @Override
    public void onServerError() {

        Toast.makeText(mContext,"server error", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed()

    {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1){
            boolean done = getSupportFragmentManager().popBackStackImmediate();
        }else
        {
            if (doubleBackToExitPressedOnce) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                ActivityCompat.finishAffinity(this);
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }

    }
}