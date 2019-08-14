package io.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    @JsonProperty("id")
    private String id = null;

    @Size(min = 5, max = 15, message = "Account name should be between {min} and {max} symbols")
    @JsonProperty("name")
    private String name = null;

    @DecimalMin(value = "0", message = "Account balance can't be negative")
    @DecimalMax(value = "10000", message = "Account max allowed balance is 10000")
    @JsonProperty("balance")
    private BigDecimal balance = null;

    public Account() {
    }

    public Account(String id, String name, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Account name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Account balance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account account = (Account) o;
        return Objects.equals(this.id, account.id) &&
                Objects.equals(this.name, account.name) &&
                Objects.equals(this.balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, balance);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Account {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
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
