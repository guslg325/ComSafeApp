package ViewController;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.widget.Toolbar;

import com.example.comsafe.R;

public class ProfileEditActivity extends AppCompatActivity implements OnClickListener {
    private SharedPreferences sp;
    private String curp,fechaNac,sexo;
    private Spinner spinner;
    private Toolbar jtb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        jtb = findViewById(R.id.toolbarProfileEdit);
        jtb.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sp = getSharedPreferences("preferencias", Context.MODE_PRIVATE);

        curp = sp.getString("CURP","[Curp no registrada]");
        fechaNac = sp.getString("nacimiento", "fechaNac");
        sexo = sp.getString("sexo", "sexo");
        spinner = findViewById(R.id.spnSexEdit);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sexArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View v){

    }
}
