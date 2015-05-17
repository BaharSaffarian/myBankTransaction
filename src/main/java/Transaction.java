import java.io.Serializable;
import java.math.BigDecimal;


public class Transaction implements Serializable {
    private String id;
    private String type;
    private BigDecimal amount;
    private String depositId;
    private boolean success;
    private BigDecimal depositNewBalance;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setDepositNewBalance(BigDecimal depositNewBalance) {
        this.depositNewBalance = depositNewBalance;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDepositId(String depositId) {
        this.depositId = depositId;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDepositId() {
        return depositId;
    }

    public boolean isSuccess() {
        return success;
    }

    public BigDecimal getDepositNewBalance() {
        return depositNewBalance;
    }
}
