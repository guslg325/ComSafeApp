package LogicController;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.view.*;
import android.widget.*;
import com.example.comsafe.R;

public class CarAdapter extends ArrayAdapter<Car>{
    private int carLayoutResource;

    public CarAdapter(final Context c,final int entLayRec){
        super(c,0);
        this.carLayoutResource = entLayRec;
    }

    @Override
    public View getView(final int i, final View v, final ViewGroup vg){
        final View v2 = getWorkingView(v);
        final ViewHolder vh = getViewHolder(v2);
        final Car car = getItem(i);
        vh.tvMarca.setText(car.getBrand());
        vh.tvModelo.setText(car.getModel());
        vh.tvPlacas.setText(car.getPlates());
        return v2;
    }

    private View getWorkingView(final View v3){
        View workingView = null;
        if (null == v3){
            final Context c2 = getContext();
            final LayoutInflater inflater = (LayoutInflater) c2.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            workingView = inflater.inflate(carLayoutResource,null);
        }else{
            workingView = v3;
        }
        return workingView;
    }

    private ViewHolder getViewHolder(final View workingView){
        final Object tag = workingView.getTag();
        ViewHolder vh = null;

        if(null == tag || !(tag instanceof ViewHolder)){
            vh = new ViewHolder();
            vh.tvMarca = (TextView) workingView.findViewById(R.id.tvMarcaFila);
            vh.tvModelo = (TextView) workingView.findViewById(R.id.tvModeloFila);
            vh.tvPlacas = (TextView) workingView.findViewById(R.id.tvPlacasFila);
            workingView.setTag(vh);
        }else{
            vh = (ViewHolder) tag;
        }
        return vh;
    }

    private static class ViewHolder{
        public TextView tvMarca,tvModelo,tvPlacas;
    }
}
