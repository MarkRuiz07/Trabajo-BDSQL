package com.example.proyectosqlite.UserInterfaces;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proyectosqlite.Adapter.MyAdapter;
import com.example.proyectosqlite.Datos.Repository;
import com.example.proyectosqlite.Entities.Asignatura;
import com.example.proyectosqlite.R;


public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    MyAdapter myAdapter;

    Repository repo;

    Button btAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repo = new Repository(this.getApplicationContext());

        btAdd = findViewById(R.id.btAdd);
        mRecyclerView = findViewById(R.id.recycleview1);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new MyAdapter(this, repo);

        mRecyclerView.setAdapter(myAdapter);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dlg = new Dialog(MainActivity.this);
                dlg.setContentView(R.layout.add_new);
                dlg.setTitle("Agregue asignaturas");
                dlg.setCancelable(false);
                Button btAddNew = (Button) dlg.findViewById(R.id.btnew);
                Button btCancel = (Button) dlg.findViewById(R.id.btcancel);


                btAddNew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText_Name = (EditText) dlg.findViewById(R.id.editText_Name);
                        EditText editText_Des = (EditText) dlg.findViewById(R.id.editText_Desc);
                        ImageView imageView = (ImageView) dlg.findViewById(R.id.imageAsig);

                        if ((editText_Name.getText().toString().contentEquals("")) ||
                                (editText_Des.getText().toString().contentEquals(""))) {
                            Toast.makeText(MainActivity.this, "Nombre y descripcion es necesario",
                                    Toast.LENGTH_LONG).show();

                        } else {
                            String nAsignatura, nDes;

                            nAsignatura = editText_Name.getText().toString();
                            nDes = editText_Des.getText().toString();

                            Asignatura asignaturaObj = new Asignatura();

                            asignaturaObj.setTitle(nAsignatura);
                            asignaturaObj.setDescription(nDes);

                            //long resultadoInsert = db.asignaturaDao().insert(asignaturaObj);
                            long resultadoInsert = repo.agregar(asignaturaObj);
                            if (resultadoInsert > 0) {
                                myAdapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Datos Insertados", Toast.LENGTH_LONG).show();
                                editText_Name.setText("");
                                editText_Des.setText("");
                            } else {
                                Toast.makeText(MainActivity.this, "Error al ingresar los datos", Toast.LENGTH_LONG).show();
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

            }

        });
    }
}

