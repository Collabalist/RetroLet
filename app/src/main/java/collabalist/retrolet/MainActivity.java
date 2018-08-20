package collabalist.retrolet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.Map;

import collabalist.retroletLib.Callbacks.RequestListener;
import collabalist.retroletLib.RetroLet;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView status;
    Button btn, buton, editBtn, spinner;
    LinearLayout linearLayout;
    PickerHelper pickerHelper;
    int type = 0;
    File profile, cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status = (TextView) findViewById(R.id.requestData);
        btn = (Button) findViewById(R.id.btn);
        buton = (Button) findViewById(R.id.buton);
        editBtn = (Button) findViewById(R.id.editText);
        spinner = (Button) findViewById(R.id.spinner);
        linearLayout = (LinearLayout) findViewById(R.id.contentLL);

        pickerHelper = PickerHelper.with(MainActivity.this, MainActivity.this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        buton.setOnClickListener(this);
        editBtn.setOnClickListener(this);
        spinner.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == pickerHelper.ACTION_REQUEST_CAMERA
                || requestCode == pickerHelper.ACTION_REQUEST_GALLERY) && resultCode == RESULT_OK) {
            if (type == 0) {
                profile = null;
                Bitmap bitmap = pickerHelper.getBitmap(data, requestCode);
                try {
                    bitmap = pickerHelper.getResizedBitmap(bitmap, 300);
                    profile = pickerHelper.saveBitmapToFile(bitmap, "nSiightProfilePic" + System.currentTimeMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                cover = null;
                Bitmap bitmap = pickerHelper.getBitmap(data, requestCode);
                try {
                    bitmap = pickerHelper.getResizedBitmap(bitmap, 1100);
                    cover = pickerHelper.saveBitmapToFile(bitmap, "nSiightProfilePic" + System.currentTimeMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buton:
                break;
            case R.id.editText:
                type = 0;
                pickerHelper.showPickerDialog();
                break;
            case R.id.spinner:
                type = 1;
                pickerHelper.showPickerDialog();
                break;
        }
    }
}
