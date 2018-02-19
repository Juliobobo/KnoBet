package fr.kb.knobet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fr.kb.knobet.Common.Common;
import fr.kb.knobet.Model.QuestionScore;

public class Done extends AppCompatActivity {

    Button btnTryAgain;
    TextView txtResultScore, getTxtResultQuestion;
    ProgressBar progressBar;

    FirebaseDatabase db;
    DatabaseReference questionScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        db = FirebaseDatabase.getInstance();
        questionScore = db.getReference("Question_Score");

        txtResultScore = (TextView)findViewById(R.id.textTotalScore);
        getTxtResultQuestion = (TextView)findViewById(R.id.textTotalQuestion);
        progressBar = (ProgressBar)findViewById(R.id.doneProgressBar);
        btnTryAgain = (Button)findViewById(R.id.btnTryAgain);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Done.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        //Get data from bundle (Play) and set to view
        Bundle ex = getIntent().getExtras();
        if(ex != null)
        {
            int score = ex.getInt("SCORE");
            int totalQuestions = ex.getInt("TOTAL");
            int correctAnswer = ex.getInt("CORRECT");

            txtResultScore.setText(String.format("SCORE : %d", score));
            getTxtResultQuestion.setText(String.format("PASSED : %d / %d", correctAnswer, totalQuestions));

            progressBar.setMax(totalQuestions);
            progressBar.setProgress(correctAnswer);

            //Upload point to Firebase
            questionScore.child(String.format("%s_%s", Common.currentUser.getUserName(),
                                                        Common.categoryId))
                            .setValue(new QuestionScore(String.format("%s_%s", Common.currentUser.getUserName(),
                                    Common.categoryId), Common.currentUser.getUserName(), String.valueOf(score)));
        }
    }
}
