package com.cps.staffportal.geofencingattendance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cps.staffportal.R;

import java.util.ArrayList;

public class LOCPunchDetailsLVAdapter extends RecyclerView.Adapter<LOCPunchDetailsLVAdapter.ViewHolder> {

    private static ArrayList<String> punch_list=new ArrayList<String>();
    private int itemLayout;

    public LOCPunchDetailsLVAdapter(ArrayList<String> punch_list, int itemLayout){
        this.punch_list = punch_list;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        String item = punch_list.get(position);
        holder.tvPunchDatetime.setText(item);
    }

    @Override
    public int getItemCount(){
        return punch_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPunchDatetime;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPunchDatetime = (TextView) itemView.findViewById(R.id.txtPunchDatetime);
//            final SharedPreferences loginsession = itemView.getContext().getSharedPreferences("SessionLogin", 0);
//            this.itemView.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v){
//                    String item = punch_list.get(getPosition());
//                    String[] strColumns = item.split("##");
//                    String strPdfFileName = strColumns[5].toString().trim();
//                    Context context = v.getContext();
//                    Intent intent;
//                    intent = new Intent(context, ViewPaySlip.class);
//                    intent.putExtra("officeid", strColumns[0]);
//                    intent.putExtra("employeeid",strColumns[1]);
//                    intent.putExtra("paystructureid",strColumns[2]);
//                    intent.putExtra("payperiodid",strColumns[3]);
//                    intent.putExtra("pdffilename", strPdfFileName);
//                    context.startActivity(intent);
//                }
//            });
        }
    }
}
