package com.example.nt118.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nt118.Class.NotificationManagerData;
import com.example.nt118.R;

import java.util.List;

public class NotificationManagerAdapter extends RecyclerView.Adapter<NotificationManagerAdapter.NotificationManagerViewHolder> {

    private List<NotificationManagerData> notificationList;

    public NotificationManagerAdapter(List<NotificationManagerData> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_manager, parent, false);
        return new NotificationManagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationManagerViewHolder holder, int position) {
        NotificationManagerData notification = notificationList.get(position);
        holder.tvDate.setText(notification.getDate());
        holder.tvContent.setText("Nội dung: " + notification.getContent());
        holder.tvReceived.setText("Số người đã nhận: " + notification.getReceived());
        holder.tvSeen.setText("Số người đã xem: " + notification.getSeen());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    static class NotificationManagerViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvContent, tvReceived, tvSeen;

        NotificationManagerViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvReceived = itemView.findViewById(R.id.tv_received);
            tvSeen = itemView.findViewById(R.id.tv_seen);
        }
    }
}
