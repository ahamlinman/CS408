package cs408.packattk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        // Set up the login form.

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        Button mEmailRegisterButton= (Button) findViewById(R.id.email_register_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mEmailRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    public void attemptRegister(){
        //Reset errors
        mEmailView.setError(null);
        mPasswordView.setError(null);
        String email= mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;

        View focusView=null;

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            int newUserValid=1;
            Client client = new Client();
            //newUserValid= client.addUser(email,password);
            //newUserValid = Client.addUser(email,password);

            if(newUserValid==1)
            {
                focusView=mEmailView;
                cancel=true;
                return;
            }
            else
            {
                attemptLogin(); //Attempt login now that we have registered the user
            }

        }
        else {
            focusView = mPasswordView;
            cancel = true;
            focusView.requestFocus();
        }

       return;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.


            //Attempt Login
            AsyncTask<Void, Void, Integer> loginTask = new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... params) {
                    Client client = new Client();
                    return client.checkLogin(email,password);
                }

                @Override
                protected void onPostExecute(Integer check) {
                    if(check==1){
                        Intent intent = new Intent(LoginActivity.this,AdminActivity.class);
                        intent.putExtra("user",email);
                        Data.username=email;
                        startActivity(intent);
                    }
                    else if(check==2){
                        Intent intent = new Intent(LoginActivity.this,AdminActivity.class);
                        intent.putExtra("user",email);
                        Data.username=email;
                        startActivity(intent);
                    }
                    else{
                        mEmailView.requestFocus();
                    }
                }
            };
            loginTask.execute();

        }
    }

}



