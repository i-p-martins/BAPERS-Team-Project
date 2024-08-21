package bapers;

public interface I_Bapacct_Customers {

    /**
     * @param f
     * @param l
     * @param e
     * @param m
     */
    abstract void createCustomerAccout(String f, String l, String e, String m);

    /**
     * @param customerID
     */
    abstract boolean deleteCustomerAccount(int customerID);

    /**
     * @param customerID
     */
    abstract boolean viewCustomerAccount(int customerID);

    /**
     * @param customerID
     */
    abstract boolean searchCustomerAccount(int customerID);

}