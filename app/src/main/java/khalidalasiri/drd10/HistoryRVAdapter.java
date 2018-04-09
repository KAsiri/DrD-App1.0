package khalidalasiri.drd10;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoryRVAdapter extends RecyclerView.Adapter<HistoryRVAdapter.VH> {

    Context context;
    List<Report> reportsList;

    public HistoryRVAdapter(Context context, List<Report> reportsList) {
        this.context = context;
        this.reportsList = reportsList;
    }

    @Override
    public HistoryRVAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_items, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(HistoryRVAdapter.VH holder, int position) {
        holder.tvBP.setText(reportsList.get(position).getBloodPressure());
        holder.tvBG.setText(reportsList.get(position).getBloodGlucoseAnalysis());
        holder.tvHR.setText(reportsList.get(position).getHeartRate());
        holder.reportID = reportsList.get(position).getReportID();
        // get Date and Time
        SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat tf = new SimpleDateFormat("hh:mm:ss a");
        Date dateAndTime = null;
        String date = null;
        String time = null;
        try {
            dateAndTime = dateFormat.parse(reportsList.get(position).getReportDate());
            Log.d("print",dateAndTime.toString());
            date = df.format(dateAndTime.getTime());
            time = tf.format(dateAndTime.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tvDate.setText(date);
        holder.tvTime.setText(time);

    }

    @Override
    public int getItemCount() {
        return reportsList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView tvBP;
        TextView tvBG;
        TextView tvHR;
        TextView tvDate;
        TextView tvTime;
        String reportID;

        public VH(View itemView) {
            super(itemView);
            tvBP = itemView.findViewById(R.id.tvBP);
            tvBG = itemView.findViewById(R.id.tvBG);
            tvHR = itemView.findViewById(R.id.tvHR);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
