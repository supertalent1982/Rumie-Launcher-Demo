package com.jsdev.ruime;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.jsdev.ruime.services.GameAlarm;
import com.jsdev.ruime.structures.Code;
import com.jsdev.rumie.utility.CodeHelper;
import com.jsdev.rumie.utility.PrefsHelper;

import java.util.Calendar;

public class DialogActivity extends Activity {

    @Override
    public void onResume() {
        super.onResume();

        makeDialog();
    }

    private void makeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning!");
        builder.setMessage(getResources().getString(R.string.attention));

        final EditText passwordEdit = new EditText(this);
        passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(passwordEdit);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Code code = CodeHelper.isValid(DialogActivity.this, passwordEdit.getText().toString());

                if (code != null) {
                    PrefsHelper.isActive = true;

                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(c.getTimeInMillis() + (code.validDuration * 60000));

                    AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent i = new Intent(DialogActivity.this, GameAlarm.class);
                    i.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                    PendingIntent pi = PendingIntent.getBroadcast(DialogActivity.this, 0, i, 0);
                    am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);

                    finish();
                } else {
                    Toast.makeText(DialogActivity.this, "Error, invalid code.", Toast.LENGTH_LONG).show();
                    makeDialog();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PrefsHelper.isActive = false;
                Intent intent = new Intent(DialogActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        builder.setCancelable(false);

        builder.create().show();
    }
}
