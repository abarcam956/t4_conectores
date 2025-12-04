package com.edu;

import java.io.IOException;
import java.time.LocalDate;

import com.edu.backend.Conexion;
import com.edu.backend.dao.CentroDao;
import com.edu.backend.dao.EstudianteDao;
import com.edu.domain.Centro;
import com.edu.domain.Estudiante;
import com.edu.domain.Titularidad;

import edu.acceso.sqlutils.errors.DataAccessException;

public class Main {

    public static void main(String[] args) {
        String url = "file::memory:?cache=shared";

        Conexion cx = null;
        
        try {
            cx = Conexion.create(url, "resources:/centros.sql");
            System.out.println("Hemos logrado conectar a la base de datos");
        } catch (IOException e){
            System.err.println("Es imposible acceder a la base de datos");
        } catch (DataAccessException e) {
            System.err.println("Error al iniciar la base de datos. "+ e.getMessage());
        }

        Centro[] centros = new Centro[] {
            new Centro(11004866, "IES Castillo de Luna", Titularidad.PUBLICA),
            new Centro(11700602, "IES Pintor Juan Lara", Titularidad.PUBLICA),
            new Centro(11004039, "IES SIDON" , Titularidad.PUBLICA),
            new Centro(21002100, "IES Pade José Miravent", Titularidad.PUBLICA)
        };

        try {
            CentroDao centroDao = new CentroDao(cx);
            EstudianteDao estudianteDao = new EstudianteDao(cx);

            // Agrego los centros a la base de datos
            centroDao.insert(centros);

            System.out.println("--- LISTA DE CENTROS ---");
            centroDao.get().forEach(System.out::println);
            System.out.println("---- ************** ----");

            System.out.println("---- **** ------");
            System.out.println(centroDao.get(11004866));
            System.out.println("---- **** ------");

            Estudiante[] estudiantes = new Estudiante[] {
                new Estudiante(null, "Perico de los Palotes", LocalDate.of(2000, 01, 01), centros[0]),
                new Estudiante(null, "Segismundo", LocalDate.of(2002, 02, 02), null)
            };

            estudianteDao.insert(estudiantes);

            System.out.println("--- LISTA DE ESTUDIANTES ---");
            for(Estudiante estudiante: estudianteDao.get()) {
                System.out.printf("Estudiante %d: %s.\n", estudiante.getId(), estudiante);
            }
            estudianteDao.get().forEach(System.out::println);
            System.out.println("---- ************** ----");
        }
        catch(DataAccessException err) {
            err.printStackTrace();
            System.err.println("Error de conexión. " + err.getMessage());
        }
    }
}