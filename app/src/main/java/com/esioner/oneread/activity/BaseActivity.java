package com.esioner.oneread.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.esioner.oneread.ActivityController;

/**
 * Created by Esioner on 2018/6/10.
 */

public class BaseActivity extends AppCompatActivity {
    protected static ActivityController controller = new ActivityController();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        controller.addActivity(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        controller.removeActivity(this);
        super.onDestroy();
    }
}
