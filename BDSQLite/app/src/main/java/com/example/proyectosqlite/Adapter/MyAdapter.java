package com.example.proyectosqlite.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proyectosqlite.Datos.Repository;
import com.example.proyectosqlite.Entities.Asignatura;
import com.example.proyectosqlite.Holder.MyHolder;
import com.example.proyectosqlite.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {
    Context c;
    Repository r = null;


    public MyAdapter(Context c, Repository r) {
        this.c=c;
        this.r = r;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row,null);
        return new MyHolder(view);
    }

    public void notifyChanged() {
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        holder.mTitle.setText(Repository.lista.get(position).getTitle());
        holder.mDescription.setText(Repository.lista.get(position).getDescription());

        holder.setCreateContextMenu(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Opciones");

                //Eliminando elemento
                menu.add("Eliminar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                    try {
                        Asignatura current = Repository.lista.get(position);
                        r.eliminar(current.getId());
                        notifyChanged();
                    }
                    catch(Exception ex) {
                        Toast.makeText(c, "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    return true;
                    }
                });

                //Actualizando elemento
                menu.add("Actualizar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Aquí va el código actualizar
                        final Dialog dlg = new Dialog(c);

                        dlg.setContentView(R.layout.add_new);
                        dlg.setTitle("Actualizar asignaturas");
                        dlg.setCancelable(false);
                        Button btAddNew = (Button) dlg.findViewById(R.id.btnew);
                        Button btCancel = (Button) dlg.findViewById(R.id.btcancel);

                        final EditText editText_Name = (EditText) dlg.findViewById(R.id.editText_Name);
                        final EditText editText_Des = (EditText) dlg.findViewById(R.id.editText_Desc);

                        final Asignatura current = Repository.lista.get(position);
                        editText_Name.setText(current.getTitle());
                        editText_Des.setText(current.getDescription());

                        btAddNew.setText("Actualizar");

                        btAddNew.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImageView imageView = (ImageView) dlg.findViewById(R.id.imageAsig);

                                if ((editText_Name.getText().toString().contentEquals("")) ||
                                        (editText_Des.getText().toString().contentEquals(""))) {
                                    Toast.makeText(c, "Nombre y descripcion es necesario",
                                            Toast.LENGTH_LONG).show();

                                } else {
                                    String nAsignatura, nDes;

                                    nAsignatura = editText_Name.getText().toString();
                                    nDes = editText_Des.getText().toString();

                                    current.setTitle(nAsignatura);
                                    current.setDescription(nDes);
                                    try {
                                        long resultadoInsert = r.actualizar(current);
                                        notifyChanged();
                                        Toast.makeText(c, "Datos Insertados", Toast.LENGTH_LONG).show();
                                        editText_Name.setText("");
                                        editText_Des.setText("");
                                    }
                                    catch(Exception e) {
                                        Toast.makeText(c, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                    dlg.cancel();
                                }
                            }
                        });

                        btCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dlg.cancel();
                            }
                        });

                        dlg.show();

                        return true;
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return Repository.lista.size();
    }

}
