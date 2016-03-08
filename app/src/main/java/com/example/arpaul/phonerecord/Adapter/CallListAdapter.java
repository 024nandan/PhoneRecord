package com.example.arpaul.phonerecord.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arpaul.phonerecord.DataAccess.PhoneRecordCPConstants;
import com.example.arpaul.phonerecord.DataObject.CallDetailsDO;
import com.example.arpaul.phonerecord.R;
import com.example.arpaul.phonerecord.Utilities.CalendarUtils;

import java.util.ArrayList;

/**
 * Created by ARPaul on 04-03-2016.
 */
public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CallDetailsDO> arrCallDetails;

    public CallListAdapter(Context context, ArrayList<CallDetailsDO> arrCallDetails) {
        this.context=context;
        this.arrCallDetails = arrCallDetails;
    }

    public void refresh(ArrayList<CallDetailsDO> arrCallDetails) {
        this.arrCallDetails = arrCallDetails;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_detail_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CallDetailsDO objCallDetailsDO = arrCallDetails.get(position);
        holder.tvContactNo.setText(objCallDetailsDO.contactNumber);
        holder.tvCallTime.setText(CalendarUtils.getCommaFormattedDateTime(objCallDetailsDO.callTime));

        if(objCallDetailsDO.contactType == PhoneRecordCPConstants.get_Contact_Type_Call())
            holder.ivContactType.setImageResource(R.drawable.call);
        else if(objCallDetailsDO.contactType == PhoneRecordCPConstants.get_Contact_Type_SMS())
            holder.ivContactType.setImageResource(R.drawable.sms);

    }

    @Override
    public int getItemCount() {
        if(arrCallDetails != null)
            return arrCallDetails.size();

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvContactNo;
        public final TextView tvCallTime;
        public final ImageView ivContactType;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvContactNo = (TextView) view.findViewById(R.id.tvContactNo);
            ivContactType = (ImageView) view.findViewById(R.id.ivContactType);
            tvCallTime = (TextView) view.findViewById(R.id.tvCallTime);
        }

        @Override
        public String toString() {
            return "";
        }
    }
}
