package ViewController;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import android.app.DatePickerDialog;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.comsafe.R;
import org.json.JSONObject;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import Dialogs.*;

public class VisitAddActivity extends AppCompatActivity implements OnClickListener{
    EditText jetNombre,jetPat,jetMat,jetFecha,jetPlacas;
    Spinner spnMedio;
    Button btnSave,btnCancel;
    ImageButton btnCalendar;
    Toolbar jtb;
    SharedPreferences sp;
    Date dtFecha;
    int a,m,d;
    String contrato,curp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_add);

        //Configure toolbar
        jtb = findViewById(R.id.toolbarVisitAdd);
        jtb.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        jetNombre = findViewById(R.id.etVisitAddName);
        jetPat = findViewById(R.id.etVisitAddPatern);
        jetMat = findViewById(R.id.etVisitAddMatern);
        jetFecha = findViewById(R.id.etVisitAddDate);
        jetPlacas = findViewById(R.id.etVisitAddPlates);
        spnMedio = findViewById(R.id.spnVisitAddArrival);
        btnSave = findViewById(R.id.btnVisitAddSave);
        btnCancel = findViewById(R.id.btnVisitAddCancel);
        btnCalendar = findViewById(R.id.btnVisitAddCalendar);

        //Populate spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.mediumArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMedio.setAdapter(adapter);

        //Date for the DatePicker
        LocalDate ldFecha = LocalDate.now();
        a = ldFecha.getYear();
        m = ldFecha.getMonthValue() - 1;
        d = ldFecha.getDayOfMonth();
        dtFecha = Date.valueOf(a+"-"+m+"-"+d);

        //Get shared preferences
        sp = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        contrato = sp.getString("contrato","contrato");
        curp = sp.getString("CURP","curp");

        //Add button listeners
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnCalendar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnVisitAddSave:
                if(contrato.equals("contrato")||curp.equals("curp")){
                    //No hay contrato o curp en las preferencias guardadas
                    DialogFragment sessionDialog = new SessionErrorDialog();
                    sessionDialog.show(getSupportFragmentManager(),"session error");
                }else
                    registerVisit();
                break;
            case R.id.btnVisitAddCancel:
                finish();
                break;
            case R.id.btnVisitAddCalendar:
                DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int ye, int mo, int da) {
                        a = ye;
                        m = mo;
                        d = da;
                        jetFecha.setText(ye + "-" + (mo+1) + "-" + da);
                        dtFecha = Date.valueOf(ye + "-" + (mo+1) + "-" + da);
                    }
                }, a, m, d);
                dpd.show();
                break;
            default:
        }
    }

    void registerVisit(){
        String url = getString(R.string.dbAPI) + "addVisit.php/";

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    //Si existen datos
                    try{
                        JSONObject jsonobj = new JSONObject(response);
                        if(jsonobj.getBoolean("status")){
                            //Correct update
                            DialogFragment success = new UpdateSuccessfulDialog();
                            success.show(getSupportFragmentManager(),"update success");
                        }else{
                            //Error en update
                            DialogFragment fail = new UpdateFailureDialog();
                            fail.show(getSupportFragmentManager(),"update failed");
                        }
                    }catch(Exception e){
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }else{
                    //Error
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Poner dialogo de error
                DialogFragment dialogo = new ComErrorDialog();
                dialogo.show(getSupportFragmentManager(),"communication error");
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("nombres",jetNombre.getText().toString().toUpperCase());
                parametros.put("paterno",jetPat.getText().toString().toUpperCase());
                parametros.put("materno",jetMat.getText().toString().toUpperCase());
                parametros.put("medio",spnMedio.getSelectedItem().toString());
                parametros.put("placas",jetPlacas.getText().toString().toUpperCase());
                parametros.put("contrato",contrato);
                parametros.put("CURP",curp);
                parametros.put("fecha",dtFecha.toString());
                return parametros;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(sr);
    }
}
