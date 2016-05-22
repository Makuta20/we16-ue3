package at.ac.tuwien.big.we16.ue3.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "User")
public class User extends BaseEntity{

    private String salutation;
    private String firstname;
    private String lastname;

    private String email;

    private String password;

    @Temporal(TemporalType.DATE)
    private Date dateofbirth;

    private int balance;
    private int runningAuctionsCount;
    private int wonAuctionsCount;
    private int lostAuctionsCount;

    public String getFullName() {
        return this.firstname + " " + this.lastname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public float getConvertedBalance() {
        float convertedBalance = (float)this.balance;
        return convertedBalance / 100;
    }

    public void decreaseBalance(int amount) {
        this.balance -= amount;
    }

    public void increaseBalance(int amount) {
        this.balance += amount;
    }

    public int getRunningAuctionsCount() {
        return this.runningAuctionsCount;
    }

    public void incrementRunningAuctions() {
        this.runningAuctionsCount++;
    }

    public void decrementRunningAuctions() {
        this.runningAuctionsCount--;
    }

    public int getWonAuctionsCount() {
        return this.wonAuctionsCount;
    }

    public int getLostAuctionsCount() {
        return this.lostAuctionsCount;
    }

    public void incrementLostAuctionsCount() {
        this.lostAuctionsCount++;
    }

    public void incrementWonAuctionsCount() {
        this.wonAuctionsCount++;
    }

    public boolean hasSufficientBalance(int amount) {
        return this.balance >= amount;
    }

    public String getEmail() {
        return email;
    }

    public String getSalutation() {
        return salutation;
    }

    public Date getDate() {
        return dateofbirth;
    }

    public void setDate(Date date) {
        this.dateofbirth = date;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
