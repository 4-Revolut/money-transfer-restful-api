package io.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;


public class User {

    @JsonProperty("id")
    private String id = null;

    @NotNull(message = "email is mandatory")
    @Email(message = "please provide correct email")
    @Size(min = 10, max = 30, message = "email should be between {min} and {max} symbols")
    @JsonProperty("email")
    private String email = null;

    @NotNull(message = "firstname is mandatory")
    @Size(min = 5, max = 15, message = "firstname should be between {min} and {max} symbols")
    @JsonProperty("firstname")
    private String firstname = null;

    @NotNull(message = "lastname is mandatory")
    @Size(min = 5, max = 15, message = "lastname should be between {min} and {max} symbols")
    @JsonProperty("lastname")
    private String lastname = null;

    @JsonProperty("accountArray")
    private AccountArray accountArray = null;

    public User() {
    }

    public User(String id, String email, String firstname, String lastname, AccountArray accountArray) {
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.accountArray = accountArray;
    }

    public User(String email, String firstname, String lastname, AccountArray accountArray) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.accountArray = accountArray;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User email(String email) {
        this.email = email;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public User lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @JsonProperty("accountArray")
    public AccountArray getAccountArray() {
        return accountArray;
    }

    public void setAccountArray(AccountArray accountArray) {
        this.accountArray = accountArray;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(this.id, user.id) &&
                Objects.equals(this.email, user.email) &&
                Objects.equals(this.firstname, user.firstname) &&
                Objects.equals(this.lastname, user.lastname) &&
                Objects.equals(this.accountArray, user.accountArray);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstname, lastname, accountArray);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class User {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    firstname: ").append(toIndentedString(firstname)).append("\n");
        sb.append("    lastname: ").append(toIndentedString(lastname)).append("\n");
        sb.append("    accountArray: ").append(toIndentedString(accountArray)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
