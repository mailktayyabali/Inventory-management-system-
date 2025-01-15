package Backend;

public class Pos {
    private double rate;
    private double amount;
    private double totalAmount;

    public Pos() {
        this.rate = 0.0;
        this.amount = 0.0;
        this.totalAmount = 0.0;
    }

    public double getRate() {
        return rate;
    } 

    public void setRate(double rate) {
        this.rate = rate;
        calculateTotalAmount();
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
        calculateTotalAmount();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    private void calculateTotalAmount() {
        this.totalAmount = this.rate * this.amount;
    }
}
