package ViewController;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.comsafe.R;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Dialogs.*;

public class CarEditActivity extends AppCompatActivity implements OnClickListener{
    Spinner spinner;
    Bundle bdl;
    TextView tvMarca,tvModelo,tvPlaca;
    Button jbtnCarCancel,jbtnCarSave,jbtnCarDelete;
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
        jbtnCarDelete = findViewById(R.id.btnCarDelete);

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
        jbtnCarDelete.setOnClickListener(this);

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
                updateCar(spinner.getSelectedItem().toString(),tvPlaca.getText().toString());
                break;
            case R.id.btnCarDelete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_update_car_confirm);
                builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteCar();
                            }
                        });
                builder.setNegativeButton(R.string.btnCancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                //Dismiss dialog without changes
                            }
                        });
                builder.setTitle(R.string.dialog_warning_title);

                builder.create().show();
                break;
            default:
        }
    }

    void updateCar(String c, String p){
        String url = getString(R.string.dbAPI) + "updateCar.php/";

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
                parametros.put("color",c);
                parametros.put("placas",p);
                return parametros;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(sr);
    }

    void deleteCar(){
        String url = getString(R.string.dbAPI) + "deleteCar.php/";

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    //Si existen datos
                    try{
                        JSONObject jsonobj = new JSONObject(response);
                        if(jsonobj.getBoolean("status")){
                            if(jsonobj.getBoolean("status2")){
                                //Correct update
                                DialogFragment success = new UpdateSuccessfulDialog();
                                success.show(getSupportFragmentManager(),"delete success");
                            }
                        }else{
                            //Error en update
                            DialogFragment fail = new UpdateFailureDialog();
                            fail.show(getSupportFragmentManager(),"delete failed");
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
                //(placas,modelo,marca,color,numcontrato,curp)
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("placas",tvPlaca.getText().toString());
                return parametros;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(sr);
    }
}
