package com.eagersphinx.marathonapp.service;

import android.content.Context;
import android.util.AttributeSet;


public class NDSpinner extends androidx.appcompat.widget.AppCompatSpinner {

    public NDSpinner(Context context) {
        super(context);
    }

    public NDSpinner(Context context, int mode) {
        super(context, mode);
    }

    public NDSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NDSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NDSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }


    @Override
    public void setSelection(int position) {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position);
        if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }
}
