package mx.com.desoft.hidrogas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent login = new Intent(this, LoginActivity.class);
        Intent main = new Intent(this, LoginActivity.class);

        preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(preferences.getString("usuario","")) &&
                !TextUtils.isEmpty(preferences.getString("password",""))){
            startActivity(main);
        }else{
            startActivity(login);
        }
        finish();
    }
}
