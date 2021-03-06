package edu.colostate.cs.cs414.StringCheese.src.BusinessLayer;

import edu.colostate.cs.cs414.StringCheese.src.Foundation.DBConnection;

import java.io.*;
import java.sql.*;

public class SerializedGame {

    public void write(DBConnection conn, Game tmp) {
        Connection con = conn.open();
        try {
            byte[] data = null;
            ByteArrayOutputStream bt = new ByteArrayOutputStream();
            ObjectOutputStream obj = new ObjectOutputStream(bt);
            obj.writeObject(tmp);
            obj.flush();
            obj.close();
            obj.reset();
            bt.close();
            data = bt.toByteArray();

            if (!con.isClosed()) {
                //check if game object is in DB already
                PreparedStatement ptst = con.prepareStatement("SELECT * from gameserialized where game_id=?");// For meet local db instance
                // PreparedStatement ptst = con.prepareStatement("insert into gameSerialized values(?,?)");//for amazon aws
                ptst.setInt(1, tmp.getGameID());
                ResultSet rs = ptst.executeQuery();
                //if game not already in DB insert it
                if (!rs.next()) {
                    ptst = con.prepareStatement("insert into gameserialized values(?,?,?)");
                    ptst.setInt(1, tmp.getGameID());
                    ptst.setBytes(2, data);
                    ptst.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                    ptst.executeUpdate();
                    //else game already in DB so update it
                } else {
                    ptst = con.prepareStatement("update gameserialized SET game_object=?, last_updated=? where game_id=?");
                    ptst.setBytes(1, data);
                    ptst.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                    ptst.setInt(3, tmp.getGameID());
                    ptst.executeUpdate();

                }
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

    public Game read(DBConnection conn, int id) {

        Connection con = conn.open();
        Game meet = null;
        try {
            if (!con.isClosed()) {
                PreparedStatement ptst = con.prepareStatement("select game_object from gameserialized where game_id=?");//For meet LOcal db Instance
                //  PreparedStatement ptst = con.prepareStatement("select game_object from gameSerialized where game_id=?");//for aws Web Service
                ptst.setInt(1, id);
                ResultSet rs = ptst.executeQuery();
                rs.next();
                byte[] t = rs.getBytes(1);
                if (t != null) {
                    ByteArrayInputStream targetStream = new ByteArrayInputStream(t);
                    ObjectInputStream in = new ObjectInputStream(targetStream);
                    meet = (Game) in.readObject();
                    // in.reset();
                    in.close();

                    targetStream.close();

                }
                rs.close();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return meet;
    }
}

