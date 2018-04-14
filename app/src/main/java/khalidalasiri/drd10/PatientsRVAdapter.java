package khalidalasiri.drd10;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PatientsRVAdapter extends RecyclerView.Adapter<PatientsRVAdapter.VH> {

    Context context;
    List<Patients> patientsList ;

    public PatientsRVAdapter(Context context, List<Patients> patientsList) {
        this.context = context;
        this.patientsList = patientsList;
    }

    @Override
    public PatientsRVAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.patients_items, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(PatientsRVAdapter.VH holder, int position) {
        holder.tvPID.setText(patientsList.get(position).getPatientID());
        holder.tvPN.setText(patientsList.get(position).getPatientName());
        holder.tvPS.setText(patientsList.get(position).getSex());
        holder.tvDT.setText(patientsList.get(position).getTypeOfDiabetes());

    }

    @Override
    public int getItemCount() {
        return patientsList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView tvPID;
        TextView tvPN;
        TextView tvPS;
        TextView tvDT;
        String reportID;
        String reportDate;

        public VH(View itemView) {
            super(itemView);
            tvPID = itemView.findViewById(R.id.tvPID);
            tvPN = itemView.findViewById(R.id.tvPN);
            tvPS = itemView.findViewById(R.id.tvPS);
            tvDT = itemView.findViewById(R.id.tvDT);
        }
    }
}
