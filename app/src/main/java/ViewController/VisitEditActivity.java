package ViewController;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.*;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.widget.*;
import android.view.View.OnClickListener;
import android.view.*;
import java.time.LocalDate;
import java.util.*;
import java.sql.Date;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.comsafe.R;

import org.json.JSONObject;

import Dialogs.ComErrorDialog;
import Dialogs.UpdateFailureDialog;
import Dialogs.UpdateSuccessfulDialog;

public class VisitEditActivity extends AppCompatActivity implements OnClickListener{
    ImageButton btnCalendar;
    TextView jtvId;
    EditText nombre,pat,mat,fecha,placas;
    Spinner medio;
    Toolbar jtb;
    Button btnSave,btnCancel,btnDelete;
    int a,m,d;
    String strNombre,strPat,strMat,strMedio,strPlacas,id;
    Date dtFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_edit);

        //Configure toolbar
        jtb = findViewById(R.id.toolbarVisitEdit);
        jtb.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Get extras from bundle and fill fields
        Calendar c = Calendar.getInstance();
        Bundle bdl = getIntent().getExtras();

        id = bdl.getString("id");
        jtvId = findViewById(R.id.tvVisitId);
        jtvId.setText(id);

        strNombre = bdl.getString("nombre");
        nombre = findViewById(R.id.etVisitName);
        nombre.setText(strNombre);

        strPat = bdl.getString("paterno");
        pat = findViewById(R.id.etVisitPatern);
        pat.setText(strPat);

        strMat = bdl.getString("materno");
        mat = findViewById(R.id.etVisitMatern);
        mat.setText(strMat);

        //Populate spinner
        medio = findViewById(R.id.spnVisitArrival);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.mediumArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medio.setAdapter(adapter);

        //Set spinner selection
        strMedio = bdl.getString("medio");
        if(bdl.getString("medio").equals(getString(R.string.medio1))){
            //AUTOMOVIL
            medio.setSelection(0);
        }else if(bdl.getString("medio").equals(getString(R.string.medio2))){
            //TRANSPORTE PUBLICO
            medio.setSelection(1);
        }

        strPlacas = bdl.getString("placas");
        placas = findViewById(R.id.etVisitPlates);
        placas.setText(strPlacas);

        //Date for the EditText
        dtFecha = Date.valueOf(bdl.getString("fecha"));
        fecha = findViewById(R.id.etVisitDate);
        fecha.setText(dtFecha.toString());

        //Date for the DatePicker
        LocalDate ldFecha = LocalDate.parse(bdl.getString("fecha"));
        a = ldFecha.getYear();
        m = ldFecha.getMonthValue() - 1;
        d = ldFecha.getDayOfMonth();

        btnCalendar = findViewById(R.id.btnVisitCalendar);
        btnCalendar.setOnClickListener(this);

        btnSave = findViewById(R.id.btnVisitSave);
        btnSave.setOnClickListener(this);

        btnCancel = findViewById(R.id.btnVisitCancel);
        btnCancel.setOnClickListener(this);

        btnDelete = findViewById(R.id.btnVisitDelete);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnVisitCalendar:
                DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int ye, int mo, int da) {
                        a = ye;
                        m = mo;
                        d = da;
                        fecha.setText(ye + "-" + (mo+1) + "-" + da);
                        dtFecha = Date.valueOf(ye + "-" + (mo+1) + "-" + da);
                    }
                }, a, m, d);
                dpd.show();
                break;
            case R.id.btnVisitSave:
                updateVisit();
                break;
            case R.id.btnVisitCancel:
                finish();
                break;
            case R.id.btnVisitDelete:
                //TODO delete visit from db
                break;
            default:
        }
    }

    void updateVisit(){
        String url = getString(R.string.dbAPI) + "updateVisit.php/";

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
                //nombres,paterno,materno,llegada,placas,fecha,id
                parametros.put("nombres",nombre.getText().toString().toUpperCase());
                parametros.put("paterno",pat.getText().toString().toUpperCase());
                parametros.put("materno",mat.getText().toString().toUpperCase());
                parametros.put("llegada",medio.getSelectedItem().toString());
                parametros.put("placas",placas.getText().toString().toUpperCase());
                parametros.put("fecha",dtFecha.toString());
                parametros.put("id",id);
                return parametros;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(sr);
    }
}
