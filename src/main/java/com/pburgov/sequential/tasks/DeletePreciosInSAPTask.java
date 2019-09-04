package com.pburgov.sequential.tasks;

public class DeletePreciosInSAPTask extends AbstractCustomTask <String> {

    private int listSize;
    private static final String TASK_TITLE = "DELETING PRICES IN ERP";
    private static final String TASK_MESSAGE = "Deleting the price for the Item Nº: A-%1$05d";

    public DeletePreciosInSAPTask( int listSize ) {
        super(TASK_TITLE);
        this.listSize = listSize;
    }

    @Override
    protected String call() throws Exception {
        try {
            for ( int i = 0; i <= listSize; i++ ) {
                Thread.sleep(100);
                updateProgress(i, listSize);
                String message = String.format(TASK_MESSAGE, i);
                if ( i == 25 ) {
                    throw new RuntimeException("Custom Exception: " + getClass().getSimpleName() +
                                                   "\nIn order to show how it works");
                }
                updateMessage(message);
            }
        } catch ( Exception ex ) {
            this.setThrowable(ex);
            this.cancel(true);
            ex.printStackTrace();
        }
        return null;
    }

}
