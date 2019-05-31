package com.example.xmn_android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupFragment extends Fragment {

    private EditText inputEmail, inputName;
    private EditText inputPassword, inputPasswordAgain;
    private Button btnSignup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        inputName = (EditText) view.findViewById(R.id.input_name_signup);
        inputEmail = (EditText) view.findViewById(R.id.input_email_signup);
        inputPassword = (EditText) view.findViewById(R.id.input_password_signup);
        inputPasswordAgain = (EditText) view.findViewById(R.id.input_password_again);
        btnSignup = (Button) view.findViewById(R.id.btn_signup);


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

//        _loginLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Finish the registration screen and return to the Login activity
//                finish();
//            }
//        });
        return view;
    }

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }

        btnSignup.setEnabled(false);

        String name = inputName.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String password2 = inputPasswordAgain.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        // TODO: Implement your own signup logic here.
        apiService service = RetrofitClientAPI.getRetrofitInstance().create(apiService.class);
        Call<Result> call = service.signupAccount(name, email, password, password2);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result result = response.body();
                if (result != null ){
                    Toast.makeText(getActivity().getApplicationContext(), result.getMessage(), Toast.LENGTH_LONG).show();
                    new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                onSignupSuccess();
                                progressDialog.dismiss();
                            }
                        }, 2000);
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "AHIHIHI đồ chó!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                onSignupFailed();
                                progressDialog.dismiss();
                            }
                        }, 1000);
            }
        });
    }

    public void onSignupSuccess() {
        btnSignup.setEnabled(true);
        Toast.makeText(getActivity().getBaseContext(), "Signup success", Toast.LENGTH_LONG).show();
        TabLayout tabhost = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        tabhost.getTabAt(0).select();

        inputPassword.setText(null);
        inputEmail.setText(null);
        inputName.setText(null);
        inputPasswordAgain.setText(null);
    }

    public void onSignupFailed() {
        Toast.makeText(getActivity().getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();
        btnSignup.setEnabled(true);
    };

    public boolean validate() {
        boolean valid = true;

        String name = inputName.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String password2 = inputPasswordAgain.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            inputName.setError("at least 6 characters");
            valid = false;
        } else {
            inputName.setError(null);
        }

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

        if (password2.isEmpty() || password2.length() < 6) {
            inputPassword.setError("please input password again");
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        if (!password2.equals(password)) {
            inputPasswordAgain.setError("password doesn't match to the password");
        } else {
            inputPasswordAgain.setError(null);
        }
        return valid;
    }

}
