package fr.kb.knobet;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import fr.kb.knobet.Common.Common;

public class Play extends AppCompatActivity implements View.OnClickListener{

    final static long INTERVAL = 1000; // 1 sec
    final static long TIMEOUT = 7000; // 7 sec

    int progressValue = 0;

    CountDownTimer mCountDown;

    int index = 0;
    int score = 0;
    int thisQuestion = 0;
    int totalQuestion, correctAnswer;

    ProgressBar progressBar;
    ImageView questionImage;
    Button btnA, btnB, btnC, btnD;
    TextView txtScore, txtQuestionNum, questionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // Views
        txtScore = (TextView)findViewById(R.id.textScore);
        txtQuestionNum = (TextView)findViewById(R.id.textTotalQuestion);
        questionText = (TextView)findViewById(R.id.questionText);
        questionImage = (ImageView)findViewById(R.id.questionImage);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        btnA = (Button)findViewById(R.id.btnAnswerA);
        btnB = (Button)findViewById(R.id.btnAnswerB);
        btnC = (Button)findViewById(R.id.btnAnswerC);
        btnD = (Button)findViewById(R.id.btnAnswerD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mCountDown.cancel();

        // If still have question in the list
        if(index < totalQuestion)
        {
            Button clickedButton = (Button)view;
            if(clickedButton.getText().equals(Common.questionList.get(index).getCorrectAnswer()))
            {
                // Choose correct answer
                score += 10;
                correctAnswer++;
                showQuestion(++index);
            }
            else
            {
                // Choose wrong answer
                Intent intent = new Intent(this, Done.class);
                Bundle dataSend = new Bundle();
                dataSend.putInt("SCORE", score);
                dataSend.putInt("TOTAL", totalQuestion);
                dataSend.putInt("CORRECT", correctAnswer);
                intent.putExtras(dataSend);
                startActivity(intent);
                finish();
            }

            txtScore.setText(String.format("%d", score));
        }
    }

    private void showQuestion(int index) {
        if(index < totalQuestion)
        {
            thisQuestion++;
            txtQuestionNum.setText(String.format("%d / %d", thisQuestion, totalQuestion));
            progressBar.setProgress(0);
            progressValue = 0;

            if(Common.questionList.get(index).getIsImageQuestion().equals("true"))
            {
                // If there is an image
                Picasso.with(getBaseContext()).load(Common.questionList.get(index).getQuestion())
                        .into(questionImage);
                questionImage.setVisibility(View.VISIBLE);
                questionText.setVisibility(View.INVISIBLE);
            }
            else
            {
                questionText.setText(Common.questionList.get(index).getQuestion());
                questionImage.setVisibility(View.INVISIBLE);
                questionText.setVisibility(View.VISIBLE);
            }

            btnA.setText(Common.questionList.get(index).getAnswerA());
            btnB.setText(Common.questionList.get(index).getAnswerB());
            btnC.setText(Common.questionList.get(index).getAnswerC());
            btnD.setText(Common.questionList.get(index).getAnswerD());

            mCountDown.start(); // Start Timer
        }
        else
        {
            // If it is final question
            Intent intent = new Intent(this, Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE", score);
            dataSend.putInt("TOTAL", totalQuestion);
            dataSend.putInt("CORRECT", correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion = Common.questionList.size();
        mCountDown = new CountDownTimer(TIMEOUT, INTERVAL) {
            @Override
            public void onTick(long l) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {
                mCountDown.cancel();

            }
        };
        showQuestion(index);
    }
}
