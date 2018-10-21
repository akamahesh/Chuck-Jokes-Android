package com.akamahesh.chuckjokes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.akamahesh.chuckjokes.model.Joke;
import com.akamahesh.chuckjokes.network.APIManager;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();
    public static int REQUEST_INVITE = 101;
    private TextView mJokeTextView;
    private ImageView mChuckImageView;
    private FABProgressCircle fabProgressCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, getString(R.string.banner_ad_unit_id));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mJokeTextView = findViewById(R.id.tv_joke);
        fabProgressCircle = findViewById(R.id.fabProgressCircle);
        mChuckImageView = findViewById(R.id.iv_main_image);
        AdRequest adRequest = new AdRequest.Builder().build();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRandomJoke();
            }
        });


    }

    void getRandomJoke(){
        fabProgressCircle.show();
        APIManager.APIManager().getRequestHelper().getRandomJoke().enqueue(new Callback<Joke>() {
            @Override
            public void onResponse(Call<Joke> call, Response<Joke> response) {
                Log.v(TAG,"onResponse");
                mJokeTextView.setText(response.body().getValue());
                fabProgressCircle.hide();
                updateImage(response.body().getIconUrl());
            }

            @Override
            public void onFailure(Call<Joke> call, Throwable t) {
                Log.v(TAG,"onFailure");
                fabProgressCircle.hide();
            }
        });
    }

    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    private void onInvite() {
        String joke = mJokeTextView.getText().toString().trim();
        String appURL = "https://play.google.com/store/apps/details?id=com.akamahesh.chuckjokes";
        String shareText  = joke + " For More jokes checkout  "+appURL;
        ShareCompat.IntentBuilder
                .from(this)
                .setText(shareText)
                .setType("text/plain")
                .setChooserTitle("Share ChuckJokes with your friends")
                .startChooser();
    }

    private void onAbout(){
        startActivity(AboutActivity.getIntent(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }
    }

    private void updateImage(String iconUrl) {
        Picasso.get().load(R.drawable.chucknorris_logo).into(mChuckImageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_invite:
                onInvite();
                break;
            case R.id.action_about:
                onAbout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
