package com.example.pdforganiser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.FeatureGroupInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /*
    List<File> pdf;
    RecyclerView view;
    ArrayAdapter<String> adapter;
    Adapter myAdapter;
    ArrayList<FileIndividual> fileNames;
    EditText search;
    ImageView searchBack;

     */

    ViewPager mainPager;
    TabLayout tabLayout;
    private viewPageAdapter ViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPager = findViewById(R.id.main_pager);
        tabLayout = findViewById(R.id.tab_layout);


        ViewPagerAdapter = new viewPageAdapter(getSupportFragmentManager(), 1);
        mainPager.setAdapter(ViewPagerAdapter);

        tabLayout.setupWithViewPager(mainPager);
        tabLayout.getTabAt(0).setText("All");
        tabLayout.getTabAt(1).setText("Groups");


        /*
        view = findViewById(R.id.recycler);
        search = findViewById(R.id.search_text);
        searchBack = findViewById(R.id.back_search);

        pdf = new ArrayList<>();
        fileNames = new ArrayList<>();

        //Search_Dir(Environment.getExternalStorageDirectory());

        showRecycler();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        searchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                //myAdapter.searchBack();
                filter("$");
                //filter("", 1);
            }
        });
       //view.setAdapter(adapter);

         */

    }

    /*
    void filter(String text){
        ArrayList<FileIndividual> lists = new ArrayList<>();
        for(FileIndividual item : fileNames){
            if(item.name.toLowerCase().startsWith(text)){
                lists.add(item);
            }
        }
        myAdapter.filter(lists);
    }

    public ArrayList<File> find_pdf(File dir) {
        //String selection = "_data LIKE '%.pdf'";

        ArrayList<File> arrayList = new ArrayList();
        File[] files = dir.listFiles();
        dir.lastModified();

        for(File singleFile : files){
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                arrayList.addAll(find_pdf(singleFile));
            }else{
                if(singleFile.getName().endsWith(".pdf")){
                    arrayList.add(singleFile);
                    FileIndividual individual = new FileIndividual(singleFile.getName());
                    fileNames.add(individual);
                }
            }
        }

        return arrayList;
    }

    public void showRecycler(){
        view.setLayoutManager(new LinearLayoutManager(this));
        pdf.addAll(find_pdf(Environment.getExternalStorageDirectory()));
        myAdapter = new Adapter(getApplicationContext(), fileNames);
        view.setAdapter(myAdapter);
    }

     */
}