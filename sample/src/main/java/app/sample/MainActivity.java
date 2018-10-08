package app.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import collabalist.retroletLib.Callbacks.RequestListener;
import collabalist.retroletLib.RequestHelper.RetroLet;

public class MainActivity extends AppCompatActivity {
    Button get;
    TextView response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get = (Button) findViewById(R.id.getBtn);
        response = (TextView) findViewById(R.id.response);


        ArrayList<Integer> list = new ArrayList<>();
        list.add(25);
        list.add(24);
        String parm = Arrays.toString(list.toArray(new Integer[list.size()]));
        Log.e("params",""+parm);
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetroLet.post("http://demo2server.com/deluxtrip/api/", "save-interests")
                        .addQuery("interest_ids", "[25,24]")
                        .addHeader("Authorization", "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEwMCwiaXNzIjoiaHR0cDovL2RlbW8yc2VydmVyLmNvbS9kZWx1eHRyaXAvYXBpL3NpZ251cCIsImlhdCI6MTUzNjA2MTQ1OCwiZXhwIjozNTQyOTY3OTM1NDg1OCwibmJmIjoxNTM2MDYxNDU4LCJqdGkiOiJqUWlzdGxYUmwyUEI0TVFRIn0.i9AlZJjyvcuTxA3C-AfzbrH9Sxvqi8dbaSPw6-TmSmo")
                        .build(MainActivity.this)
                        .execute(new RequestListener() {
                            @Override
                            public void beforeExecuting(String url, Map formInfo, int TAG) {
                                response.setText("URL: " + url);
                            }

                            @Override
                            public void onResponse(String response, int TAG) {
                                MainActivity.this.response.setText(response);
                            }

                            @Override
                            public void onError(String error, String message, int TAG) {

                            }
                        });
            }
        });
    }
}
