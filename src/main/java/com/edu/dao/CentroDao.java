package com.edu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.edu.ConnectionPool;
import com.edu.domain.Centro;
import com.edu.domain.Titularidad;

public class CentroDao implements GenericDao<Centro> {

    private ConnectionPool cp;
    public CentroDao(ConnectionPool cp) {
        this.cp = cp;
    }

    private static Centro resultSetToCentro(ResultSet rs) throws SQLException{
        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        Titularidad titularidad = Titularidad.fromString(rs.getString("titularidad"));
        return new Centro(id, nombre, titularidad);
    }
    
    @Override
    public Centro get(int id) throws SQLException {
        String sqlString= "SELECT * FROM Centro WHERE id= ?;";
        try (
            Connection conn = cp.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString)
        ){
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            return rs.next() ? resultSetToCentro(rs) : null;
        }
    }

    @Override
    public List<Centro> get() throws SQLException {
        String sqlString = "SELECT * FROM Centro;";
        List<Centro> centros = new ArrayList<>();

        try (
            Connection conn = cp.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlString);
        ){
            while (rs.next()) {
                try{
                    Centro centro = resultSetToCentro(rs);
                    centros.add(centro);
                } catch(SQLException e){
                    System.err.println("Un registro no puede convertirse en centro: "+e.getMessage());
                }
            }
        }
        return centros;
    }

    @Override
    public int insert(Centro entity) throws SQLException {
        String sqlString = "INSERT INTO Centro (nombre, titularidad, id) VALUES (?, ?, ?);";

        try (
            Connection conn = cp.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ){
            setParams(pstmt, entity);
            pstmt.executeUpdate();
            return entity.getId();
        }
        return 0;
    }

    @Override
    public boolean remove(int id) throws SQLException {
        String sqlString = "DELETE FROM Centro WHERE id= ?;";

        try (
            Connection conn = cp.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ){
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        }
    }

    @Override
    public void update(Centro entity) throws SQLException {
        String sqlString = "UPDATE Centro SET nombre = ?, titularidad = ? WHERE id = ?;";

        try (
            Connection conn = cp.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            setParams(pstmt, entity);
            int rows = pstmt.executeUpdate();
            if (rows == 0) throw new IllegalArgumentException("Error");
        } catch (Exception e) {
            // TODO: handle exception
        }
        
    }

    @Override
    public void insert(Iterable<Centro> entities) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }
}
