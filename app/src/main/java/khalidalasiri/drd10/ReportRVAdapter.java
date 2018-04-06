package khalidalasiri.drd10;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ReportRVAdapter extends RecyclerView.Adapter<ReportRVAdapter.VH> {

    Context context;
    List<Report> reportsList;

    public ReportRVAdapter(Context context, List<Report> reportsList) {
        this.context = context;
        this.reportsList = reportsList;
    }

    @Override
    public ReportRVAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.report_items, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(ReportRVAdapter.VH holder, int position) {
        holder.tvBP.setText(reportsList.get(position).getBloodPressure());
        holder.tvBG.setText(reportsList.get(position).getBloodGlucoseAnalysis());
        holder.tvHR.setText(reportsList.get(position).getHeartRate());
        holder.reportID = reportsList.get(position).getReportID();
        holder.reportDate = reportsList.get(position).getReportDate();

    }

    @Override
    public int getItemCount() {
        return reportsList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView tvBP;
        TextView tvBG;
        TextView tvHR;
        String reportID;
        String reportDate;

        public VH(View itemView) {
            super(itemView);
            tvBP = itemView.findViewById(R.id.tvBP);
            tvBG = itemView.findViewById(R.id.tvBG);
            tvHR = itemView.findViewById(R.id.tvHR);
        }
    }
}
