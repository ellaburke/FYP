package com.example.fyp_1.Notifications;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_1.R;
import com.example.fyp_1.model.Notification;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RequestNotificationAdapter extends RecyclerView.Adapter<RequestNotificationAdapter.ExampleViewHolder> {

    private List<Notification> mNotifications;
    private Context mContext;
    private OnListingListener mOnListingListener;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //notification content
        public TextView notificationTypeTitle;
        public TextView noticationItemName;
        public TextView notificationCollectorName;
        public TextView notificationRequestName;
        public Button approveBtn;
        public Button declineBtn;
        public ImageView mImageView;
        public RelativeLayout cardLayout;
        public CardView cardView;
        OnListingListener mOnListingListener;

        public ExampleViewHolder(@NonNull View itemView, OnListingListener onListingListener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.notificationImage);
            notificationTypeTitle = itemView.findViewById(R.id.requestOrApprovalOrDeclineTV);
            noticationItemName = itemView.findViewById(R.id.listingTitleNotification);
            notificationCollectorName = itemView.findViewById(R.id.collectorNameTV);
            notificationRequestName = itemView.findViewById(R.id.requesterName);

            approveBtn = itemView.findViewById(R.id.acceptBtnNotifcation);
            declineBtn = itemView.findViewById(R.id.declineBtnNotifcation);
            cardLayout = itemView.findViewById(R.id.notificationRelLayout);
            cardView = itemView.findViewById(R.id.notificationCardView);
            mOnListingListener = onListingListener;

            approveBtn.setOnClickListener(this);
            declineBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.acceptBtnNotifcation:
                    mOnListingListener.onListingApprovalClick(this.getAdapterPosition());

                    break;
                case R.id.declineBtnNotifcation:
                    mOnListingListener.onListingDeclineClick(this.getAdapterPosition());
            }
        }
    }

    public interface OnListingListener {
        void onListingApprovalClick(int position);

        void onListingDeclineClick(int position);

    }

    public RequestNotificationAdapter(Context context, ArrayList<Notification> userNotifications, OnListingListener onListingListener) {
        //super();
        mContext = context;
        mNotifications = userNotifications;
        this.mOnListingListener = onListingListener;
    }

    @NonNull
    @Override
    public RequestNotificationAdapter.ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.notification_card, parent, false);
        return new RequestNotificationAdapter.ExampleViewHolder(v, mOnListingListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestNotificationAdapter.ExampleViewHolder holder, int position) {
        Notification currentNotification = mNotifications.get(position);
        holder.notificationTypeTitle.setText(currentNotification.getNotificationType());
        holder.noticationItemName.setText(currentNotification.getItemName());
        holder.notificationRequestName.setText("Requested By: " + currentNotification.getSenderCollectorName());
        Picasso.get()
                .load(currentNotification.getItemURL())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.mImageView);

        if(currentNotification.getListingState().equals("Approved")) {
            holder.approveBtn.setVisibility(View.INVISIBLE);
            holder.declineBtn.setVisibility(View.INVISIBLE);
            holder.notificationTypeTitle.setText("Request Approved");
            //holder.notificationCollectorName.setVisibility(View.VISIBLE);
            holder.notificationRequestName.setText("Collector: " + currentNotification.getSenderCollectorName());
        }

        int grey = Color.parseColor(	"#CCA9A9A9");
        if(currentNotification.getListingState().equals("Declined")) {
            holder.approveBtn.setVisibility(View.INVISIBLE);
            holder.declineBtn.setVisibility(View.INVISIBLE);
            holder.notificationTypeTitle.setText("Request Declined");
            holder.cardLayout.setBackgroundColor(grey);
            holder.mImageView.setColorFilter(grey);
        }


    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

}
