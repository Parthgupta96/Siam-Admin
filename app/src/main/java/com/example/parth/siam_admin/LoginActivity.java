package com.example.parth.siam_admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.parth.siam_admin.activities.MainActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 321;
    View rootView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        rootView = findViewById(R.id.login_root);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        AuthUI authUI = AuthUI.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
            callMainActivity();
        } else {
            // not signed in
//            AuthUI.SignInIntentBuilder  signInIntentBuilder = authUI.createSignInIntentBuilder();
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(Collections.singletonList(
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .build(),
                    RC_SIGN_IN);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                callMainActivity();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button

                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar(R.string.unknown_error);
                    return;
                }
            }

            showSnackbar(R.string.unknown_sign_in_response);
        }
    }
    private void callMainActivity() {
        finish();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        Snackbar.make(rootView,"calling Main Activity", Toast.LENGTH_SHORT).show();

    }

    private void showSnackbar(int message){
        Snackbar.make(rootView,message, Toast.LENGTH_SHORT).show();
    }
}
