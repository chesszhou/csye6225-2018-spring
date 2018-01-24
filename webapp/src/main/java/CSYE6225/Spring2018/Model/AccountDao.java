package CSYE6225.Spring2018.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AccountDao implements Dao{
    @Autowired
    DataSource dataSource;
    @Autowired
    JdbcTemplate jdbcTemplate;
    public void signUp(Account account){

    }

    public Account validateUser(Account account){
        return null;
    }

    class UserMapper implements RowMapper<Account> {
        @Override
        public Account mapRow(ResultSet resultSet, int i) throws SQLException {
            Account account = new Account();

            account.setUsername(resultSet.getString("username"));
            account.setPassword(resultSet.getString("password"));
            return account;

        }
    }
}


