package com.example.nt118;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.nt118.Adapter.NotificationAdapter;
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
 * Use the {@link Notification#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notification extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String manv;
    private String pass;

    public Notification() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Notification.
     */
    // TODO: Rename and change types and number of parameters
    public static Notification newInstance(String param1, String param2) {
        Notification fragment = new Notification();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private NhanVien nhanVien;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            manv = getArguments().getString(ARG_PARAM1);
            pass = getArguments().getString(ARG_PARAM2);
        }
        nhanVien = new NhanVien();
        nhanVien.setMANV(manv);
        nhanVien.setMK(pass);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);

    }
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationManagerData> dataList;
    private Handler handler;
    private Runnable updateTask;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataList = new ArrayList<>();
        adapter = new NotificationAdapter(dataList);
        recyclerView.setAdapter(adapter);

        updateData();

        adapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                updateStatusSeen(dataList.get(position).getId());
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Thông báo: " + dataList.get(position).getDate());
                builder.setMessage(dataList.get(position).getContent());

                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);

                dialog.show();
            }
        });
    }

    private void updateStatusReceived(int id){
        NotificationManagerData data = new NotificationManagerData();
        data.setId(id);

        String json = new Gson().toJson(new EntryWrapper(new AbstractMap.SimpleEntry(nhanVien, data)));

        Server server = new Server();

        server.postAsync("https://s3.cazo-dev.net/NT118/api/Home/InsertNgNhanTB", json, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(Map.Entry<String, Integer> response) {
                if (response.getValue() == 200){

                } //else showErrorRequest(response.getValue().toString() + " " + response.getKey());
            }
        });
    }
    private void updateStatusSeen(int id){
        NotificationManagerData data = new NotificationManagerData();
        data.setId(id);

        String json = new Gson().toJson(new EntryWrapper(new AbstractMap.SimpleEntry(nhanVien, data)));

        Server server = new Server();

        server.postAsync("https://s3.cazo-dev.net/NT118/api/Home/InsertNgXemTB", json, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(Map.Entry<String, Integer> response) {
                if (response.getValue() == 200){

                } //else showErrorRequest(response.getValue().toString() + " " + response.getKey());
            }
        });
    }
    private void updateData(){
        handler = new Handler();
        updateTask = new Runnable() {
            @Override
            public void run() {
                String json = new Gson().toJson(nhanVien);
                Server server = new Server();

                server.postAsync("https://s3.cazo-dev.net/NT118/api/NhanVien/GetNotificationsForEmployee", json, new Server.PostResponseListener() {
                    @Override
                    public void onPostCompleted(Map.Entry<String, Integer> response) {
                        if (response.getValue() == 200){
                            for (NotificationManagerData res : NotificationManagerData.convertJsonToList(response.getKey())){
                                updateDataList(res);
                            }
                        } else showErrorRequest(response.getValue().toString() + " " + response.getKey());
                    }
                });

                handler.postDelayed(this, 7000);
            }
        };
    }
    private void showErrorRequest(String mess){
        Toast.makeText(getContext(), "Response code: " + mess, Toast.LENGTH_SHORT).show();
    }
    public void updateDataList(NotificationManagerData data) {
        boolean found = false;

        for (NotificationManagerData item : dataList) {
            if (item.getId() == data.getId()) {
                found = true;
                boolean change = false;
                if (item.getReceived() != data.getReceived()) {
                    item.setReceived(data.getReceived());
                    change = true;
                }
                if (item.getSeen() != data.getSeen()) {
                    item.setSeen(data.getSeen());
                    change = true;
                }
                if (item.isSeen() != data.isSeen()) {
                    item.setSeen(data.isSeen());
                    change = true;
                }
                if (change) adapter.notifyDataSetChanged();
                break;
            }
        }

        if (!found) {
            dataList.add(data);
            adapter.notifyDataSetChanged();
            updateStatusReceived(data.getId());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFirstTime();
    }
    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(updateTask);
    }

    private void getDataFirstTime(){
        String json = new Gson().toJson(nhanVien);
        Server server = new Server();

        server.postAsync("https://s3.cazo-dev.net/NT118/api/NhanVien/GetNotificationsForEmployee", json, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(Map.Entry<String, Integer> response) {
                dataList.clear();
                if (response.getValue() == 200){

                    for (NotificationManagerData res : NotificationManagerData.convertJsonToList(response.getKey())){
                        dataList.add(res);
                        updateStatusReceived(res.getId());
                    }
                } else showErrorRequest(response.getValue().toString() + " " + response.getKey());
                adapter.notifyDataSetChanged();
                handler.post(updateTask);
            }
        });
    }
}