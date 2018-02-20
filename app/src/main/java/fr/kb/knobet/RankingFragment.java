package fr.kb.knobet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fr.kb.knobet.Common.Common;
import fr.kb.knobet.Interface.RankingCallBack;
import fr.kb.knobet.Model.Category;
import fr.kb.knobet.Model.QuestionScore;
import fr.kb.knobet.Model.Ranking;
import fr.kb.knobet.ViewHolder.CategoryViewHolder;
import fr.kb.knobet.ViewHolder.RankingViewHolder;


public class RankingFragment extends Fragment {

    View myFragment;

    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter;

    FirebaseDatabase db;
    DatabaseReference questionScore, rankingTable;

    int sum = 0;

    public static RankingFragment newInstance(){
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseDatabase.getInstance();
        questionScore = db.getReference("Question_Score");
        rankingTable = db.getReference("Ranking");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_ranking, container, false);

        // Implement callback
        updateScore(Common.currentUser.getUserName(), new RankingCallBack<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                // Update Ranking Table
                rankingTable.child(ranking.getUserName())
                        .setValue(ranking);

                showRanking(); // After upload, we will show rank


            }
        });
        
        return myFragment;
    }

    private void showRanking() {
        rankingTable.orderByChild("score")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot d : dataSnapshot.getChildren())
                            {
                                Ranking l = d.getValue(Ranking.class);
                                Log.d("DEBUG", l.getUserName());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
    }

    private void updateScore(final String userName, final RankingCallBack<Ranking> callBack) {
        questionScore.orderByChild("user").equalTo(userName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot data: dataSnapshot.getChildren())
                            {
                                QuestionScore q = data.getValue(QuestionScore.class);
                                sum += Integer.parseInt(q.getScore());
                            }
                            // Firebase is async => process sum here, otherwise sum will be reset
                            Ranking r = new Ranking(userName, sum);
                            callBack.callBack(r);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
    }
}
