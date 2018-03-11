package com.javarank.syncpractice;

/**
 * Created by Anik on 3/10/2018.
 */

public class Person {

    private String firstName;
    private String lastName;
    private int syncStatus;

    public Person(String firstName, String lastName, int syncStatus) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.syncStatus = syncStatus;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }
}
