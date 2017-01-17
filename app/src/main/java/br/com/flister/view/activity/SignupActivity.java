package br.com.flister.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import br.com.flister.R;
import br.com.flister.utils.UtilView;

/**
 * Created by junior on 22/12/2016.
 */
@EActivity(R.layout.activity_signup)
public class SignupActivity extends AppCompatActivity {

    private static final String TAG = SignupActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAnalytics mFirebaseAnalytics;

    @ViewById(R.id.input_email)
    EditText txtEmail;

    @ViewById(R.id.input_password)
    EditText txtPassword;

    @ViewById(R.id.btn_signup)
    Button btnSignup;

    @ViewById(R.id.link_login)
    TextView loginLink;

    @ViewById(R.id.snackbarPosition)
    CoordinatorLayout snackBarPositionLayout;

    @AfterViews
    public void init(){
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    Intent intent = MainActivity_.intent(SignupActivity.this).get();
                    startActivity(intent);

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Click(R.id.link_login)
    void signinLink(){
        Intent intent = LoginActivity_.intent(SignupActivity.this).get();
        startActivity(intent);
    }

    @Click(R.id.btn_signup)
    void btnSignupClicked() {

        if (!validate()) {
            return;
        }

        UtilView.hideSoftkeyboard(this);
        UtilView.showIndeterminateProgressDialog("Creating account...", this);

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        UtilView.cancelIndeterminateProgressDialog();

                        if (task.isSuccessful()){
                            Snackbar.make(snackBarPositionLayout, "Account created successfully", Snackbar.LENGTH_LONG)
                                    .setAction("Close", clickListener)
                                    .show();

                            Intent intent = LoginActivity_.intent(SignupActivity.this).get();
                            startActivity(intent);
                        } else {

                            Snackbar.make(snackBarPositionLayout, task.getException().getMessage(), Snackbar.LENGTH_LONG)
                                    .setAction("Close", clickListener)
                                    .show();
                        }


                    }
                });

    }

    public boolean validate() {
        boolean valid = true;

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.setError("enter a valid email address");
            valid = false;
        } else {
            txtEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            txtPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            txtPassword.setError(null);
        }

        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    final View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {
        }
    };

}
