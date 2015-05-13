import java.math.BigDecimal;

/**
 * Created by DOTIN SCHOOL 3 on 5/13/2015.
 */
public class Deposit {
    String customer;
    String id;
    BigDecimal initialBalance;
    BigDecimal upperBound;

    public Deposit(String customer, String id, BigDecimal initialBalance, BigDecimal upperBound) {
        this.customer = customer;
        this.id = id;
        this.initialBalance = initialBalance;
        this.upperBound = upperBound;
    }
}
