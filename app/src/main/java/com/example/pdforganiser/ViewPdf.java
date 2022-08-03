package com.example.pdforganiser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.UnicodeSetSpanner;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ViewPdf extends AppCompatActivity {

    TextView nameView, pathView;
    ImageView back, preview;
    String name, id;
    String path;
    Adapter myAdapter;
    ArrayList<File> documents;
    RecyclerView recyclerView;
    //myDatabaseHelper db;
    ArrayList<FileIndividual> contentIndi;
    AlbumFragment fragment = new AlbumFragment();
    FragmentManager manager;
    ArrayList<String> rowId;
    //ArrayList<FileIndividual> myPdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        nameView = findViewById(R.id.pdf_view_name);
        pathView = findViewById(R.id.pdf_view_path);
        back = findViewById(R.id.back_pdf_view);
        recyclerView = findViewById(R.id.view_content_recycler);
        preview = findViewById(R.id.preview_pdf_view);
        //delete = findViewById(R.id.delete_view_pdf);

        contentIndi = new ArrayList<>();
        rowId = new ArrayList<>();

        //FileIndividual myPdf = ;

        //AllFragment fragment = (AllFragment.class);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getStuff();
        //getTableData();
        getPdfData();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new Adapter(this, contentIndi, rowId, 1, nameView.getText().toString());
        recyclerView.setAdapter(myAdapter);

    }

    public void getStuff(){
        if(getIntent().hasExtra("name")){
            name = getIntent().getStringExtra("name");
            //id = getIntent().getStringExtra("id");
            //documents = (ArrayList<File>)getIntent().getSerializableExtra("indi");

            nameView.setText(name);

        }
    }

    void getPdfData(){
        String path = Environment.getExternalStorageDirectory().toString() + "/pdf_organizer/" + nameView.getText().toString().trim();
        File directory = new File(String.valueOf(path));
        File[] files = directory.listFiles();
        if(directory.exists() && files!=null) {
            for (File file : files) {
                if(file.getName().endsWith(".pdf")) {
                    FileIndividual individual = new FileIndividual(file.getName(), file.getAbsolutePath(), file);
                    contentIndi.add(individual);
                }
                //Log.d("FILE",file.getName());
            }
        }
        else{
            preview.setVisibility(View.VISIBLE);
            //Log.d("Null?", "it is null");
        }
    }

    /*
    void getTableData(){
        db = new myDatabaseHelper(this);
        Cursor cursor = db.getContentData(name.replace(" ", "_"));

        if(cursor.getCount() == 0){
            //Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show();
            preview.setVisibility(View.VISIBLE);
        }else{
            while(cursor.moveToNext()){
                rowId.add(cursor.getString(0));
                FileIndividual individual = new FileIndividual(cursor.getString(1), cursor.getString(2), new File(cursor.getString(3)));
                contentIndi.add(individual);
            }
        }
    }

     */
}