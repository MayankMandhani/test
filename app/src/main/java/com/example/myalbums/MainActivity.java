package com.example.myalbums;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Album> albumList=new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private String photoEncoded;
    public static int currentAlbum=0;
    private int REQUEST_CODE_PERMISSIONS = 101;
    private String permissionList[] = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
        initialiseRecyclerView();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_box,null);
                final EditText title = mView.findViewById(R.id.title);
                Button cancel = mView.findViewById(R.id.cancel);
                Button ok = mView.findViewById(R.id.ok);
                alert.setView(mView);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(title.getText()!=null&&!title.getText().toString().equals("")) {
                            albumList.add(new Album(title.getText().toString(), new ArrayList<String>()));
                            adapter.notifyDataSetChanged();
                            alertDialog.dismiss();
                        }
                        else{
                            title.setError("Title can not be empty!");
                        }
                    }
                });
                alertDialog.show();
            }
        });

        if(!permissionsGranted())
        {
            ActivityCompat.requestPermissions(this, permissionList, REQUEST_CODE_PERMISSIONS);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK
                && null != data) {
            // Get the Image from data
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        mArrayUri.add(uri);
                        String wholeID = DocumentsContract.getDocumentId(uri);

                        // Split at colon, use second item in the array

                        String id = wholeID.split(":").length>1?wholeID.split(":")[1]:wholeID.split(":")[0];


                        String sel = MediaStore.Images.Media._ID + "=?";
                        // Get the cursor
                        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                filePathColumn, sel, new String[]{ id }, null);
                        if(cursor != null && cursor.getCount()>0) {
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            photoEncoded = cursor.getString(columnIndex);
                            albumList.get(currentAlbum).getPhotoList().add(photoEncoded);
                            adapter.notifyDataSetChanged();
                            cursor.close();
                        }

                    }
                    Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                }
            else if(data.getData()!=null){

                Uri mImageUri=data.getData();

                String wholeID = DocumentsContract.getDocumentId(mImageUri);

                // Split at colon, use second item in the array
                String id =  wholeID.split(":").length>1?wholeID.split(":")[1]:wholeID.split(":")[0];


                String sel = MediaStore.Images.Media._ID + "=?";
                // Get the cursor
                Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        filePathColumn, sel, new String[]{ id }, null);
                // Move to first row
                if(cursor != null && cursor.getCount()>0) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    photoEncoded = cursor.getString(columnIndex);
                    albumList.get(currentAlbum).getPhotoList().add(photoEncoded);
                    adapter.notifyDataSetChanged();
                    cursor.close();
                }

            }
            }

         else {
            Toast.makeText(this, "You haven't picked Image",
                    Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initialiseRecyclerView() {
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new AlbumAdapter(MainActivity.this,albumList);
        recyclerView.setAdapter(adapter);
    }

    private boolean permissionsGranted() {
        for (String permission:permissionList) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
            ) != PERMISSION_GRANTED
            ) return false;
        }
        return true;
    }
}