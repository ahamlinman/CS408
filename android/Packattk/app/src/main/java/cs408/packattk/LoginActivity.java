package cs408.packattk;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


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
            Client client = new Client(email);
            newUserValid= client.addUser(email,password);
            //newUserValid = Client.addUser(email,password);

            if(newUserValid==1)
            {
                mEmailView.setError("Invalid Username or Password");
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
            //display error message
            mEmailView.setError("Empty Username or Password for Registration");
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
        /*if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
            CharSequence two = "Attempting Login";
            Toast toast = Toast.makeText(context, two, Toast.LENGTH_SHORT);
            toast.show();


            //Attempt Login
            Client client = new Client(email);
            int check=client.checkLogin(email,password);

            AsyncTask<Void, Void, Integer> loginTask = new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... params) {
                    Client client = new Client(email);
                    return client.checkLogin(email,password);
                }

                @Override
                protected void onPostExecute(Integer check) {
                    if(check==1){
                        Intent intent = new Intent(LoginActivity.this,StudentActivity.class);
                        startActivity(intent);
                    }
                    else if(check==2){
                        Intent intent = new Intent(LoginActivity.this,AdminActivity.class);
                        startActivity(intent);
                    }
                    else{
                        mEmailView.setError("Invalid username or password");
                        mEmailView.requestFocus();
                    }
                }
            };
            loginTask.execute();

        }
    }

    private boolean isEmailValid(String email) {

        //Check for valid length
        if(email.length() < 8)
            return false;

        //Check if email has proper suffix
        if(!(email.endsWith(".edu") || email.endsWith(".com") || email.endsWith(".org") || email.endsWith(".net")))
            return false;

        //check if email has @ and does not start with @
        if(!(email.contains("@")) || email.charAt(0)=='@')
            return false;

        //check if @ is last character
        if(email.indexOf("@") != email.length()-1)
            return false;

        //get characters before and after ampersand
        char beforeAt=email.charAt(email.indexOf("@")-1);
        char afterAt=email.charAt(email.indexOf("@")+1);

        //check for valid character before and after @
        if(!(character.isAlphaNumeric(beforeAt) && character.isAlphaNumeric(afterAt)))
            return false;

        return true;
    }

    private boolean isPasswordValid(String password) {

        //Check for valid length
        if(password.length() < 5 || password.length() > 20)
            return false;

        //check for spaces in password
        for(int i=0;i<password.length();i++){
            if(character.isSpace(password.charAt(i)))
                return false;
        }
        return true;
    }
}



