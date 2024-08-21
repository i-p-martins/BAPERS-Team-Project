package bapers;

import bapers.bapacct.Job;
import bapers.bapacct.JobsCollection;

import java.sql.Date;
import java.util.Collection;

public interface I_Bapacct_Jobs {

    /**
     * @param customerAccountID
     * @param urgency
     * @param price
     * @param deadline
     */
    abstract boolean createJob(int customerAccountID, int urgency, double price, Date deadline);

    /**
     * @param jobID
     */
    abstract boolean deleteJob(int jobID);

    /**
     * @param id
     */
    abstract void assignJobID(int id);

    /**
     * @param jobID
     */
    abstract Job retrieveJob(int jobID);

    abstract Collection<JobsCollection> viewJobsInProgress();

    /**
     * @param jobID
     */
    abstract void viewJobStatus(int jobID);

    abstract Collection<JobsCollection> viewJobs();

    /**
     * @param jobID
     */
    abstract void updateJobStatus(int jobID);

}