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

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationManagerViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private OnItemClickListener listener;
    private List<NotificationManagerData> notificationList;

    public NotificationAdapter(List<NotificationManagerData> notificationList) {
        this.notificationList = notificationList;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public NotificationManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationManagerViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationManagerViewHolder holder, int position) {
        NotificationManagerData notification = notificationList.get(position);
        holder.tvDate.setText(notification.getDate());
        holder.tvContent.setText("Nội dung: " + notification.getContent());
        holder.tvSeen.setText("Số người đã xem: " + notification.getSeen());
        if (notification.isSeen())
            holder.tvYouSeen.setText("Bạn đã xem");
        else holder.tvYouSeen.setText("Bạn chưa xem");
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    static class NotificationManagerViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvContent, tvYouSeen, tvSeen;

        NotificationManagerViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvYouSeen = itemView.findViewById(R.id.tv_you_seen);
            tvSeen = itemView.findViewById(R.id.tv_seen);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
