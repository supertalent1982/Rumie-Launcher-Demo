package com.jsdev.ruime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;

/**
 * @author Aider Abdurafeev
 */
public class PlayerActivity extends Activity {

    private static final String PACKAGE_PATH = "android.resource://com.jsdev.ruime/";
    private static final int[] videos =     new int[]{R.raw.video1,    R.raw.video2, R.raw.screencast, R.raw.video3};
    private static final int[] videoNames = new int[]{R.string.video1, R.string.video2, R.string.screencast, R.string.video3};
    private int videoIndex = 0;
    private VideoView mVideoView;
    private Button tryDemoButton;


    @Override
    protected void onPause() {
        super.onPause();
        finishVideo();
        finish();
    }

    private void finishVideo() {
        if (mVideoView != null) {
            mVideoView.suspend();
        }
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

        final Uri uri = getVideoUri();
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
                if (videoIndex < videos.length && isScreenCastVideo()) {
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

    private boolean isScreenCastVideo() {
        return R.raw.screencast == videos[videoIndex];
    }

    private Uri getVideoUri() {
        File dir = new File(Environment.getExternalStorageDirectory(), getString(R.string.sdcard_folder_name));
        final Uri uri;
        if (dir.exists()) {
            uri = getVideoUri(dir, videoNames[videoIndex]);
        } else {
            uri = Uri.parse(PACKAGE_PATH + videos[videoIndex]);
        }
        videoIndex++;
        return uri;
    }

    private Uri getVideoUri(File dir, int resId) {
        final Uri uri;
        final File video = new File(dir.getAbsolutePath(), getString(resId));
        if (video.exists()) {
            uri = Uri.fromFile(video);
        } else {
            uri = Uri.parse(PACKAGE_PATH + videos[videoIndex]);
        }
        return uri;
    }

    private void playNextVideo() {
        mVideoView.setVisibility(View.INVISIBLE);
        mVideoView.setVideoURI(getVideoUri());
        mVideoView.requestFocus();
        mVideoView.seekTo(0);
        mVideoView.start();
        mVideoView.setVisibility(View.VISIBLE);
    }
}
