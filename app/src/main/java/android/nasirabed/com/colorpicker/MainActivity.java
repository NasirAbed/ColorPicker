package android.nasirabed.com.colorpicker;

import android.app.Activity;
import android.nasirabed.com.colorpicker.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;


public class MainActivity extends Activity implements ColorPicker.OnColorChangedListener {

    private ColorPicker picker;
    private OpacityBar opacityBar;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        picker = (ColorPicker) findViewById(R.id.picker);
        opacityBar = (OpacityBar) findViewById(R.id.opacitybar);
        button = (Button) findViewById(R.id.button1);

        picker.addOpacityBar(opacityBar);
        picker.setOnColorChangedListener(this);

//        button.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                text.setTextColor(picker.getColor());
//                picker.setOldCenterColor(picker.getColor());
//            }
//        });
    }

    @Override
    public void onColorChanged(int color) {
        //gives the color when it's changed.
    }
}