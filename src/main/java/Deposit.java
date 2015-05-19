import java.math.BigDecimal;

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

    boolean validateDepositOperand(BigDecimal value) {
        return (initialBalance.add(value).compareTo(upperBound) <= 0);
    }

    boolean validateWithdrawOperand(BigDecimal value) {

        return (initialBalance.subtract(value).compareTo(new BigDecimal(0)) >= 0);
    }

    BigDecimal deposit(BigDecimal value) {
        initialBalance = initialBalance.add(value);
        return initialBalance;
    }

    BigDecimal withdraw(BigDecimal value) {
        initialBalance = initialBalance.subtract(value);
        return initialBalance;
    }
}
