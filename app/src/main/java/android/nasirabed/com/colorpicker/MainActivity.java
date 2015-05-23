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
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.security.Key;

public class MainActivity extends Activity implements ColorPicker.OnColorChangedListener {

    private ColorPicker picker;
    private OpacityBar opacityBar;
    private Button button;
    private String ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ipAddress = "192.168.0.104";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        picker = (ColorPicker) findViewById(R.id.picker);
        opacityBar = (OpacityBar) findViewById(R.id.opacitybar);
        button = (Button) findViewById(R.id.button1);

        picker.addOpacityBar(opacityBar);
        picker.setOnColorChangedListener(this);

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


    public void onButtonClick(View v) {
        picker = (ColorPicker) findViewById(R.id.picker);
        int red = Color.red(picker.getColor());
        int green = Color.green(picker.getColor());
        int blue = Color.blue(picker.getColor());
        Log.v("ColorPicker", "Red:" + red + "Green:" + green + "Blue:" + blue);

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
        } catch (IOException ex) {
            Log.v("ColorPicker", "Cannot connect");
            ex.printStackTrace();
        }
    }

    @Override
    public void onColorChanged(int color) {
        //gives the color when it's changed.
    }

//    public boolean onEditorAction(EditText editText, int actionId, KeyEvent keyEvent) {
//        if(actionId != EditorInfo.IME_ACTION_DONE)
//            return false;
//
//        ipAddress = editText.getText().toString();
//        return true;
//    }
}