package com.example.pdforganiser;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllFragment extends Fragment {

    public List<File> pdf;
    RecyclerView view;
    ArrayAdapter<String> adapter;
    Adapter myAdapter;
    ArrayList<FileIndividual> fileNames;
    EditText search;
    ImageView searchBack;
    ArrayList<File> pdfFile;
    ImageView nothing;
    ArrayList<String> names;
    ArrayList<String> ogNames;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllFragment newInstance(String param1, String param2) {
        AllFragment fragment = new AllFragment();
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
        View view1 = inflater.inflate(R.layout.fragment_all, container, false);

        view = view1.findViewById(R.id.recycler);
        search = view1.findViewById(R.id.search_text);
        searchBack = view1.findViewById(R.id.back_search);
        nothing = view1.findViewById(R.id.nothing_here);

        pdf = new ArrayList<>();
        fileNames = new ArrayList<>();
        pdfFile = new ArrayList<>();

        requestPerm();

        //Search_Dir(Environment.getExternalStorageDirectory());

        //showRecycler();

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
                searchBack.setVisibility(View.VISIBLE);
                search.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                        if(keyCode == KeyEvent.KEYCODE_DEL) {
                            if(TextUtils.isEmpty(s)){
                                getFragmentManager().beginTransaction().detach(AllFragment.this).attach(AllFragment.this).commit();
                            }
                            //this is for backspace
                        }
                        return false;
                    }
                });

            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search.clearFocus();
                    InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(search.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


        searchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                //myAdapter.searchBack();
                filter("$");
                searchBack.setVisibility(View.GONE);
                getFragmentManager().beginTransaction().detach(AllFragment.this).attach(AllFragment.this).commit();
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                //filter("", 1);
            }
        });

        return view1;
    }

    void filter(String text){
        names = new ArrayList<>();
        ArrayList<FileIndividual> lists = new ArrayList<>();
        for(FileIndividual item : fileNames){
            //names.add(item.name.toLowerCase());
            if(item.name.toLowerCase().contains(text) && (!names.contains(item.name.toLowerCase()))){
                    lists.add(item);
                    names.add(item.name.toLowerCase());

            }
        }
        myAdapter.filter(lists);
    }

    public void requestPerm(){
        Dexter.withContext(getContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                showRecycler();
                //pdf.addAll(find_pdf(Environment.getExternalStorageDirectory()));
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
        Dexter.withContext(getContext()).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                showRecycler();
                //pdf.addAll(find_pdf(Environment.getExternalStorageDirectory()));
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    public ArrayList<File> find_pdf(File dir) {
        //String selection = "_data LIKE '%.pdf'";

        ArrayList<File> arrayList = new ArrayList();
        File[] files = dir.listFiles();
        //dir.lastModified();

        //if(files.length != 0) {
            for (File singleFile : files) {
                if (singleFile.isDirectory() && !singleFile.isHidden()) {
                    arrayList.addAll(find_pdf(singleFile));
                } else {
                    if (singleFile.getName().endsWith(".pdf")) {
                        arrayList.add(singleFile);
                        FileIndividual individual = new FileIndividual(singleFile.getName(), (singleFile.getAbsolutePath()), singleFile);
                        fileNames.add(individual);
                    }
                }
            //}
        }

        return arrayList;
    }

    void getPdf(){
        ContentResolver contentResolver = Objects.requireNonNull(getContext()).getContentResolver();
        //Uri songUri = MediaStore.Files.getContentUri(DOWNLOAD_SERVICE);
        //Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String select = "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.pdf'" + " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.pdf'" + ")";
        String orderBy = MediaStore.Files.FileColumns.DATE_TAKEN;
        //String[] projection = { MediaStore.Files.FileColumns.DATA };
        Cursor c = contentResolver.query(MediaStore.Files.getContentUri("external"), null, select , null, orderBy + " DESC ");


        if(c!=null && c.moveToFirst()){
            ogNames = new ArrayList<>();
            int fileName = c.getColumnIndex(MediaStore.Files.FileColumns.TITLE);
            int filePath = c.getColumnIndex(MediaStore.Files.FileColumns.DATA);

            do{
                String fileNameStr = c.getString(fileName);
                String filePathStr = c.getString(filePath);

                File file = new File(filePathStr);
                if(file.exists() && !ogNames.contains(fileNameStr)) {
                    FileIndividual individual = new FileIndividual(fileNameStr, filePathStr, new File(filePathStr));
                    fileNames.add(individual);
                    ogNames.add(fileNameStr);
                }
                //art.add(currArt);
            }while (c.moveToNext());
        }else{
            nothing.setVisibility(View.VISIBLE);
            //Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
        }
    }

    public void showRecycler(){
        //pdf.addAll(find_pdf(Environment.getExternalStorageDirectory()));
        getPdf();
        view.setLayoutManager(new LinearLayoutManager(getContext()));
        myAdapter = new Adapter(getContext(), fileNames, 0);
        view.setAdapter(myAdapter);
    }
}