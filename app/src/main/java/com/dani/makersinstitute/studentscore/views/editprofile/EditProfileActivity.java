package com.dani.makersinstitute.studentscore.views.editprofile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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
import com.dani.makersinstitute.studentscore.utils.Constants;

public class EditProfileActivity extends AppCompatActivity {

    private EditText mTextName;
    private EditText mTextMajor;
    private TextInputLayout mTextLayoutName;
    private TextInputLayout mTextLayoutMajor;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextLayoutName = (TextInputLayout) findViewById(R.id.textLayoutName);
        mTextLayoutMajor = (TextInputLayout) findViewById(R.id.textLayoutMajor);
        mTextName = (EditText) findViewById(R.id.textName);
        mTextMajor = (EditText) findViewById(R.id.textMajor);

        mSharedPreferences = getSharedPreferences(Constants.KEY_SHARED_PREF, MODE_PRIVATE);

        if (mSharedPreferences.contains(Constants.KEY_NAME)
                || mSharedPreferences.contains(Constants.KEY_MAJOR)) {
            String name = mSharedPreferences.getString(Constants.KEY_NAME, "");
            String major = mSharedPreferences.getString(Constants.KEY_MAJOR, "");

            mTextName.setText(name);
            mTextMajor.setText(major);
        }

        mTextName.addTextChangedListener(new InputWatcher(mTextName));
        mTextMajor.addTextChangedListener(new InputWatcher(mTextMajor));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);

        menu.findItem(R.id.action_delete).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                if (!validateName())
                    return false;

                if (!validateMajor())
                    return false;

                String name = mTextName.getText().toString().trim();
                String major = mTextMajor.getText().toString().trim();

                // Save to SharedPref
                savePref(name, major);

                // Back to MainActivity
                Intent i = new Intent();
                setResult(Constants.REQUEST_EDIT_PROFILE, i);
                finish();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean validateName() {
        if (mTextName.getText().toString().trim().isEmpty()) {
            mTextLayoutName.setError(getString(R.string.error_required,
                    getString(R.string.profile_full_name)));
            requestFocus(mTextName);
            return false;
        } else {
            mTextLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateMajor() {
        if (mTextMajor.getText().toString().trim().isEmpty()) {
            mTextLayoutMajor.setError(getString(R.string.error_required,
                    getString(R.string.profile_major)));
            requestFocus(mTextMajor);
            return false;
        } else {
            mTextLayoutMajor.setErrorEnabled(false);
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
                case R.id.textName:
                    validateName();
                    break;
                case R.id.textMajor:
                    validateMajor();
                    break;
            }
        }
    }

    private void savePref(String name, String major) {
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();

        mEditor.putString(Constants.KEY_NAME, name);
        mEditor.putString(Constants.KEY_MAJOR, major);
        mEditor.apply();
    }
}
