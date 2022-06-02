package ViewController;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View.OnClickListener;

import com.example.comsafe.R;

public class ForgotActivity extends AppCompatActivity implements OnClickListener {
    Toolbar jtb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        jtb = findViewById(R.id.toolbarForgot);
        jtb.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {//Al presionar un boton

    }
}
