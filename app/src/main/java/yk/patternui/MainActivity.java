package yk.patternui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_wx_options_menu).setOnClickListener(v -> startActivity(WXOptionsMenuActivity.class));
    }

    private void startActivity(Class<? extends Activity> wxOptionsMenuActivityClass) {
        startActivity(new Intent(getApplicationContext(), wxOptionsMenuActivityClass));
    }
}
