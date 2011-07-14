package com.morntea.web.pricemonitor.server;

//import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class User {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long userId;

    @Persistent
    private String username;
    
    @Persistent
    private String password;

    /*
    @Persistent
    private String nickName;

    @Persistent
    private Date birthday;

    @Persistent
    private Date registerDate;
    */
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public Long getUserId() {
        return this.userId;
    }
    public String getUsername() {
        return this.username;
    }
    public boolean isPassword(String password) {
        return this.password.equals(password);
    }
}
