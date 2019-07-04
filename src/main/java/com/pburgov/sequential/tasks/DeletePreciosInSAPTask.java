package com.pburgov.sequential.tasks;

import javafx.concurrent.Task;

public class DeletePreciosInSAPTask extends Task<String> {

    private int listSize;
    private static final String TASK_TITLE = "DELETING PRICES IN ERP";
    private static final String TASK_SUCCESS_TEXT = "Successfully deleted!!!";
    private static final String TASK_MESSAGE = "Deleting the price for the Item Nº: A-%1$05d";

    public DeletePreciosInSAPTask(int listSize) {
        this.listSize = listSize;
        this.updateTitle(TASK_TITLE);
    }

    @Override
    protected String call() throws Exception {
        try {
            for (int i = 0; i <= listSize; i++) {
                Thread.sleep(100);
                updateProgress(i, listSize);
                String message = String.format(TASK_MESSAGE, i);
                updateMessage(message);
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
