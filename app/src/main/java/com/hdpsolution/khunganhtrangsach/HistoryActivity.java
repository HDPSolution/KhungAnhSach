package com.hdpsolution.khunganhtrangsach;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.hdpsolution.khunganhtrangsach.Adapter.ListHistoryAdapter;
import com.hdpsolution.khunganhtrangsach.Model.Image;
import com.hdpsolution.khunganhtrangsach.ads.MyBaseActivityWithAds;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import static com.hdpsolution.khunganhtrangsach.Contains.FullSreen;

public class HistoryActivity extends MyBaseActivityWithAds implements ListHistoryAdapter.ItemClickListener,ListHistoryAdapter.ItemLongClickListener {
    private ListHistoryAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Image> data;
    private static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        FullSreen(getWindow());
        ToolbarSetup();
        data = new ArrayList<>();
        LayDuLieu();
        recyclerView = findViewById(R.id.lvImageHistory);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new ListHistoryAdapter(data, this);
        mAdapter.setClickListener(this);
        mAdapter.setLongClickListener(this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


    }

    private void LayDuLieu() {
        db = Contains.Docfiledb(Contains.DATABASE, this);
        Cursor datas = db.query(Contains.TABLE_DATABASE, null, null, null, null, null, null);
        datas.moveToFirst();
        try {
            do {
                File file = new File(URI.create(datas.getString(1)));
                if (file.canRead()) {
                    data.add(new Image(datas.getInt(0), datas.getString(1), datas.getString(2)));
                } else {
                    XoaAnhRong(datas.getInt(0));
                }

            } while (datas.moveToNext());
        } catch (CursorIndexOutOfBoundsException e) {

            final AlertDialog.Builder dialogsave = new AlertDialog.Builder(this);
            dialogsave.setMessage(getString(R.string.text_dialog_not_edit));
            dialogsave.setNegativeButton(getString(R.string.agree), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ChonHinh();
                }
            });
            dialogsave.setPositiveButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialogsave.show();
        }


    }

    public static void XoaAnhRong(Integer id) {
        db.execSQL("DELETE FROM " + Contains.TABLE_DATABASE + " WHERE Id = '" + id + "'");
    }
    @Override
    public void onItemClick(View view, int position) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(data.get(position).getUrigalery());
        intent.setDataAndType(uri, "image/*");
        startActivity(Intent.createChooser(intent, getString(R.string.openwith)));

    }
    Integer posi;
    @Override
    public void onItemLongClick(View view, int position) {
        posi= position;
        final AlertDialog.Builder dialogsave = new AlertDialog.Builder(this);
        dialogsave.setMessage(getString(R.string.wantdelete));
        dialogsave.setNegativeButton(getString(R.string.agree), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Cursor datas = db.query(Contains.TABLE_DATABASE, null, null, null, null, null, null);
                datas.move(posi+1);
                File file = new File(URI.create(datas.getString(1)));
                if(file.delete()){
                    Toast.makeText(HistoryActivity.this, getString(R.string.done), Toast.LENGTH_SHORT).show();
                    if (data.size() > 0) {
                        data.clear();
                        LayDuLieu();
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(HistoryActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                }


            }
        });
        dialogsave.setPositiveButton(getString(R.string.no),null);
        dialogsave.show();
    }

    private void ToolbarSetup() {
        Toolbar tb = findViewById(R.id.toolbarss);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.photohaveframe));
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (data.size() > 0) {
            data.clear();
            LayDuLieu();
            mAdapter.notifyDataSetChanged();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(Contains.REQUEST_CODE_PICK_GALERY == requestCode){
            if(resultCode == Activity.RESULT_OK && data !=null){
                Intent intent = new Intent(this,EditActivity.class);
                intent.putExtra(Contains.KEY_PUT_BITMAP,data.getDataString());
                startActivity(intent);
                finish();
            }
        }
    }
    private void ChonHinh() {

        Intent ChonHinh = new Intent();
        ChonHinh.setType("image/*");
        ChonHinh.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(ChonHinh, getString(R.string.pickimage)), Contains.REQUEST_CODE_PICK_GALERY);

    }

}