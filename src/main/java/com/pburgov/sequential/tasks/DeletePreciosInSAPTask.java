package com.pburgov.sequential.tasks;

public class DeletePreciosInSAPTask extends AbstractCustomTask<String> {

    private int listSize;
    private static final String TASK_TITLE = "DELETING PRICES IN ERP";
    private static final String TASK_MESSAGE = "Deleting the price for the Item Nº: A-%1$05d";

    public DeletePreciosInSAPTask(int listSize) {
        super(TASK_TITLE);
        this.listSize = listSize;
    }

    @Override
    protected String call() throws Exception {
        try {
            for (int i = 0; i <= listSize; i++) {
                Thread.sleep(100);
                updateProgress(i, listSize);
                String message = String.format(TASK_MESSAGE, i);
                if(i==25){
                    throw  new RuntimeException("Custom Exception");
                }
                updateMessage(message);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.setMessageError(ex.getMessage());
            this.cancel(true);
        }
        return null;
    }

}
