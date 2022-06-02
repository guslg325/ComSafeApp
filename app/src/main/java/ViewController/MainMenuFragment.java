package ViewController;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.comsafe.R;

public class MainMenuFragment extends Fragment implements OnClickListener{
    SharedPreferences sp;
    TextView jtvBienvenida;
    Button btnMoreVisitas, btnMoreReservas,btnMoreAutomoviles;

    public MainMenuFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);

        jtvBienvenida = rootView.findViewById(R.id.xtvBienvenida);
        btnMoreVisitas = rootView.findViewById(R.id.btnMoreVisitas);
        btnMoreReservas = rootView.findViewById(R.id.btnMoreReservas);
        btnMoreAutomoviles = rootView.findViewById(R.id.btnMoreAutomoviles);

        sp = getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);

        jtvBienvenida.setText(jtvBienvenida.getText()+" "+sp.getString("nombre","[usuario]"));
        btnMoreVisitas.setOnClickListener(this);
        btnMoreReservas.setOnClickListener(this);
        btnMoreAutomoviles.setOnClickListener(this);

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
        Fragment fragment = null;
        String title = getString(R.string.tvTitleMainMenu);
        switch (v.getId()){
            case R.id.btnMoreVisitas:
                fragment = new VisitasFragment();
                title = getString(R.string.tvTitleVisitas);
                break;
            case R.id.btnMoreReservas:
                fragment = new ReservasFragment();
                title = getString(R.string.tvTitleReservas);
                break;
            case R.id.btnMoreAutomoviles:
                fragment = new AutomovilesFragment();
                title = getString(R.string.tvTitleAutomoviles);
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            // set the toolbar title from fragment
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        }
    }
}
