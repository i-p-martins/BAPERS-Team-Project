package bapers;

public interface I_Bapproc {

    /**
     * @param jobID
     * @param department
     * @param price
     */
    abstract boolean createTask(int jobID, String department, double price);

    /**
     * @param taskID
     */
    abstract void deleteTask(int taskID);

    abstract void retrieveTask();

    abstract void updateTaskStatus();

    /**
     * @param taskID
     */
    abstract void assignLocation(int taskID);

}