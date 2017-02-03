package com.dani.makersinstitute.studentscore.views.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dani.makersinstitute.studentscore.views.addscore.AddScoreActivity;
import com.dani.makersinstitute.studentscore.R;
import com.dani.makersinstitute.studentscore.adapter.ScoreAdapter;
import com.dani.makersinstitute.studentscore.model.ScoreModel;
import com.dani.makersinstitute.studentscore.utils.Constants;
import com.dani.makersinstitute.studentscore.utils.DBHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {

    private ArrayList<ScoreModel> mScores;
    private TextView mTextViewEmpty;
    private TextView mTextViewScoreAvg;
    private TextView mTextViewScoreDescription;
    private ListView mListView;
    private Double mAvgScore;
    private DBHelper mDBHelper;
    private ScoreAdapter mAdapter;

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_report, container, false);

        mTextViewEmpty = (TextView) view.findViewById(R.id.textViewEmpty);
        mTextViewScoreAvg = (TextView) view.findViewById(R.id.textViewScoreAvg);
        mTextViewScoreDescription = (TextView) view.findViewById(R.id.textViewScoreDescription);
        mListView = (ListView) view.findViewById(R.id.listView);

        mDBHelper = new DBHelper(getContext());
        bindData();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ScoreModel score = mAdapter.getItem(position);
                Intent intent = new Intent(getContext(), AddScoreActivity.class);
                intent.putExtra(Constants.KEY_ID, score.getId().toString());
                startActivityForResult(intent, Constants.REQUEST_ADD_SCORE);
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_ADD_SCORE
                && resultCode != getActivity().RESULT_CANCELED) {
            reloadData();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_report, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(getContext(), AddScoreActivity.class);
            startActivityForResult(intent, Constants.REQUEST_ADD_SCORE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void bindData() {
        mScores = mDBHelper.getSubject();
        if (mScores.size() != 0) {
            mAdapter = new ScoreAdapter(getContext(), mScores);
            mListView.setAdapter(mAdapter);

            mListView.setVisibility(View.VISIBLE);
            mTextViewEmpty.setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.GONE);
            mTextViewEmpty.setVisibility(View.VISIBLE);
        }

        countAverage();
        setDescriptionText();
    }

    private void reloadData() {
        mScores = mDBHelper.getSubject();
        if (mScores.size() != 0) {
            mAdapter.clear();
            mAdapter.addAll(mScores);
            mAdapter.notifyDataSetChanged();

            mListView.setVisibility(View.VISIBLE);
            mTextViewEmpty.setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.GONE);
            mTextViewEmpty.setVisibility(View.VISIBLE);
        }

        countAverage();
        setDescriptionText();
    }

    private void countAverage() {
        if (mScores.size() == 0)
            return;

        Double sumScore = 0.0;
        mAvgScore = 0.0;
        for (int i = 0; i < mScores.size(); i++) {
            sumScore += mScores.get(i).getSubjectScore();
        }

        mAvgScore = sumScore / (double) mScores.size();

        DecimalFormat format = new DecimalFormat(getString(R.string.myreport_number_format));
        mTextViewScoreAvg.setText(format.format(mAvgScore));
    }

    private void setDescriptionText() {
        if (mAvgScore == null)
            return;

        if (mAvgScore < 50)
            mTextViewScoreDescription.setText(getString(R.string.myreport_score_low));
        else if (mAvgScore >= 50 && mAvgScore <= 80)
            mTextViewScoreDescription.setText(getString(R.string.myreport_score_medium));
        else if (mAvgScore > 80)
            mTextViewScoreDescription.setText(getString(R.string.myreport_score_high));
    }
}
