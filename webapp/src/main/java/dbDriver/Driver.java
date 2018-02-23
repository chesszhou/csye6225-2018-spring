package dbDriver;

import com.csye6225.spring2018.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Driver {
    Connection myConnect;
    Statement myStat;
    ResultSet resultSet;
    HashMap<String, String> users;
    public Driver(){
        try {
            this.myConnect = DriverManager.getConnection("jdbc:mysql://localhost:3306/csye6225", "root", "1994218m");
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

    public String getAboutMe(String username) throws SQLException {
        resultSet = myStat.executeQuery("select * from user");
        users = new HashMap<>();
        while(resultSet.next()){
            users.put(resultSet.getString("username"), resultSet.getString("aboutMe"));
        }
        return users.get(username);
    }

    public String searchForAboutMe(String username) throws SQLException {
        resultSet = myStat.executeQuery("select * from user");
        ArrayList<String> userArr = new ArrayList<>();

        users = new HashMap<>();
        while(resultSet.next()){
            users.put(resultSet.getString("username"), resultSet.getString("aboutMe"));
            userArr.add(resultSet.getString("username"));
        }

        for(String s: userArr){
            if(s.equals(username)){
                if(getAboutMe(username) == null){
                    return "";
                }
                return getAboutMe(username);
            }
        }

        return "Not Found Such User";
    }

    public boolean addAboutMe(String inusername, String content) throws SQLException {
        String sql = "UPDATE user SET aboutMe=" + "'" + content + "'" +
                " WHERE username =" + "'" + inusername + "'";
        myStat.execute(sql);
        return true;
    }

}
