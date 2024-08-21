package bapers;

public interface I_ReportChain {

    /**
     * @param nextReportChain
     */
    I_ReportChain setNextChain(I_ReportChain nextReportChain);

    /**
     * @param request
     */
    void generateReport(String request);

}