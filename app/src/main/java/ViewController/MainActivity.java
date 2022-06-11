package ViewController;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.*;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.widget.*;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import Dialogs.CreateAccountDialog;
import Dialogs.ComErrorDialog;
import Dialogs.LoginErrorDialog;

import com.example.comsafe.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    Button btnLogin,btnForgot,btnCreate;
    ImageButton btnShow;
    EditText etCorreo,etContrasena;
    SharedPreferences sp;
    boolean status,passChecked;
    String CURP,nombre,apPat,apMat,contrato,fechaNac,tipo,sexo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions(new String[]{Manifest.permission.INTERNET},1);
        //Verify active session
        sp = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        status = sp.getBoolean("status",false);
        if(status){//Active session, go to fragment
            startActivity(new Intent(MainActivity.this, FragmentHolderActivity.class));
            finish();
        }

        passChecked = false;
        btnLogin = findViewById(R.id.xbtnLogin);
        btnForgot = findViewById(R.id.xbtnForgot);
        btnCreate = findViewById(R.id.xbtnAccount);
        etCorreo = findViewById(R.id.xetCorreoLogin);
        etContrasena = findViewById(R.id.xetContrasenaLogin);
        btnShow = findViewById(R.id.btnShowPassword);

        btnLogin.setOnClickListener(this);
        btnForgot.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
        btnShow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.xbtnLogin){
            //By post method
            String correo = etCorreo.getText().toString();
            String contrasena = etContrasena.getText().toString();
            String url = getString(R.string.dbAPI) + "getUser.php/";

            if(correo.isEmpty()||contrasena.isEmpty()){
                DialogFragment dialogo = new LoginErrorDialog();
                dialogo.show(getSupportFragmentManager(),"login error");
            }else{
                StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.isEmpty()){
                            //Si existe el usuario y password
                            try{
                                JSONObject jsonobj = new JSONObject(response);
                                status = Boolean.parseBoolean(jsonobj.getString("status"));
                                CURP = jsonobj.getString("curp");
                                nombre = jsonobj.getString("nombre");
                                apPat = jsonobj.getString("ap_paterno");
                                apMat = jsonobj.getString("ap_materno");
                                contrato = jsonobj.getString("contrato");
                                fechaNac = jsonobj.getString("fech_nac");
                                tipo = jsonobj.getString("tipo");
                                sexo = jsonobj.getString("sexo");

                                //Update session parameters
                                SharedPreferences.Editor miEditor = sp.edit();
                                miEditor.putBoolean("status", status);
                                miEditor.putString("CURP", CURP);
                                miEditor.putString("nombre", nombre);
                                miEditor.putString("paterno", apPat);
                                miEditor.putString("materno", apMat);
                                miEditor.putString("contrato", contrato);
                                miEditor.putString("nacimiento", fechaNac);
                                miEditor.putString("tipo", tipo);
                                miEditor.putString("sexo", sexo);
                                miEditor.commit();

                                //Redirige al menu principal
                                Intent itn = new Intent(MainActivity.this, FragmentHolderActivity.class);
                                startActivity(itn);
                                finish();
                            }catch(Exception e){
                                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }else{
                            //Usuario o password incorrectas
                            DialogFragment dialogo = new LoginErrorDialog();
                            dialogo.show(getSupportFragmentManager(),"login error");
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
                        parametros.put("correo",correo);
                        parametros.put("contrasena",contrasena);
                        return parametros;
                    }
                };

                RequestQueue rq = Volley.newRequestQueue(this);
                rq.add(sr);
            }
        }

        if(view.getId()==R.id.xbtnForgot){
            //Intent to ForgotActivity
            startActivity(new Intent(MainActivity.this, ForgotActivity.class));
        }

        if(view.getId()==R.id.xbtnAccount){
            //Dialog of create account
            DialogFragment dialogo = new CreateAccountDialog();
            dialogo.show(getSupportFragmentManager(),"create");
        }

        if(view.getId()==R.id.btnShowPassword){
            if(passChecked){//Hide password
                etContrasena.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }else{//Show password
                etContrasena.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            passChecked=!passChecked;
        }
    }
}