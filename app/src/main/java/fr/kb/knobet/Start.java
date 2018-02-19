package fr.kb.knobet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

import fr.kb.knobet.Common.Common;
import fr.kb.knobet.Model.Question;

public class Start extends AppCompatActivity {

    Button btnPlay;

    FirebaseDatabase db;
    DatabaseReference questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        db = FirebaseDatabase.getInstance();
        questions = db.getReference("Questions");

        loadQuestion(Common.categoryId);

        btnPlay = (Button)findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Start.this, Play.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadQuestion(String categoryId) {
        // Clear list
        if(Common.questionList.size() > 0)
        {
            Common.questionList.clear();
        }

        questions.orderByChild("CategoryId").equalTo(categoryId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                            {
                                Question q = postSnapshot.getValue(Question.class);
                                Common.questionList.add(q);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        Collections.shuffle(Common.questionList);
    }
}
