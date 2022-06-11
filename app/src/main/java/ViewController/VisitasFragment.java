package ViewController;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.*;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.fragment.app.*;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.comsafe.R;
import org.json.*;
import java.sql.Date;
import java.util.*;
import Dialogs.*;
import LogicController.*;

public class VisitasFragment extends Fragment implements OnClickListener{
    ImageButton btnAddVisita;
    ListView lvRegVisitas;
    VisitAdapter vadapter;
    SharedPreferences sp;
    Boolean status;
    String nombre,paterno,materno,medio,placas;
    Date fecha;
    int id;
    TextView emptyListMessage;

    public VisitasFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_visitas, container, false);

        btnAddVisita = rootView.findViewById(R.id.btnAddVisitas);
        lvRegVisitas = rootView.findViewById(R.id.lvRegistroVisitas);
        vadapter = new VisitAdapter(getContext(),R.layout.visit_row);
        sp = rootView.getContext().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        emptyListMessage = rootView.findViewById(R.id.tvEmptyVisitList);

        btnAddVisita.setOnClickListener(this);
        lvRegVisitas.setAdapter(vadapter);
        lvRegVisitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Visit v = (Visit) adapterView.getItemAtPosition(i);
                //TODO create VisitEditActivity
                /*Intent itn = new Intent(getContext(),VisitEditActivity.class);
                Bundle bdl = new Bundle();
                bdl.putString("nombre",v.getName());
                bdl.putString("paterno",v.getPatern());
                bdl.putString("materno",v.getMatern());
                bdl.putString("fecha",v.getDate().toString());
                bdl.putInt("id",v.getId());
                bdl.putString("mLlegada",v.getArriveMedium());
                bdl.putString("placas",v.getPlates());
                itn.putExtras(bdl);
                startActivity(itn);*/
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
        switch (v.getId()){
            case R.id.btnAddVisitas:
                //TODO create activity VisitAddActivity
                break;
            default:
        }
    }

    private List<Visit> getEntradas(){
        final List<Visit> data = new ArrayList<Visit>();

        //By post method
        String curp = sp.getString("CURP","[curp]");
        String url = getString(R.string.dbAPI) + "getVisits.php/";

        if(curp.equals("[curp]")){
            DialogFragment dialogo = new SessionErrorDialog();
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
                            if(status){
                                for(int i = 1; i < keys.length(); i++){
                                    JSONObject obj = jsonobj.getJSONObject(keys.getString(i));
                                    id = obj.getInt("id");
                                    fecha = Date.valueOf(obj.getString("fecha"));
                                    nombre = obj.getString("nombre");
                                    paterno = obj.getString("paterno");
                                    materno = obj.getString("materno");
                                    medio = obj.getString("medio");
                                    placas = obj.getString("placas");

                                    Visit nueva = new Visit(id,fecha,nombre,paterno,materno,medio,placas);
                                    vadapter.add(nueva);
                                }
                            }else{
                                emptyListMessage.setVisibility(TextView.VISIBLE);
                            }
                        }catch(Exception e){
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }else{
                        //No hay datos guardados
                        emptyListMessage.setVisibility(TextView.VISIBLE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Poner dialogo de error
                    DialogFragment dialogo = new ComErrorDialog();
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

    void updateList(){
        for( Visit v : getEntradas()) {
            vadapter.add(v);
        }
    }
}
