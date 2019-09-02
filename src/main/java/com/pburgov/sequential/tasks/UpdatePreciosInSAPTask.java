package com.pburgov.sequential.tasks;

public class UpdatePreciosInSAPTask extends AbstractCustomTask<String>  {

    private int listSize;
    private static final String TASK_TITLE = "UPDATING ITEM PRICES IN ERP";
    private static final String TASK_MESSAGE = "Updating the price for the Item Nº: A-%1$05d";

    public UpdatePreciosInSAPTask(int listSize) {
        super(TASK_TITLE);
        this.listSize = listSize;
    }

    @Override
    protected String call() throws Exception {
        try {
            for (int i = 0; i <= listSize; i++) {
                Thread.sleep(125);
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


}
