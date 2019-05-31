package com.example.xmn_android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    private static final int REQUEST_SIGNUP = 0;
    private EditText inputEmail;
    private EditText inputPassword;
    private Button btnLogin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        inputEmail = (EditText) view.findViewById(R.id.input_email);
        inputPassword = (EditText) view.findViewById(R.id.input_password);
        btnLogin = (Button) view.findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        return view;
    }

    private void login() {
        if (!validate()) {
            onLoginFailed("");
            return;
        }
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        btnLogin.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.
        apiService service = RetrofitClientAPI.getRetrofitInstance().create(apiService.class);
        Call<Result> call = service.login(email, password);
        call.enqueue(new Callback<Result>() {
             @Override
             public void onResponse(Call<Result> call, Response<Result> response) {
                 Result result = response.body();
                 if (result != null) {
                     if (result.getStatus().toString().equals("FAIL")) {
                         final String message =  result.getMessage().toString();
                         new android.os.Handler().postDelayed(
                                 new Runnable() {
                                     public void run() {
                                         progressDialog.dismiss();
                                         onLoginFailed(message);
                                     }
                                 }, 2000);
                     }
                     else {
                         Paper.init(getContext());
                         apiService service = RetrofitClientAPI.getRetrofitInstance().create(apiService.class);
                         Call<User> callUser = service.getAccount( result.getReturnId());
                         callUser.enqueue(new Callback<User>() {
                             @Override
                             public void onResponse(Call<User> call, Response<User> response) {
                                 User userAccount = response.body();
                                 Paper.book().write("user_info", userAccount);
                             }

                             @Override
                             public void onFailure(Call<User> call, Throwable t) {

                             }
                         });
                         new android.os.Handler().postDelayed(
                             new Runnable() {
                                 public void run() {
                                     progressDialog.dismiss();
                                     onLoginSuccess();
                                 }
                             }, 2000);
                     }

                 } else {
                     Toast.makeText(getActivity().getApplicationContext(), "AHIHIHI đồ chó!", Toast.LENGTH_SHORT).show();
                     progressDialog.dismiss();
                 }
             }

             @Override
             public void onFailure(Call<Result> call, Throwable t) {
                 Toast.makeText(getActivity().getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                 final String message = t.toString();
                 new android.os.Handler().postDelayed(
                         new Runnable() {
                             public void run() {
                                 progressDialog.dismiss();
                                 onLoginFailed(message);
                             }
                         }, 1000);
             }
         });
    };

    public void onLoginSuccess() {
        Toast.makeText(getActivity().getBaseContext(), "Login success", Toast.LENGTH_LONG).show();

        getActivity().finish();
        inputEmail.setText(null);
        inputPassword.setText(null);
    }

    public void onLoginFailed(String message) {
        String msg = "Login failed";
        if (msg.isEmpty() == false) {
            msg = msg + ": " + message;
        }
        Toast.makeText(getActivity().getBaseContext(), msg, Toast.LENGTH_LONG).show();
        btnLogin.setEnabled(true);
    };

    private boolean validate() {
        boolean valid = true;

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("enter a valid email address");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            inputPassword.setError("at least 6 characters");
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        return valid;
    }
}
