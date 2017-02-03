package com.dani.makersinstitute.studentscore.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dani.makersinstitute.studentscore.R;
import com.dani.makersinstitute.studentscore.model.ScoreModel;

import java.util.ArrayList;

/**
 * Created by dani@taufani.com on 1/25/17.
 */
public class ScoreAdapter extends ArrayAdapter<ScoreModel> {

    public ScoreAdapter(Context context, ArrayList<ScoreModel> scoreList) {
        super(context, R.layout.list_item, scoreList);
    }

    public static class ViewHolder {
        TextView subjectId;
        TextView subjectName;
        TextView subjectScore;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the data item for this position
        ScoreModel score = getItem(position);
        ViewHolder viewHolder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.subjectId = (TextView) convertView.findViewById(R.id.textViewSubjectId);
            viewHolder.subjectName = (TextView) convertView.findViewById(R.id.textViewSubjectName);
            viewHolder.subjectScore = (TextView) convertView.findViewById(R.id.textViewSubjectScore);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        //set Text
        viewHolder.subjectId.setText(score.getId().toString());
        viewHolder.subjectName.setText(score.getSubjectName());
        viewHolder.subjectScore.setText(score.getSubjectScore().toString());

        //retun view of item
        return convertView;
    }
}
