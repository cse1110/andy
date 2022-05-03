package delft;

class ATM {

    public boolean validWithdraw(int amount) {
        if (amount < 20 || amount > 200)
            throw new IllegalArgumentException();
        return amount % 20 == 0;
    }

}