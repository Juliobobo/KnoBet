package fr.kb.knobet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fr.kb.knobet.Model.QuestionScore;
import fr.kb.knobet.ViewHolder.ScoreDetailViewHolder;

public class ScoreDetail extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference questionScore;

    String viewUser = "";

    RecyclerView scoreList;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_detail);

        db = FirebaseDatabase.getInstance();
        questionScore = db.getReference("Question_Score");

        // Init view
        scoreList = (RecyclerView)findViewById(R.id.scoreList);
        layoutManager = new LinearLayoutManager(this);
        scoreList.setHasFixedSize(true);
        scoreList.setLayoutManager(layoutManager);

        if(getIntent() != null)
        {
            viewUser = getIntent().getStringExtra("viewUser");
        }
        
        if(!viewUser.isEmpty())
        {
            loadScoreDetail(viewUser);
        }
    }

    private void loadScoreDetail(String viewUser) {
        adapter = new FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder>(
                QuestionScore.class,
                R.layout.score_detail_layout,
                ScoreDetailViewHolder.class,
                questionScore.orderByChild("user").equalTo(viewUser)
        ) {
            @Override
            protected void populateViewHolder(ScoreDetailViewHolder viewHolder, QuestionScore model, int position) {
                viewHolder.txtName.setText(model.getCategoryName());
                viewHolder.txtScore.setText(model.getScore());

            }
        };
        adapter.notifyDataSetChanged();
        scoreList.setAdapter(adapter);
    }
}
