package com.springapp.mvc.data;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.springapp.mvc.model.User;

import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getFollowers(int userid)
    {
        return jdbcTemplate.query("SELECT userid,name from users where userid in (select userid from following where following_userid=?)",new Object[]{userid},new BeanPropertyRowMapper<User>(User.class));
    }

    public List<User> getSubscriptions(int userid)
    {
        return jdbcTemplate.query("SELECT userid,name from users where userid in (select following_userid from following where userid=?)",new Object[]{userid},new BeanPropertyRowMapper<User>(User.class));
    }

    public void addUser(String name, String password, String emailID) {
        System.out.println("User inserted "+name+" "+password+" "+emailID);
        jdbcTemplate.execute("INSERT INTO users(name,password,email) values ('" + name + "', '" + password + "', '" + emailID + "')");
    }

    public void modifyUser(String emailId, String password) {
        jdbcTemplate.update("UPDATE users set password=? where email=?", new Object[]{password, emailId});
    }

    public List<User> findUserbyEmail(String email)
    {
        return jdbcTemplate.query("SELECT userID,name,password from users where email=?", new Object[]{email}, new BeanPropertyRowMapper<User>(User.class));
    }

    public boolean isUserPresent(String email) {
        try{
            List<User> userList = jdbcTemplate.query("select name from users where email=?",
                    new Object[]{email}, new BeanPropertyRowMapper<User>());
            if(userList.size() > 0){
                return true;
            }
        }
        catch (Exception e){
            return true;
        }
        return false;
    }

    public int getUserIDByEmail(String email)
    {
        return jdbcTemplate.queryForObject("Select userid from users where email=?",new Object[]{email},new BeanPropertyRowMapper<User>(User.class)).getUserID();
    }

    public void unfollow(int one,int two)
    {
        jdbcTemplate.execute("delete from following where userid="+one+" and following_userid="+two);
    }

    public void follow(int one,int two)
    {
        jdbcTemplate.execute("insert into following values("+one+","+two+")");
    }

}

