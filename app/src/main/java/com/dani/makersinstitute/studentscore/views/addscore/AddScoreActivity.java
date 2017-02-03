package com.dani.makersinstitute.studentscore.views.addscore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.dani.makersinstitute.studentscore.R;
import com.dani.makersinstitute.studentscore.model.ScoreModel;
import com.dani.makersinstitute.studentscore.utils.Constants;
import com.dani.makersinstitute.studentscore.utils.DBHelper;

public class AddScoreActivity extends AppCompatActivity {

    private EditText mTextSubject;
    private EditText mTextScore;
    private TextInputLayout mTextLayoutSubject;
    private TextInputLayout mTextLayoutScore;
    private DBHelper mDBHelper;
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_score);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextLayoutSubject = (TextInputLayout) findViewById(R.id.textLayoutSubject);
        mTextLayoutScore = (TextInputLayout) findViewById(R.id.textLayoutScore);
        mTextSubject = (EditText) findViewById(R.id.textSubject);
        mTextScore = (EditText) findViewById(R.id.textScore);

        mDBHelper = new DBHelper(this);
        mId = getIntent().getStringExtra(Constants.KEY_ID);

        if (mId != null) {
            // Set title activity
            setTitle(getString(R.string.title_activity_edit_score));
            mTextSubject.setFocusable(false); // Disable subject
            ScoreModel score = mDBHelper.getSubject(Integer.parseInt(mId));
            mTextSubject.setText(score.getSubjectName());
            mTextScore.setText(score.getSubjectScore().toString());
        }

        mTextSubject.addTextChangedListener(new InputWatcher(mTextSubject));
        mTextScore.addTextChangedListener(new InputWatcher(mTextScore));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);

        if (mId == null)
            menu.findItem(R.id.action_delete).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                saveRecord();
                break;
            case R.id.action_delete:
                deleteRecord();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean validateSubject() {
        if (mTextSubject.getText().toString().trim().isEmpty()) {
            mTextLayoutSubject.setError(getString(R.string.addscore_error_subject));
            requestFocus(mTextSubject);
            return false;
        } else {
            mTextLayoutSubject.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateScore() {
        if (mTextScore.getText().toString().trim().isEmpty()) {
            mTextLayoutScore.setError(getString(R.string.addscore_error_score));
            requestFocus(mTextScore);
            return false;
        } else {
            mTextLayoutScore.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateScoreRange() {
        Double score = Double.parseDouble(mTextScore.getText().toString());
        if (score < 0 || score > 100) {
            mTextLayoutScore.setError(getString(R.string.addscore_error_score_range));
            requestFocus(mTextScore);
            return false;
        } else {
            mTextLayoutScore.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateSubjectUnique() {
        String subject = mTextSubject.getText().toString().trim();
        if (mId == null && mDBHelper.isDuplicated(subject.toUpperCase())) {
            mTextLayoutSubject.setError(getString(R.string.addscore_error_subject_duplicated, subject));
            requestFocus(mTextSubject);
            return false;
        } else {
            mTextLayoutSubject.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class InputWatcher implements TextWatcher {

        private View view;

        private InputWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.textSubject:
                    validateSubject();
                    break;
                case R.id.textScore:
                    validateScore();
                    break;
            }
        }
    }

    private boolean saveRecord() {
        if (!validateSubject())
            return false;

        if (!validateSubjectUnique())
            return false;

        if (!validateScore())
            return false;

        if (!validateScoreRange())
            return false;

        String subjectName = mTextSubject.getText().toString().trim();
        Double subjectScore = Double.parseDouble(mTextScore.getText().toString());

        if (mId == null) {
            mDBHelper.insertSubject(subjectName, subjectScore);
        } else {
            ScoreModel score = new ScoreModel(Integer.parseInt(mId), subjectName, subjectScore);
            mDBHelper.updateSubject(score);
        }

        // Back to MainActivity
        Intent i = new Intent();
        setResult(Constants.REQUEST_ADD_SCORE, i);
        finish();

        return true;
    }

    private boolean deleteRecord() {
        //Confirm dialog
        ScoreModel score = mDBHelper.getSubject(Integer.parseInt(mId));
        AlertDialog.Builder builder = new AlertDialog.Builder(AddScoreActivity.this);
        builder.setMessage(getString(R.string.dialog_confirm_message, score.getSubjectName()));
        builder.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                mDBHelper.deleteSubject(Integer.parseInt(mId)); //Delete a record

                // Back to MainActivity
                Intent i = new Intent();
                setResult(Constants.REQUEST_ADD_SCORE, i);
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

        return true;
    }
}
