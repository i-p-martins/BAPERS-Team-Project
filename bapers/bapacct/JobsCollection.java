package bapers.bapacct;

import java.util.Collection;
import java.util.Date;

public class JobsCollection {

    private final int indx = 0;
    private Collection<Job> jobs;

    public JobsCollection() {
        // TODO - implement JobsCollection.JobsCollection
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        // TODO - implement JobsCollection.isEmpty
        throw new UnsupportedOperationException();
    }

    /**
     * @param customerAccountID
     * @param urgency
     * @param price
     * @param deadline
     */
    public boolean addItem(int customerAccountID, int urgency, double price, Date deadline) {
        // TODO - implement JobsCollection.addItem
        throw new UnsupportedOperationException();
    }

    /**
     * @param jobID
     */
    public void deleteItem(int jobID) {
        // TODO - implement JobsCollection.deleteItem
        throw new UnsupportedOperationException();
    }

    /**
     * @param jobID
     */
    public Job retrieveItem(int jobID) {
        // TODO - implement JobsCollection.retrieveItem
        throw new UnsupportedOperationException();
    }

    /**
     * @param j
     */
    public boolean containsItem(Job j) {
        // TODO - implement JobsCollection.containsItem
        throw new UnsupportedOperationException();
    }

    public void nextItem() {
        // TODO - implement JobsCollection.nextItem
        throw new UnsupportedOperationException();
    }

    public int size() {
        // TODO - implement JobsCollection.size
        throw new UnsupportedOperationException();
    }

}