package com.github.teocci.openmxplayerdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.github.teocci.openmxplayer.OpenMXPlayer;
import com.github.teocci.openmxplayer.PlayerEvents;

public class MainActivity extends Activity
{
    final static public String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements OnClickListener
    {
        SeekBar seekbar;
        EditText et;
        TextView tv;

        PlayerEvents events = new PlayerEvents()
        {
            @Override
            public void onStop()
            {
                seekbar.setProgress(0);
                tv.setText(R.string.playback_stopped);
            }

            @Override
            public void onStart(String mime, int sampleRate, int channels, long duration)
            {
                Log.d(TAG, "onStart called: " + mime + " sampleRate:" + sampleRate + " channels:" + channels);
                if (duration == 0)
                    Toast.makeText(getActivity(), "This is a LIVE Stream!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "This is a RECORDING!", Toast.LENGTH_SHORT).show();
                tv.setText("Playing content:" + mime + " " + sampleRate + "Hz " + (duration / 1000000) + "sec");
            }

            @Override
            public void onPlayUpdate(int percent, long currentms, long totalms)
            {
                seekbar.setProgress(percent);
            }

            @Override
            public void onPlay()
            {
            }

            @Override
            public void onError()
            {
                seekbar.setProgress(0);
                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                tv.setText(R.string.error_enconuntered);
            }
        };

        OpenMXPlayer p = new OpenMXPlayer(events);

        public PlaceholderFragment()
        {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            // set listeners on buttons
            rootView.findViewById(R.id.idbut1).setOnClickListener(this);
            rootView.findViewById(R.id.idbut2a).setOnClickListener(this);
            rootView.findViewById(R.id.idbut2b).setOnClickListener(this);
            rootView.findViewById(R.id.idbut2c).setOnClickListener(this);
            rootView.findViewById(R.id.idbut3).setOnClickListener(this);
            rootView.findViewById(R.id.idbut4).setOnClickListener(this);
            rootView.findViewById(R.id.idbut5).setOnClickListener(this);
            rootView.findViewById(R.id.idbut6).setOnClickListener(this);

            seekbar = (SeekBar) rootView.findViewById(R.id.seekbar);
            seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                {
                    if (fromUser) p.seek(progress);
                }
            });

            et = (EditText) rootView.findViewById(R.id.idet1);
            tv = (TextView) rootView.findViewById(R.id.idtv1);

            return rootView;
        }

        @Override
        public void onClick(View v)
        {
            int id = v.getId();
            switch (id) {
                case R.id.idbut1:
                    showDialog(getActivity(), "codecs", OpenMXPlayer.listCodecs());
                    break;
                case R.id.idbut2a: {
                    Log.d(TAG, "Load an audio file from resources.");
                    p.setDataSource(getActivity(), R.raw.samplemp3);
                    Toast.makeText(getActivity(), "Now press play!", Toast.LENGTH_SHORT).show();
                }
                break;
                case R.id.idbut2b: {
                    Log.d(TAG, "Load an audio file from resources.");
                    p.setDataSource(getActivity(), R.raw.sampleaac);
                    Toast.makeText(getActivity(), "Now press play!", Toast.LENGTH_SHORT).show();
                }
                break;
                case R.id.idbut2c: {
                    Log.d(TAG, "Load an audio file from resources.");
                    p.setDataSource(getActivity(), R.raw.samplewma);
                    Toast.makeText(getActivity(), "Now press play!", Toast.LENGTH_SHORT).show();
                }
                break;
                case R.id.idbut3:
                    Log.d(TAG, "Load an audio file from given location.");
                    p.setDataSource(et.getText().toString());
                    Toast.makeText(getActivity(), "Now press play!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.idbut4:
                    Log.d(TAG, "Start playing!");
                    p.play();
                    break;
                case R.id.idbut5:
                    Log.d(TAG, "Pause.");
                    p.pause();
                    break;
                case R.id.idbut6:
                    Log.d(TAG, "Stop.");
                    p.stop();
                    break;
            }
        }
    }

    private static void showDialog(Context context, String title, String msg)
    {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setIcon(R.drawable.ic_launcher)
                .show();
    }
}
