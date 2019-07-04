package com.pburgov.sequential.tasks;


import javafx.concurrent.Task;

public class UpdateFactorsInPSTask extends Task<String>  {

    private int listSize;
    private static final String TASK_TITLE = "UPDATING CONVERSION FACTORS";
    private static final String TASK_SUCCESS_TEXT = "Successfully updated!!!";
    private static final String TASK_MESSAGE = "Updating %1$04d conversion factors";

    public UpdateFactorsInPSTask(int listSize) {
        this.listSize = listSize;
        this.updateTitle(TASK_TITLE);
    }

    @Override
    protected String call() throws Exception {
        try {
            for (int i = 0; i <= listSize; i++) {

                if (i % 20 == 0) {
                    Thread.sleep(1000);
                    updateProgress(i, listSize - 1);
                    updateMessage(String.format(TASK_MESSAGE, i));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.cancel(true);
        }
        return null;
    }

    @Override
    protected void updateMessage(String message) {
        super.updateMessage(message);
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        updateValue(TASK_SUCCESS_TEXT);
    }

    @Override
    protected void updateProgress(long workDone, long max) {
        super.updateProgress(workDone, max);
    }

}
