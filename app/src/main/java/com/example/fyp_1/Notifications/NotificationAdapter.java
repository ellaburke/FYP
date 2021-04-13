package com.example.fyp_1.Notifications;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ExampleViewHolder> {

    private List<Notification> mNotifications;
    private Context mContext;
    private OnListingListener mOnListingListener;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //notification content
        public TextView notificationTypeTitle;
        public TextView noticationItemName;
        public TextView notificationPhoneNumber;
        public ImageView mImageView;
        public RelativeLayout cardLayout;
        public CardView cardView;
        OnListingListener mOnListingListener;


        public ExampleViewHolder(@NonNull View itemView, OnListingListener onListingListener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.notificationImage1);
            notificationTypeTitle = itemView.findViewById(R.id.requestOrApprovalOrDeclineTV1);
            noticationItemName = itemView.findViewById(R.id.listingTitleNotification1);
            notificationPhoneNumber = itemView.findViewById(R.id.listingPhoneNumber);
            cardLayout = itemView.findViewById(R.id.notificationRelLayout1);
            cardView = itemView.findViewById(R.id.notificationCardView1);
            mOnListingListener = onListingListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mOnListingListener.onNotificationClick(getAdapterPosition());
        }
    }

    public interface OnListingListener {
        void onNotificationClick(int position);


    }


        public NotificationAdapter(Context context, ArrayList<Notification> userNotifications, OnListingListener onListingListener) {
            //super();
            mContext = context;
            mNotifications = userNotifications;
            this.mOnListingListener = onListingListener;
        }

        @NonNull
        @Override
        public NotificationAdapter.ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.notification_approval_decline_card, parent, false);
            return new NotificationAdapter.ExampleViewHolder(v, mOnListingListener);
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationAdapter.ExampleViewHolder holder, int position) {
            Notification currentNotification = mNotifications.get(position);
            holder.notificationTypeTitle.setText(currentNotification.getNotificationType());
            holder.noticationItemName.setText(currentNotification.getItemName());
            holder.notificationPhoneNumber.setText("Lister's Number: " + currentNotification.getRecieverPhoneNumber());
            Picasso.get()
                    .load(currentNotification.getItemURL())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(holder.mImageView);

            int grey = Color.parseColor(	"#CCA9A9A9");
            if(holder.notificationTypeTitle.getText().toString().equals("Declined")) {
                holder.cardLayout.setBackgroundColor(grey);
                holder.mImageView.setColorFilter(grey);
            }



        }

        @Override
        public int getItemCount() {
            return mNotifications.size();
        }
    }


