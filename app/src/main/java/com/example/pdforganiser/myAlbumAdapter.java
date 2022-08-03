package com.example.pdforganiser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class myAlbumAdapter extends RecyclerView.Adapter<myAlbumAdapter.myViewHolder> {

    Context context;
    ArrayList<MainTableIndividual> tableList;
    String nameFile, path;
    File loc;
    //myDatabaseHelper myDB;
    int type;

    myAlbumAdapter(Context context, ArrayList<MainTableIndividual> tableList, int type){
        this.context = context;
        this.tableList = tableList;
        this.type = type;
    }

    myAlbumAdapter(Context context, ArrayList<MainTableIndividual> tableList, String nameFile, String path, File loc, int type){
        this.context = context;
        this.tableList = tableList;
        this.nameFile = nameFile;
        this.loc = loc;
        this.type = type;
        this.path = path;
    }

    @NonNull
    @Override
    public myAlbumAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        //pdf = new ArrayList<>();
        view = LayoutInflater.from(context).inflate(R.layout.table_name_indivual, parent, false);
        return new myAlbumAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myAlbumAdapter.myViewHolder holder, int position) {
        holder.name.setText(tableList.get(position).getName().replace("_", " "));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == 1) {
                    //Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context, holder.name.getText().toString(), Toast.LENGTH_SHORT).show();
                    //myDB = new myDatabaseHelper(context);
                    //myDB.addToTable(tableList.get(position).getName(), nameFile, path, loc);
                    View alertView = LayoutInflater.from(context).inflate(R.layout.rename_file, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(alertView);

                    final EditText renameName = alertView.findViewById(R.id.rename_file_edittext);
                    final TextView fileName = alertView.findViewById(R.id.add_rename_text);
                    fileName.setText(nameFile + ".pdf");
                    builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!TextUtils.isEmpty(renameName.getText().toString())){
                                File sourceLocation = new File(loc.getAbsolutePath());
                                File targetLocation = new File(Environment.getExternalStorageDirectory().toString() + "/pdf_organizer/" + holder.name.getText().toString() + "/" + renameName.getText().toString().trim() + ".pdf");
                                try {
                                    copyFile(sourceLocation, targetLocation);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                Toast.makeText(context, "Please enter new file name", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("Keep", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File sourceLocation = new File(loc.getAbsolutePath());
                            File targetLocation = new File(Environment.getExternalStorageDirectory().toString() + "/pdf_organizer/" + holder.name.getText().toString() + "/" + fileName.getText().toString().trim());
                            try {
                                copyFile(sourceLocation, targetLocation);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    builder.show();


                }else if(type == 0){
                    //Toast.makeText(context, "Opening", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, ViewPdf.class);
                    intent.putExtra("name", holder.name.getText().toString().trim());
                    //intent.putExtra("id", tableList.get(position).getId());
                    context.startActivity(intent);
                }
            }
        });

        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(tableList.get(position).getName());
                builder.setMessage("Are you sure you want to delete?");

                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //myDatabaseHelper db = new myDatabaseHelper(context);
                        File file = new File(Environment.getExternalStorageDirectory().toString() + "/pdf_organizer/" + holder.name.getText().toString().trim());
                        deleteDir(file);
                        //db.deleteTable(String.valueOf(tableList.get(position).getId()), tableList.get(position).getName());
                        tableList.remove(position);
                        notifyItemRemoved(position);
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

    @Override
    public int getItemCount() {
        return tableList.size();
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    public static void copyFile(File sourceLocation, File targetLocation)
            throws FileNotFoundException, IOException {
        InputStream in = new FileInputStream(sourceLocation);
        OutputStream out = new FileOutputStream(targetLocation);

        // Copy the bits from instream to outstream
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        TextView name, preview;
        RelativeLayout layout;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            layout = itemView.findViewById(R.id.main_table_relative_layout);
            preview = itemView.findViewById(R.id.preview);
        }
    }
}
