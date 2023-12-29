package com.example.nt118;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.nt118.Adapter.NotificationManagerAdapter;
import com.example.nt118.Class.EntryWrapper;
import com.example.nt118.Class.NhanVien;
import com.example.nt118.Class.NotificationManagerData;
import com.google.gson.Gson;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationManager#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationManager extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String manv;
    private String mk;
    private String phban;

    public NotificationManager() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationManager.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationManager newInstance(String param1, String param2, String param3) {
        NotificationManager fragment = new NotificationManager();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }
    private NhanVien nhanVien;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            manv = getArguments().getString(ARG_PARAM1);
            mk = getArguments().getString(ARG_PARAM2);
            phban = getArguments().getString(ARG_PARAM3);
            nhanVien = new NhanVien();
            nhanVien.setMANV(manv);
            nhanVien.setMK(mk);
            nhanVien.setPHBAN(phban);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification_manager, container, false);
    }
    private RecyclerView recyclerView;
    private NotificationManagerAdapter adapter;
    private List<NotificationManagerData> dataList;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((Button) view.findViewById(R.id.btn_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddNotification.class);
                intent.putExtra("nhanVien", nhanVien);
                startActivity(intent);
            }
        });
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataList = new ArrayList<>();
        adapter = new NotificationManagerAdapter(dataList);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // Không cần xử lý sự kiện di chuyển
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    // Hiển thị dialog xác nhận khi lướt sang trái
                    showDeleteConfirmationDialog(viewHolder.getAdapterPosition());
                }
                adapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }
        };

        new ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(recyclerView);

    }
    private void showDeleteConfirmationDialog(final int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xác nhận")
                .setMessage("Bạn muốn xóa thông báo này?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RemoveNotification(position);
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
    @Override
    public void onResume() {
        super.onResume();

        GetNotificationsForEmployee();
    }
    private void RemoveNotification(int position){
        NotificationManagerData data = new NotificationManagerData();
        data.setId(dataList.get(position).getId());
        String json = new Gson().toJson(new EntryWrapper(new AbstractMap.SimpleEntry(nhanVien, data)));

        Server server = new Server();
        server.postAsync("https://s3.cazo-dev.net/NT118/api/Trph/DeleteNotification", json, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(Map.Entry<String, Integer> response) {
                if (response.getValue() == 200){
                    Toast.makeText(getContext(), "Xóa thông báo thành công!", Toast.LENGTH_SHORT).show();
                    GetNotificationsForEmployee();
                } else showErrorRequest(response.getValue().toString() + " " + response.getKey());
            }
        });
    }
    private void GetNotificationsForEmployee(){
        String json = new Gson().toJson(nhanVien);
        Server server = new Server();

        server.postAsync("https://s3.cazo-dev.net/NT118/api/Trph/GetNotificationsForEmployee", json, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(Map.Entry<String, Integer> response) {
                dataList.clear();
                if (response.getValue() == 200){

                    for (NotificationManagerData res : NotificationManagerData.convertJsonToList(response.getKey())){

                        dataList.add(res);
                    }
                } else showErrorRequest(response.getValue().toString() + " " + response.getKey());
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void showErrorRequest(String mess){
        Toast.makeText(getContext(), "Response code: " + mess, Toast.LENGTH_SHORT).show();
    }
}