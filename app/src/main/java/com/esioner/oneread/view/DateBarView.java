package com.esioner.oneread.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.esioner.oneread.R;

/**
 * Created by Esioner on 2018/6/10.
 */

public class DateBarView extends LinearLayout{
    public DateBarView(Context context) {
        this(context,null);
    }

    public DateBarView(Context context, @Nullable AttributeSet attrs) {
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_home_page_date_bar,this);

    }

    public DateBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);

    }

    public DateBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
