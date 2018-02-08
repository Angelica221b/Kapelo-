package agtzm.kapelo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class App extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final int RequestSignInCode = 7;
    public FirebaseAuth firebaseAuth;
    public GoogleApiClient googleApiClient;
    Button SignOutButton;

    com.google.android.gms.common.SignInButton signInButton;
    TextView loginUsername, loginUserEmail;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app);

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        SignOutButton = (Button) findViewById(R.id.sign_out);
        loginUsername = (TextView) findViewById(R.id.textViewName);
        loginUserEmail = (TextView) findViewById(R.id.textViewEmail);

        signInButton = (com.google.android.gms.common.SignInButton)findViewById(R.id.sign_in_button);

        firebaseAuth = FirebaseAuth.getInstance();

        loginUsername.setVisibility(View.GONE);
        loginUserEmail.setVisibility(View.GONE);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(App.this)
                .enableAutoManage(App.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                }/* */)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
                .build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSignInMethod();
            }
        });

        SignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSignOutFunction();
            }
        });
    }

    public void UserSignInMethod(){
        Intent AuthIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);

        startActivityForResult(AuthIntent, RequestSignInCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RequestSignInCode){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(googleSignInResult.isSuccess()){
                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
                FirebaseUserAuth(googleSignInAccount);

                startActivity(new Intent(this,Main.class));
            }

        }
    }

    public void FirebaseUserAuth(GoogleSignInAccount googleSignInAccount){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);

        Toast.makeText(App.this," "+authCredential.getProvider(),Toast.LENGTH_SHORT).show();

        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(App.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task AuthResultTask) {

                                if(AuthResultTask.isSuccessful()){
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                    //Mostrar boton sing out
                                    SignOutButton.setVisibility(View.VISIBLE);

                                    signInButton.setVisibility(View.GONE);

                                    loginUserEmail.setVisibility(View.GONE);
                                    loginUsername.setVisibility(View.GONE);

                                    loginUsername.setText(("NAME: " + firebaseUser.getDisplayName().toString()));
                                    loginUserEmail.setText("Email: "+ firebaseUser.getEmail().toString());


                                }else{
                                    Toast.makeText(App.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }

    public void UserSignOutFunction(){
        firebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                        Toast.makeText(App.this,"Log out", Toast.LENGTH_SHORT).show();

                    }
                }
        );

        SignOutButton.setVisibility(View.GONE);

        loginUserEmail.setText(null);
        loginUsername.setText(null);

        signInButton.setVisibility(View.VISIBLE);
    }

}




