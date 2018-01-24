package CSYE6225.Spring2018.Model;

public interface Dao {
    void signUp(Account account);
    Account validateUser(Account account);
}
