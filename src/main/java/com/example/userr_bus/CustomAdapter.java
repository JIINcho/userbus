package com.example.userr_bus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    private ArrayList<Reservation> arrayList;
    private Context context;
    private Reservation reservations;
    private FirebaseFirestore db;
    private static final String TAG = "CustomAdapter";

    public CustomAdapter(ArrayList<Reservation> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        this.reservations = reservations;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Reservation reservation = arrayList.get(position);
        holder.place.setText(arrayList.get(position).getPlace());
        holder.route.setText(arrayList.get(position).getRoute());
        holder.time.setText(arrayList.get(position).getTime());
        //Timestamp로 저장되어있는 날짜데이터는 String으로 바꿔서 변환
        String formattedDate;
        try {
            Timestamp reservationDate = arrayList.get(position).getReservationDate();
            if (reservationDate != null) {
                Date date = reservationDate.toDate();
                formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
                holder.reservationDate.setText(formattedDate);
            } else {
                holder.reservationDate.setText("날짜 정보 없음");
            }
        } catch (Exception e) {
            Log.e("CustomAdapter", "Date conversion error: " + e.getMessage());
            holder.reservationDate.setText("날짜 변환 오류");
        }
    }

    @Override
    public int getItemCount() {
        return (arrayList != null? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        Button btnCancel = itemView.findViewById(R.id.btnCancel_1);
        TextView route;
        TextView place;
        TextView time;
        TextView reservationDate;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.place = itemView.findViewById(R.id.tv_place);
            this.route = itemView.findViewById(R.id.tv_route);
            this.time = itemView.findViewById(R.id.tv_time);
            this.reservationDate = itemView.findViewById(R.id.tv_reservationDate);

            db = FirebaseFirestore.getInstance();

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Reservation reservation = arrayList.get(position);
                    showCancelComfirmationDialog(reservation);
                }

                private void showCancelComfirmationDialog(Reservation reservation) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("예약 취소 확인")
                            .setMessage("정말 예약을 취소하시겠습니까?")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteReservationFromFirebase(reservation);
                                }
                            })
                            .setNegativeButton("아니오", null)
                            .show();
                }

                private void deleteReservationFromFirebase(Reservation reservation) {
                    db.collection("Reservation")
                            .document(reservation.getDocumentId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //데이터 소스에서 해당 예약 내역 제거
                                    int position = arrayList.indexOf(reservation);
                                    arrayList.remove(position);
                                    //RecyclerView에 데이터 변경 알림
                                    notifyItemRemoved(position);

                                    Toast.makeText(context, "예약이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "예약 취소에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "Error deleting reservation: " + e.getMessage());
                                }
                            });
                }
            });
        }
    }

}