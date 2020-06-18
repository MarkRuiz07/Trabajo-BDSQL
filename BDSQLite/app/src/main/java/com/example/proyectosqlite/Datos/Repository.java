package com.example.proyectosqlite.Datos;

import android.content.Context;
import android.widget.Toast;

import androidx.room.Room;

import com.example.proyectosqlite.Config.Constants;
import com.example.proyectosqlite.Database.AppDatabase;
import com.example.proyectosqlite.Entities.Asignatura;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    public static List<Asignatura> lista = new ArrayList<Asignatura>();
    public static AppDatabase db = null;
    
    public Repository(Context c) {
        if(db == null) {
            db = Room.databaseBuilder(c, AppDatabase.class, Constants.BD_NAME)
                .allowMainThreadQueries()
                .build();
        }
        lista = db.asignaturaDao().getAllAsignatura();
    }
    
    public long agregar(Asignatura asignatura) {
        lista.add(asignatura);
        long res = db.asignaturaDao().insert(asignatura);
        return res;
    }
    
    public int eliminar(long id) {
        int res = 0;
        Asignatura a = null;
        for (Asignatura asig : lista) {
            if(asig.getId() == id) {
                a = asig;
            }
        }
        if(a != null) {
            lista.remove(a);
            res = db.asignaturaDao().deleteById(id);
        }
        return res;
    }

    public int actualizar(Asignatura asignatura) {
        int res = 0;
        Asignatura asigActualizar = null;
        int indice = -1;
        for (Asignatura asig : lista) {
            if(asig.getId() == asignatura.getId()) {
                asigActualizar = asig;
                indice ++;
            }
        }
        if(asigActualizar != null) {
            lista.set(indice, asignatura);
            db.asignaturaDao().updateEntidad(asignatura.getId(), asignatura.getTitle(), asignatura.getDescription());
        }
        return res;
    }
    
}
