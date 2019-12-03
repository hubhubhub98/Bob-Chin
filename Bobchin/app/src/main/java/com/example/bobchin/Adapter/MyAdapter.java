package com.example.bobchin.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bobchin.BobChin;
import com.example.bobchin.Fragment.Meetings;
import com.example.bobchin.MeetInfo;
import com.example.bobchin.R;
import com.example.bobchin.activity_chatroom;
import com.example.bobchin.select_meeting;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView address;
        TextView time;
        TextView person;
        TextView age;
        MeetInfo data;

        MyViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.meeting_title);
            address = view.findViewById(R.id.address);
            time = view.findViewById(R.id.meet_time);
            person = view.findViewById(R.id.meet_person);
            age=view.findViewById(R.id.age);

        }
    }

    private ArrayList<MeetInfo> MeetInfoArrayList;
    public MyAdapter(ArrayList<MeetInfo> MeetInfoArrayList){
        this.MeetInfoArrayList = MeetInfoArrayList;
    }

    @Override // oncreateViewHolder는 아이템레이아웃을 뷰홀더에 고정시키는 역할을 함.
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);

        return new MyViewHolder(v);
    }

    @Override // onBindViewHolder는 뷰홀더에 데이터를 입력시킴.
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.title.setText(MeetInfoArrayList.get(position).title);
        myViewHolder.address.setText(MeetInfoArrayList.get(position).address);
        myViewHolder.time.setText(MeetInfoArrayList.get(position).time);
        myViewHolder.person.setText(MeetInfoArrayList.get(position).person);
        myViewHolder.age.setText(MeetInfoArrayList.get(position).age);
        myViewHolder.data = MeetInfoArrayList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Toast.makeText(v.getContext(),myViewHolder.data.meetid,Toast.LENGTH_LONG).show();
                int position = holder.getAdapterPosition();
                System.out.println(enteredMeet(position));
                Intent intent = new Intent(v.getContext(), select_meeting.class);
                intent.putExtra("class",myViewHolder.data);
                intent.putExtra("entered", enteredMeet(position));
                ((Activity) v.getContext()).startActivityForResult(intent,1);
            }
        });
    }

    @Override // 사이즈 확인
    public int getItemCount() {
        return MeetInfoArrayList.size();
    }

    public boolean enteredMeet(int i){
        for(int x = 1; x < MeetInfoArrayList.get(i).users.length; x++){
            if(MeetInfoArrayList.get(i).users[x].equals(MeetInfoArrayList.get(i).user)){
                return true;
            }
        }
        return false;
    }

    public MeetInfo getNthItem(int i){
        return MeetInfoArrayList.get(i);
    }
}
