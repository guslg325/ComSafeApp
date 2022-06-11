package ViewController;

import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import android.view.View.OnClickListener;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.example.comsafe.R;
import org.json.JSONObject;
import java.util.*;
import Dialogs.*;

public class CarAddActivity extends AppCompatActivity implements OnClickListener{
    Toolbar jtb;
    Spinner carColor, marcas;
    Button save,cancel;
    EditText placas,modelo;
    String contrato,curp;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_add);

        carColor = findViewById(R.id.spnCarColorAdd);
        marcas = findViewById(R.id.spnMarcas);
        save = findViewById(R.id.btnCarAddSave);
        cancel = findViewById(R.id.btnCarAddCancel);
        placas = findViewById(R.id.etPlatesCarAdd);
        modelo = findViewById(R.id.etModeloCarAdd);
        sp = this.getSharedPreferences("preferencias", Context.MODE_PRIVATE);

        //Populate color spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.carColors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carColor.setAdapter(adapter);

        //Populate brands spinner
        ArrayAdapter<CharSequence> adapterBrand = ArrayAdapter.createFromResource(this, R.array.arrayCarBrands, android.R.layout.simple_spinner_item);
        adapterBrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        marcas.setAdapter(adapterBrand);

        //Configure toolbar
        jtb = findViewById(R.id.toolbarCarAdd);
        jtb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Add button listeners
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);

        //Set the curp and contrato from shared preferences
        curp = sp.getString("CURP","[curp]");
        contrato = sp.getString("contrato","[num contrato]");

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnCarAddCancel:
                finish();
                break;
            case R.id.btnCarAddSave:
                registerCar();
                break;
            default:
        }
    }

    void registerCar(){
        String url = getString(R.string.dbAPI) + "addCar.php/";

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    //Si existen datos
                    try{
                        JSONObject jsonobj = new JSONObject(response);
                        if(jsonobj.getBoolean("status")){
                            //Correct update
                            DialogFragment success = new CreateAddCarSuccessDialog();
                            success.show(getSupportFragmentManager(),"update success");
                        }else{
                            //Error en update
                            DialogFragment fail = new CreateAddCarFailureDialog();
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
                DialogFragment dialogo = new CreateComErrorDialog();
                dialogo.show(getSupportFragmentManager(),"communication error");
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //(placas,modelo,marca,color,numcontrato,curp)
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("placas",placas.getText().toString().toUpperCase());
                parametros.put("modelo",modelo.getText().toString().toUpperCase());
                parametros.put("marca",marcas.getSelectedItem().toString());
                parametros.put("color",carColor.getSelectedItem().toString());
                parametros.put("numcontrato",contrato);
                parametros.put("curp",curp);
                return parametros;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(sr);
    }
}
