package com.hdpsolution.khunganhtrangsach;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hdpsolution.khunganhtrangsach.Adapter.ListAdapter;
import com.hdpsolution.khunganhtrangsach.ads.MyBaseActivityWithAds;

import java.util.ArrayList;
import java.util.List;

import static com.hdpsolution.khunganhtrangsach.Contains.FullSreen;

public class ListImageActivity extends MyBaseActivityWithAds implements ListAdapter.ItemClickListener{

    private ListAdapter mAdapter;
    private RecyclerView recyclerView;
    private List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_image);

        FullSreen(getWindow());
        CheckInternet();
        LayDuLieu();

        recyclerView = findViewById(R.id.lvImage);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mAdapter = new ListAdapter(data,this);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void CheckInternet() {
        if(!Contains.isNetworkConnected(this)){

            final AlertDialog.Builder dialogsave = new AlertDialog.Builder(this);
            dialogsave.setMessage(getString(R.string.notnetwork));
            dialogsave.setNegativeButton(getString(R.string.turn), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS))
                    ;}
            });
            dialogsave.setPositiveButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }});
            dialogsave.show();
        }
    }

    @Override
    public void onItemClick(View view, int position) {

        Intent intent = new Intent();
        intent.putExtra(Contains.KEY_RESULT_PHOTO_FRAME,data.get(position).toString());
        setResult(Activity.RESULT_OK,intent);
        finish();

    }


    private void LayDuLieu(){

        data = new ArrayList<>();
        Integer a= 1;
        do {
            data.add("khunganhtrangsach/Frame "+a+".png");
            a++;
        } while (a<71);



//
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("khung");
//
//
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
//                    data.add(String.valueOf(snapshot.getValue()));
//                }
//                mAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }
}
