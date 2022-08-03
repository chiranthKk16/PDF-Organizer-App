package com.example.pdforganiser;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumFragment extends Fragment {

    RelativeLayout addLayout;
    //myDatabaseHelper myDB;
    RecyclerView albumRecycler;
    ArrayList<MainTableIndividual> mainTableIndividuals;
    myAlbumAdapter albumAdapter;
    ImageView preview;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumFragment newInstance(String param1, String param2) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        //myDB = new myDatabaseHelper(getContext());

        addLayout = view.findViewById(R.id.add_layout);
        albumRecycler = view.findViewById(R.id.album_recycler);
        preview = view.findViewById(R.id.preview_fragment);

        mainTableIndividuals = new ArrayList<>();

        //getData();
        getPDfData();

        albumRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        albumAdapter = new myAlbumAdapter(getContext(), mainTableIndividuals, 0);
        albumRecycler.setAdapter(albumAdapter);


        addLayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                View alertView = LayoutInflater.from(getContext()).inflate(R.layout.enter_table_name, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(alertView);

                final EditText tableName = alertView.findViewById(R.id.table_add_editText);
                builder.setPositiveButton("create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!TextUtils.isEmpty(tableName.getText().toString())){
                            File folder = new File(Environment.getExternalStorageDirectory().toString() + "/pdf_organizer/" + tableName.getText().toString().trim());
                            folder.mkdirs();
                            //myDB.addMainTable(tableName.getText().toString().trim().replace(" ", "_"));
                            //albumAdapter.notifyDataSetChanged();
                            getFragmentManager().beginTransaction().detach(AlbumFragment.this).attach(AlbumFragment.this).commit();
                        }
                    }
                });

                builder.show();
            }
        });

        return view;
    }

    void getPDfData(){
            String path = Environment.getExternalStorageDirectory().toString() + "/pdf_organizer";
            File directory = new File(String.valueOf(path));
            File[] files = directory.listFiles();
            if(directory.exists() && files!=null) {
                //preview.setVisibility(View.GONE);
                for (File file : files) {
                    MainTableIndividual individual = new MainTableIndividual(file.getAbsolutePath(), file.getName());
                    mainTableIndividuals.add(individual);
                    //Log.d("FILE",file.getName());
                }
            }
            else{
                    preview.setVisibility(View.VISIBLE);
                    //Log.d("Null?", "it is null");
            }

    }

    /*
    void getData(){
        myDatabaseHelper helper = new myDatabaseHelper(getContext());
        Cursor cursor = helper.getMainData();

        if(cursor.getCount() == 0){
            //Toast.makeText(getContext(), "NO data", Toast.LENGTH_SHORT).show();
            preview.setVisibility(View.VISIBLE);
        }else{
            while (cursor.moveToNext()){
                //MainTableIndividual individual = new MainTableIndividual(cursor.getInt(0), cursor.getString(1));
                //mainTableIndividuals.add(individual);
            }
        }
    }

     */
}