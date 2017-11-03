/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author vulst
 */
public class User implements Serializable{

    String username;
    byte[] salt;
    byte[] hash;
    int sessiontoken;
    Timestamp time;

    public User(String username, byte[] salt, byte[] hash, int sessiontoken, Timestamp time) {
        this.username = username;
        this.salt = salt;
        this.hash = hash;
        this.sessiontoken = sessiontoken;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public int getSessiontoken() {
        return sessiontoken;
    }

    public void setSessiontoken(int sessiontoken) {
        this.sessiontoken = sessiontoken;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
