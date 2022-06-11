package LogicController;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.view.*;
import android.widget.*;
import com.example.comsafe.R;

public class VisitAdapter extends ArrayAdapter<Visit>{
    private int visitLayoutResource;

    public VisitAdapter(final Context c,final int entLayRec){
        super(c,0);
        this.visitLayoutResource = entLayRec;
    }

    @Override
    public View getView(final int i, final View v, final ViewGroup vg){
        final View v2 = getWorkingView(v);
        final ViewHolder vh = getViewHolder(v2);
        final Visit visit = getItem(i);
        vh.tvNombre.setText(visit.getName()+" "+visit.getPatern()+" "+visit.getMatern());
        vh.tvFecha.setText(visit.getDate().toString());
        vh.tvId.setText(visit.getId()+"");
        return v2;
    }

    private View getWorkingView(final View v3){
        View workingView = null;
        if (null == v3){
            final Context c2 = getContext();
            final LayoutInflater inflater = (LayoutInflater) c2.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            workingView = inflater.inflate(visitLayoutResource,null);
        }else{
            workingView = v3;
        }
        return workingView;
    }

    private VisitAdapter.ViewHolder getViewHolder(final View workingView){
        final Object tag = workingView.getTag();
        VisitAdapter.ViewHolder vh = null;

        if(null == tag || !(tag instanceof VisitAdapter.ViewHolder)){
            vh = new VisitAdapter.ViewHolder();
            vh.tvNombre = (TextView) workingView.findViewById(R.id.tvNombreFila);
            vh.tvFecha = (TextView) workingView.findViewById(R.id.tvFechaFila);
            vh.tvId = (TextView) workingView.findViewById(R.id.tvIdFila);
            workingView.setTag(vh);
        }else{
            vh = (VisitAdapter.ViewHolder) tag;
        }
        return vh;
    }

    private static class ViewHolder{
        public TextView tvNombre,tvFecha,tvId;
    }
}
