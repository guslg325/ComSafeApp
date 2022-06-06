package ViewController;

import static android.content.ClipData.newPlainText;
import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.comsafe.R;
import android.content.ClipboardManager;

public class ProfileFragment extends Fragment implements OnClickListener {
    private SharedPreferences sp;
    private Button jbtnLogout, jbtnEditProfile;
    private ImageButton jbtnCopy;
    private TextView tvCURP,tvNombre,tvContrato,tvSexo,tvFecha,tvCURP2,tvFecha2,tvSexo2;
    private View rootView;
    private String nombre,paterno,materno,tipo,contrato,curp,fechaNac,sexo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        sp = rootView.getContext().getSharedPreferences("preferencias", Context.MODE_PRIVATE);

        nombre = sp.getString("nombre","Nombre de usuario");
        curp = sp.getString("CURP","[Curp no registrada]");
        paterno = sp.getString("paterno", "apPat");
        materno = sp.getString("materno", "apMat");
        contrato = sp.getString("contrato", "contrato");
        fechaNac = sp.getString("nacimiento", "fechaNac");
        tipo = sp.getString("tipo", "tipo");
        sexo = sp.getString("sexo", "sexo");

        jbtnLogout = rootView.findViewById(R.id.btnLogout);
        jbtnEditProfile = rootView.findViewById(R.id.btnEditProfile);
        jbtnCopy = rootView.findViewById(R.id.btnCopy);
        tvNombre = rootView.findViewById(R.id.xtvNombreProfile);
        tvCURP = rootView.findViewById(R.id.xtvInfo2);
        tvCURP2 = rootView.findViewById(R.id.xtvCURP);
        tvContrato = rootView.findViewById(R.id.xtvInfo1);
        tvFecha = rootView.findViewById(R.id.xtvInfo3);
        tvFecha2 = rootView.findViewById(R.id.xtvFechNac);
        tvSexo = rootView.findViewById(R.id.xtvInfo4);
        tvSexo2 = rootView.findViewById(R.id.xtvSexo);

        tvNombre.setText(nombre+" "+paterno+" "+materno);
        tvCURP2.setText(curp);
        tvContrato.setText(contrato);
        tvFecha2.setText(fechaNac);
        tvSexo2.setText(sexo);
        jbtnLogout.setOnClickListener(this);
        jbtnEditProfile.setOnClickListener(this);
        jbtnCopy.setOnClickListener(this);

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
    public void onClick(View view) {
        if(view.getId()==R.id.btnLogout){
            //Cerrar sesion
            //Update session parameters
            SharedPreferences.Editor miEditor = sp.edit();
            miEditor.putBoolean("status", false);
            miEditor.putString("CURP", "");
            miEditor.putString("nombre", "");
            miEditor.putString("paterno", "");
            miEditor.putString("materno", "");
            miEditor.putString("contrato", "");
            miEditor.putString("nacimiento", "");
            miEditor.putString("tipo", "");
            miEditor.putString("sexo", "");
            miEditor.commit();
            //Go to login activity
            Intent itn = new Intent(rootView.getContext(),MainActivity.class);
            startActivity(itn);
            this.getActivity().finish();
        }
        if(view.getId()==R.id.btnEditProfile){
            Intent itn = new Intent(rootView.getContext(), ProfileEditActivity.class);
            startActivity(itn);
        }
        if(view.getId()==R.id.btnCopy){
            //Copiar num contrato al portapapeles
            ClipboardManager clipboard = (ClipboardManager)  ((AppCompatActivity) getActivity()).getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = newPlainText("Contrato",tvContrato.getText().toString());
            clipboard.setPrimaryClip(clip);

            Toast.makeText(getContext(),R.string.ContratoCopied,Toast.LENGTH_LONG).show();
        }
    }
}
