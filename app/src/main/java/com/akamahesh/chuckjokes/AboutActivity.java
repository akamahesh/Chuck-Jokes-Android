package com.akamahesh.chuckjokes;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context,AboutActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        String version = getVersionDetail();
        TextView tvVersion = findViewById(R.id.tvVersion);
        tvVersion.setText(version);
    }

    private String getVersionDetail(){
        String version = "Version ";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String name = pInfo.versionName;
            int code = pInfo.versionCode;
            version = version+ name ;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;

    }


}
