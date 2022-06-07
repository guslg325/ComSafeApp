package ViewController;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.*;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.comsafe.R;
import org.json.JSONArray;
import org.json.JSONObject;
import Dialogs.*;
import LogicController.*;
import java.util.*;

public class CarsFragment extends Fragment implements OnClickListener{
    ImageButton btnAddCar;
    ListView lvRegAutomoviles;
    CarAdapter cadapter;
    SharedPreferences sp;
    Boolean status;
    String model,brand,plates,carColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cars, container, false);

        btnAddCar = rootView.findViewById(R.id.btnAddAutomoviles);
        lvRegAutomoviles = rootView.findViewById(R.id.lvRegistroAutomoviles);
        cadapter = new CarAdapter(getContext(),R.layout.car_row);
        sp = rootView.getContext().getSharedPreferences("preferencias", Context.MODE_PRIVATE);

        btnAddCar.setOnClickListener(this);
        lvRegAutomoviles.setAdapter(cadapter);
        lvRegAutomoviles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO reaccionar a seleccionar un auto
            }
        });

        updateList();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnAddAutomoviles:
                break;
            default:
        }
    }

    private List<Car> getEntradas(){
        final List<Car> data = new ArrayList<Car>();

        //By post method
        String curp = sp.getString("CURP","[curp]");
        String url = getString(R.string.dbAPI) + "getCars.php/";

        if(curp.equals("[curp]")){
            DialogFragment dialogo = new CreateSessionErrorDialog();
            dialogo.show(getParentFragmentManager(),"session error");
        }else{
            StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(!response.isEmpty()){
                        //Si existen datos
                        try{
                            JSONObject jsonobj = new JSONObject(response);
                            JSONArray keys = jsonobj.names();
                            status = Boolean.parseBoolean(jsonobj.getString("status"));
                            for(int i = 1; i < keys.length(); i++){
                                JSONObject obj = jsonobj.getJSONObject(keys.getString(i));
                                plates = obj.getString("placas");
                                model = obj.getString("modelo");
                                brand = obj.getString("marca");
                                carColor = obj.getString("color");

                                Car nuevo = new Car(plates,model,brand,carColor);
                                cadapter.add(nuevo);
                            }
                        }catch(Exception e){
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }else{
                        //No hay datos guardados
                        //TODO poner mensaje de vacío
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Poner dialogo de error
                    DialogFragment dialogo = new CreateComErrorDialog();
                    dialogo.show(getParentFragmentManager(),"communication error");
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parametros = new HashMap<String,String>();
                    parametros.put("CURP",curp);
                    return parametros;
                }
            };

            RequestQueue rq = Volley.newRequestQueue(getContext());
            rq.add(sr);
        }

        return data;
    }

    public void updateList(){
        for( Car c : getEntradas()) {
            cadapter.add(c);
        }
    }
}
