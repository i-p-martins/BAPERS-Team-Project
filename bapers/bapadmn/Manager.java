package bapers.bapadmn;

public abstract class Manager extends Staff_ImplClass {

    private boolean canEditCustomerAccount;

    /**
     * @param i
     * @param r
     * @param u
     * @param fn
     * @param ln
     */
    public Manager(String i, String r, String u, String fn, String ln, String ll) {
        super(i, r, u, fn, ln,ll);
        // TODO - implement Manager.Manager
        throw new UnsupportedOperationException();
    }

    public void upgrageCustomerAccount() {
        // TODO - implement Manager.upgrageCustomerAccount
        throw new UnsupportedOperationException();
    }

    public void downgradeCustomerAccount() {
        // TODO - implement Manager.downgradeCustomerAccount
        throw new UnsupportedOperationException();
    }

}