package com.example.pdforganiser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.artifex.mupdf.viewer.DocumentActivity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {

    Context context;
    ArrayList<FileIndividual> pdfFiles;
    static ArrayList<FileIndividual> ogList;
    ArrayList id;
    int who;
    String tableName;
    //List<File> pdf;
    ArrayList<MainTableIndividual> individuals;

    Adapter(Context context, ArrayList<FileIndividual> files, int who) {
        this.context = context;
        this.pdfFiles = files;
        this.who = who;
        //this.pdf = pdf;
    }

    Adapter(Context context, ArrayList<FileIndividual> files, ArrayList<String> id, int who, String tableName) {
        this.context = context;
        this.pdfFiles = files;
        this.who = who;
        this.id = id;
        this.tableName = tableName;
        //this.pdf = pdf;
    }



    @NonNull
    @Override
    public Adapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        //pdf = new ArrayList<>();
        view = LayoutInflater.from(context).inflate(R.layout.individual_pdf, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.myViewHolder holder, int position) {
        holder.name.setText(pdfFiles.get(position).getName().replace(".pdf", ""));

        individuals = new ArrayList<>();

        getPDfData();

        if(who == 1){
            holder.delete.setVisibility(View.GONE);
            holder.name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(pdfFiles.get(position).getName());
                    builder.setMessage("Are you sure you want to delete?");

                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //myDatabaseHelper db = new myDatabaseHelper(context);
                            //db.delIndi(String.valueOf(id.get(position)), tableName);
                            File file = new File(pdfFiles.get(position).getPath());
                            if(file.exists()){
                                file.delete();
                                Toast.makeText(context, "File Deleted", Toast.LENGTH_SHORT).show();
                            }
                            //notifyDataSetChanged();
                            notifyItemRemoved(position);
                            pdfFiles.remove(position);
                            //id.remove(position);
                            //notifyItemChanged(position);
                            //Intent intent = new Intent(context, ViewPdf.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            //((Activity)context).finish();
                            //context.startActivity(intent);
                            //((Activity)context).finish();
                        }
                    });
                    builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                    return false;
                }
            });
            holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(pdfFiles.get(position).getName());
                    builder.setMessage("Are you sure you want to delete?");

                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //myDatabaseHelper db = new myDatabaseHelper(context);
                            //db.delIndi(String.valueOf(id.get(position)), tableName);
                            File file = new File(pdfFiles.get(position).getPath());
                            if(file.exists()){
                                file.delete();
                                Toast.makeText(context, "File Deleted", Toast.LENGTH_SHORT).show();
                            }
                            //notifyDataSetChanged();
                            notifyItemRemoved(position);
                            pdfFiles.remove(position);
                            //id.remove(position);
                            //notifyItemChanged(position);
                            //Intent intent = new Intent(context, ViewPdf.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            //((Activity)context).finish();
                            //context.startActivity(intent);
                            //((Activity)context).finish();
                        }
                    });
                    builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                    return false;
                }
            });
        }

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, String.valueOf(pdfFiles.get(position).getPath()), Toast.LENGTH_SHORT).show();

                /*
                Intent intent = new Intent(context, DocumentActivity.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(pdfFiles.get(position).getPath()));
                context.startActivity(intent);


                 */

                Uri path = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", pdfFiles.get(position).getFile());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                        //Uri.parse(String.valueOf(pdfFiles.get(position).getFile())));
                intent.setDataAndType(path, "application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //intent.setType("application/pdf");
                PackageManager pm = context.getPackageManager();
                List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
                if (activities.size() > 0) {
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "FIle doesn't exist", Toast.LENGTH_SHORT).show();
                    // Do something else here. Maybe pop up a Dialog or Toast
                }



                /*

                Intent intent = new Intent(context, ViewPdf.class);
                intent.putExtra("name", pdfFiles.get(position).getName());
                intent.putExtra("path", pdfFiles.get(position).getPath());
                //intent.putExtra("file", pdf);
                context.startActivity(intent);


                 */



            }
        });

        holder.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.dropdown.getText().toString().equals("0")) {
                    holder.linearLayout.setVisibility(View.VISIBLE);
                    holder.dropdown.setText("1");
                }else if(holder.dropdown.getText().toString().equals("1")){
                    holder.linearLayout.setVisibility(View.GONE);
                    holder.dropdown.setText("0");
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle(pdfFiles.get(position).getName());
                dialog.setMessage("Are you sure you want to delete from device?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //File sub = new File(pdfFiles.get(position).getPath());
                        File file = new File(pdfFiles.get(position).getPath());
                        if(file.exists()){
                            file.delete();
                            Toast.makeText(context, "File Deleted", Toast.LENGTH_SHORT).show();
                        }
                        //ContentResolver resolver = context.getContentResolver();

                            //resolver.delete(Uri.parse(pdfFiles.get(position).getPath()), null, null);
                            /*
                            if(file.delete()){
                                resolver.delete(Uri.parse(pdfFiles.get(position).getPath()), null);
                                Toast.makeText(context, "File deleted", Toast.LENGTH_SHORT).show();
                                InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            }

                             */
                            pdfFiles.remove(position);
                            notifyItemRemoved(position);
                        Intent intent = new Intent(context, MainActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intent);
                        ((Activity)context).overridePendingTransition(0, 0);
                        ((Activity)context).finish();
                            //getFragmentManager().beginTransaction().detach(AllFragment.this).attach(AllFragment.this).commit();

                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });


        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(context).inflate(R.layout.dialog_yable_show, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view);

                AlbumFragment fragment = new AlbumFragment();
                final RecyclerView recyclerView = view.findViewById(R.id.show_recycler);
                myAlbumAdapter myAdapter = new myAlbumAdapter(context, individuals, pdfFiles.get(position).getName(), pdfFiles.get(position).getPath(), pdfFiles.get(position).getFile(), 1);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(myAdapter);

                builder.show();

                //InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

                //imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pdfFiles.size();
    }

    void getPDfData(){
        String path = Environment.getExternalStorageDirectory().toString() + "/pdf_organizer/";
        File directory = new File(String.valueOf(path));
        File[] files = directory.listFiles();
        if(directory.exists() && files!=null) {
            for (File file : files) {
                MainTableIndividual individual = new MainTableIndividual(file.getAbsolutePath(), file.getName());
                individuals.add(individual);
                //Log.d("FILE",file.getName());
            }
        }
        else{
            Log.d("Null?", "it is null");
        }

    }

    /*
    void getData(){
        myDatabaseHelper helper = new myDatabaseHelper(context);
        Cursor cursor = helper.getMainData();

        if(cursor.getCount() == 0){
            //Toast.makeText(context, "NO data", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                //MainTableIndividual individual = new MainTableIndividual(cursor.getInt(0), cursor.getString(1));
                //individuals.add(individual);
            }
        }
    }

     */


    public void filter(ArrayList<FileIndividual> list){

        //ogList = pdfFiles;
        if(!list.isEmpty()) {
            pdfFiles = list;
            notifyDataSetChanged();
        }
    }

    public void refresh(){
        //pdfFiles = ogList;
        notifyDataSetChanged();
    }



    public class myViewHolder extends RecyclerView.ViewHolder{

        TextView name, dropdown, add, delete, rename;
        RelativeLayout layout;
        LinearLayout linearLayout;
        ImageView down;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.pdf_name_indi);
            layout = itemView.findViewById(R.id.indi_layout);
            down = itemView.findViewById(R.id.down_arrow);
            linearLayout = itemView.findViewById(R.id.down_arrow_layout);
            dropdown = itemView.findViewById(R.id.dropdown);
            add = itemView.findViewById(R.id.add);
            //rename = itemView.findViewById(R.id.rename);
            delete = itemView.findViewById(R.id.delete_permanent);
        }
    }
}
