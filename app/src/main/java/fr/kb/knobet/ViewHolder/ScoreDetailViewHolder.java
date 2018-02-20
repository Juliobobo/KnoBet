package fr.kb.knobet.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import fr.kb.knobet.R;

/**
 * Created by Julien on 20/02/2018.
 */

public class ScoreDetailViewHolder extends RecyclerView.ViewHolder {

    public TextView txtName, txtScore;

    public ScoreDetailViewHolder(View itemView) {
        super(itemView);

        txtName = (TextView)itemView.findViewById(R.id.txt_name);
        txtScore = (TextView)itemView.findViewById(R.id.txt_score);
    }
}
