package ViewController;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import androidx.appcompat.widget.Toolbar;
import com.example.comsafe.R;

public class CarEditActivity extends AppCompatActivity implements OnClickListener{
    Spinner spinner;
    Bundle bdl;
    TextView tvMarca,tvModelo,tvPlaca;
    Button jbtnCarCancel,jbtnCarSave;
    Toolbar jtb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_edit);

        tvMarca = findViewById(R.id.tvBrandDynamic);
        tvModelo = findViewById(R.id.tvModelDynamic);
        tvPlaca = findViewById(R.id.tvPlateDynamic);
        spinner = findViewById(R.id.spnCarColor);
        jbtnCarCancel = findViewById(R.id.btnCarCancel);
        jbtnCarSave = findViewById(R.id.btnCarSave);

        //Configure toolbar
        jtb = findViewById(R.id.toolbarCarEdit);
        jtb.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Populate spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.carColors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Recover data from bundle
        bdl = getIntent().getExtras();
        tvMarca.setText(bdl.getString("marca"));
        tvModelo.setText(bdl.getString("modelo"));
        tvPlaca.setText(bdl.getString("placas"));
        jbtnCarCancel.setOnClickListener(this);
        jbtnCarSave.setOnClickListener(this);

        if(bdl.getString("carColor").equals(getString(R.string.strColorRed))){
            //Rojo
            spinner.setSelection(0);
        }else if(bdl.getString("carColor").equals(getString(R.string.strColorBlue))){
            //Azul
            spinner.setSelection(1);
        }else if(bdl.getString("carColor").equals(getString(R.string.strColorCyan))){
            //Cyan
            spinner.setSelection(2);
        }else if(bdl.getString("carColor").equals(getString(R.string.strColorGuinda))){
            //Guinda
            spinner.setSelection(3);
        }else if(bdl.getString("carColor").equals(getString(R.string.strColorWhite))){
            //Blanco
            spinner.setSelection(4);
        }else if(bdl.getString("carColor").equals(getString(R.string.strColorGray))){
            //Gris
            spinner.setSelection(5);
        }else if(bdl.getString("carColor").equals(getString(R.string.strColorGreen))){
            //Verde
            spinner.setSelection(6);
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnCarCancel:
                finish();
                break;
            case R.id.btnCarSave:
                //TODO update database
                break;
            default:
        }
    }
}
