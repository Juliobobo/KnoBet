package fr.kb.knobet;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import fr.kb.knobet.Common.Common;
import fr.kb.knobet.Model.User;

public class MainActivity extends AppCompatActivity {

    // Sign up
    MaterialEditText edtNewUser, edtNewPassword, edtNewEmail;
    // Sign in
    MaterialEditText edtUser, edtPassword;

    Button btnSignIn, btnSignUp;

    FirebaseDatabase database;
    DatabaseReference users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        edtUser = (MaterialEditText)findViewById(R.id.edtUserName);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);

        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpDialiog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(edtUser.getText().toString(), edtPassword.getText().toString());
            }
        });
    }

    private void signIn(final String user, final String pwd) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(user).exists()){
                    if(!user.isEmpty()){
                        User log = dataSnapshot.child(user).getValue(User.class);
                        if(log.getPassword().equals(pwd)){
                            //Toast.makeText(MainActivity.this, R.string.loginOk,
                                    //Toast.LENGTH_LONG).show();

                            Intent homeActivity = new Intent(MainActivity.this, Home.class);
                            Common.currentUser = log;
                            startActivity(homeActivity);
                            finish();

                        }else{
                            Toast.makeText(MainActivity.this, R.string.wrongPwd,
                                    Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, R.string.enterUserName,
                                Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, R.string.notExitUser,
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showSignUpDialiog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle(R.string.signUp);
        alertDialog.setMessage(R.string.fill);

        LayoutInflater inflater = this.getLayoutInflater();
        View signUpLayout = inflater.inflate(R.layout.sign_up_layout, null);

        edtNewUser = (MaterialEditText)signUpLayout.findViewById(R.id.edtNewUserName);
        edtNewEmail = (MaterialEditText)signUpLayout.findViewById(R.id.edtNewEmail);
        edtNewPassword = (MaterialEditText)signUpLayout.findViewById(R.id.edtNewPassword);

        alertDialog.setView(signUpLayout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final User user = new User(edtNewUser.getText().toString(),
                                        edtNewPassword.getText().toString(),
                                            edtNewEmail.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(user.getUserName()).exists()){
                            Toast.makeText(MainActivity.this, R.string.alreadyUser,
                                                Toast.LENGTH_LONG).show();
                        }else{
                            users.child(user.getUserName()).setValue(user);
                            Toast.makeText(MainActivity.this, R.string.succesUser,
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();

    }
}
