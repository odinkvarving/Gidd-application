package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.entities.Account;
import ntnu.idatt2106.group8.gidd.model.entities.AccountInfo;
import ntnu.idatt2106.group8.gidd.model.entities.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    private Account testAccount1;
    private Account testAccount2;

    private AccountInfo testInfo1;
    private AccountInfo testInfo2;

    private Activity testActivity1;
    private Activity testActivity2;


    @BeforeEach
    void setUp() {

        //set up the first account and info
        this.testInfo1 = new
                AccountInfo.Builder()
                .setFirstname("fistname1")
                .setSurname("surname1")
                .setPoints(1)
                .setProfileDescription("testprofile1")
                .setImageURL("imageURL1")
                .build();
        this.testAccount1 = new Account("email1", "password1");

        //set up the second account and info
        this.testInfo2 = new
                AccountInfo.Builder()
                .setFirstname("fistname2")
                .setSurname("surname2")
                .setPoints(2)
                .setProfileDescription("testprofile2")
                .setImageURL("imageURL2")
                .build();
        this.testAccount1 = new Account("email2", "password2");

        //set up the first activity
        this.testActivity1 = new
                Activity.Builder("activity1", this.testAccount1,
                null, null, null, null, 10)
                .setDescription("test activity 1")
                .build();

        //set up the second activity
        this.testActivity2 = new
                Activity.Builder("activity2", this.testAccount2,
                null, null, null, null, 10)
                .setDescription("test activity 2")
                .build();
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertNotNull(this.accountService);
    }

    @Test
    void addAndGetUser(){

        this.accountService.saveAccountWithInfo(this.testAccount1,this.testInfo1);
        assertNotNull(this.accountService.findAccountById(this.testAccount1.getId()),"failed getting ");
        assertNotNull(this.accountService.findAccountById(this.testAccount1.getId()).getAccountInfo());
    }

}