package com.pburgov.sequential.tasks;

public class UpdateFactorsInPSTask extends AbstractCustomTask<String>  {

    private int listSize;
    private static final String TASK_TITLE = "UPDATING CONVERSION FACTORS";
    private static final String TASK_MESSAGE = "Updating %1$04d conversion factors";

    public UpdateFactorsInPSTask(int listSize) {
        super(TASK_TITLE);
        this.listSize = listSize;

    }

    @Override
    protected String call() throws Exception {
        try {
            for (int i = 0; i <= listSize; i++) {
                if (i % 20 == 0) {
                    Thread.sleep(500);
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



}
