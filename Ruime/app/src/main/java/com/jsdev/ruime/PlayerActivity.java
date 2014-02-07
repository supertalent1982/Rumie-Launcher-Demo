package com.jsdev.ruime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * @author Aider Abdurafeev
 */
public class PlayerActivity extends Activity {

    private static final String PATH = "path";
    private static final String PACKAGE_PATH = "android.resource://com.jsdev.ruime/";
    private static final int[] videos = new int[]{R.raw.video1, R.raw.video2, R.raw.screencast, R.raw.video3};
    public static final String INDEX_KEY = "index";
    public static final String POSITION = "position";
    private int videoIndex = 0;
    private VideoView mVideoView;
    private Button tryDemoButton;


    @Override
    protected void onPause() {
        super.onPause();
        finishVideo();
    }

    private void finishVideo() {
        if (mVideoView != null) {
            mVideoView.suspend();
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishVideo();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.player_layout);
        mVideoView = (VideoView) findViewById(R.id.video);
        tryDemoButton = (Button) findViewById(R.id.btn_try_demo);
        tryDemoButton.setVisibility(View.GONE);

        //Creating MediaController
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mVideoView);

        final String videoName = String.valueOf(videos[videoIndex++]);

        final Uri uri = Uri.parse(PACKAGE_PATH + videoName);
        mVideoView.setMediaController(mediaController);

        mVideoView.setVideoURI(uri);
        mVideoView.requestFocus();

        mVideoView.start();

        tryDemoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryDemoButton.setVisibility(View.GONE);
                mVideoView.setVisibility(View.VISIBLE);
                playNextVideo();
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (videoIndex < videos.length && R.raw.screencast == videos[videoIndex]) {
                    tryDemoButton.setVisibility(View.VISIBLE);
                    mVideoView.setVisibility(View.INVISIBLE);
                } else if (videoIndex < videos.length) {
                    playNextVideo();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PlayerActivity.this);
                    builder.setTitle(R.string.close_video)
                            .setNegativeButton(R.string.close,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            finish();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                }

            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                finish();
                return true;
            }
        });

    }

    private void playNextVideo() {
        mVideoView.setVisibility(View.INVISIBLE);
        mVideoView.setVideoURI(Uri.parse(PACKAGE_PATH + videos[videoIndex++]));
        mVideoView.requestFocus();
        mVideoView.seekTo(0);
        mVideoView.start();
        mVideoView.setVisibility(View.VISIBLE);
    }
}
