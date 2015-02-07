package com.example.jmc242.tigerwolf;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.media.AudioManager;

/**
 * Created by jmc242 on 2/7/2015.
 */
public class RingerDialogFragment extends DialogFragment {
    private AudioManager mAudioManager;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mAudioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Set phone to Vibrate?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
            }
        });
        return builder.create();
    }
}
