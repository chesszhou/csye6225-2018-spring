package dbDriver;

import com.csye6225.spring2018.BCrypt;

import java.sql.*;
import java.util.HashMap;

public class Driver {
    Connection myConnect;
    Statement myStat;
    ResultSet resultSet;
    HashMap<String, String> users;
    public Driver(){
        try {
            this.myConnect = DriverManager.getConnection("jdbc:mysql://localhost:3306/csye6225",
                    "root", "1994218m");
            this.myStat = myConnect.createStatement();
            this.resultSet = myStat.executeQuery("select * from user");
            this.users = new HashMap<>();
            while(resultSet.next()){
                users.put(resultSet.getString("username"), resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean isValidUser(String username, String password) throws SQLException {
        resultSet = myStat.executeQuery("select * from user");
        users = new HashMap<>();
        while(resultSet.next()){
            users.put(resultSet.getString("username"), resultSet.getString("password"));
        }
        if(username.equals("") || password.equals("")){
            return false;
        }
        if (BCrypt.checkpw(password, users.get(username))){
            return true;
        }else{
            return false;
        }
    }


    public boolean registerUser(String username, String password) throws SQLException {
        resultSet = myStat.executeQuery("select * from user");
        users = new HashMap<>();
        while(resultSet.next()){
            users.put(resultSet.getString("username"), resultSet.getString("password"));
        }
        if(users.get(username) != null){
            return false;
        }else{

            String sql = "insert into user " +
                    " (username, password)" +
                    " values(" + "'" + username + "'" + "," + "'" + BCrypt.hashpw(password, BCrypt.gensalt()) + "')";
            myStat.execute(sql);
            return true;
        }
    }


}
