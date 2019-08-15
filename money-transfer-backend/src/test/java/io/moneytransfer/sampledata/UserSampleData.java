package io.moneytransfer.sampledata;

import io.moneytransfer.model.Account;
import io.moneytransfer.model.AccountArray;
import io.moneytransfer.model.User;

import java.math.BigDecimal;

public class UserSampleData {


    public User userWithoutAccount() {
        User user = new User();
        user.setEmail("some@email.com");
        user.setFirstname("someFirstName");
        user.setFirstname("someLastName");

        return user;
    }

    public User userWithAccount() {
        User user = new User();
        user.setEmail("some@email.com");
        user.setFirstname("someFirstName");
        user.setFirstname("someLastName");

        AccountArray accountArray = new AccountArray();
        Account account = new Account();
        account.setBalance(new BigDecimal("2000"));
        account.setName("someAccountName");
        accountArray.add(account);
        user.setAccountArray(accountArray);

        return user;
    }

}
