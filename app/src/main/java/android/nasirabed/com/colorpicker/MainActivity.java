package android.nasirabed.com.colorpicker;

import android.app.Activity;
import android.graphics.Color;
import android.nasirabed.com.colorpicker.R;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SaturationBar;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.security.Key;

public class MainActivity extends Activity implements ColorPicker.OnColorChangedListener {

    private ColorPicker picker;
    private SaturationBar saturationBar;
    private Button button;
    private SeekBar seekBar;
    private String ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ipAddress = "192.168.0.104";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        picker = (ColorPicker) findViewById(R.id.picker);
        saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
        button = (Button) findViewById(R.id.button);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        saturationBar.setBottom(50);

        picker.addSaturationBar(saturationBar);
        picker.setOnColorChangedListener(this);

        seekBar.setMax(255);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sendBrightness(seekBar.getProgress());
            }
        });

        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText(ipAddress);
        editText.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.i("ColorPicker","Enter pressed");
                    ipAddress = v.getText().toString();
                }
                return false;
            }
        });
    }

    public void sendBrightness(int brightness) {
        Log.v("ColorPicker", "Brightness:" + brightness);

        try {
            Log.v("ColorPicker", "Connecting to: " + ipAddress);
            Socket socket = new Socket(ipAddress, 5204);
            Log.v("ColorPicker", "Is connected:" + socket.isBound());
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.write(7);
            dataOutputStream.write(brightness);
            socket.close();
        } catch (IOException ex) {
            Log.v("ColorPicker", "Cannot connect");
            ex.printStackTrace();
        }
    }

    public void onButtonClick(View v) {
        picker = (ColorPicker) findViewById(R.id.picker);
        int red = Color.red(picker.getColor());
        int green = Color.green(picker.getColor());
        int blue = Color.blue(picker.getColor());
        Log.v("ColorPicker", "Red:" + red + " Green:" + green + " Blue:" + blue);

        try {
            Log.v("ColorPicker", "Connecting to: " + ipAddress);
            Socket socket = new Socket(ipAddress, 5204);
            Log.v("ColorPicker", "Is connected:" + socket.isBound());
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.write(3);
            dataOutputStream.write(green);
            dataOutputStream.write(red);
            dataOutputStream.write(blue);
            socket.close();

            picker.setOldCenterColor(picker.getColor());
        } catch (IOException ex) {
            Log.v("ColorPicker", "Cannot connect");
            ex.printStackTrace();
        }
    }

    @Override
    public void onColorChanged(int color) {
        //gives the color when it's changed.
    }
}