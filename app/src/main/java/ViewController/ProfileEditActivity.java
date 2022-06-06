package ViewController;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.widget.Toolbar;
import android.widget.*;
import com.example.comsafe.R;
import java.util.Calendar;

public class ProfileEditActivity extends AppCompatActivity implements OnClickListener {
    private SharedPreferences sp;
    private String curp,fechaNac,sexo;
    private Spinner spinner;
    private Toolbar jtb;
    private Button btnCancel,btnSave;
    private ImageButton btnCalendar;
    private EditText jetBirthdayEdit;
    int a,m,d;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        Calendar c = Calendar.getInstance();

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
        a = c.get(Calendar.YEAR);
        m = c.get(Calendar.MONTH);
        d = c.get(Calendar.DAY_OF_MONTH);
        spinner = findViewById(R.id.spnSexEdit);
        btnCalendar = findViewById(R.id.btnCalendar);
        btnCancel = findViewById(R.id.btnCancelEdit);
        btnSave = findViewById(R.id.btnSaveEdit);
        jetBirthdayEdit = findViewById(R.id.etBirthdayEdit);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sexArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        btnCalendar.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnCalendar:
                DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int ye, int mo, int da) {
                        a = ye;
                        m = mo;
                        d = da;
                        jetBirthdayEdit.setText(da + "/" + (mo+1) + "/" + ye);
                    }
                }, a, m, d);
                dpd.show();
                break;
            case R.id.btnCancelEdit:
                finish();
                break;
            case R.id.btnSaveEdit:
                //TODO insert into remote db
                Toast.makeText(this,"Boton guardar presionado",Toast.LENGTH_LONG).show();
                break;
            default:
        }
    }
}
