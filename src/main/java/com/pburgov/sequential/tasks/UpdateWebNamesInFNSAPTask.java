package com.pburgov.sequential.tasks;

public class UpdateWebNamesInFNSAPTask extends AbstractCustomTask<String> {

    private int listSize;
    private static final String TASK_TITLE = "UPDATING ITEM DESCRIPTION";
    private static final String TASK_MESSAGE = "Updating description for the Item Nº: A-%1$05d";

    public UpdateWebNamesInFNSAPTask( int listSize ) {
        super(TASK_TITLE);
        this.listSize = listSize;
    }

    @Override
    protected String call() throws Exception {
        try {
            for ( int i = 0; i <= listSize; i++ ) {
                Thread.sleep(150);
                updateProgress(i, listSize);
                String message = String.format(TASK_MESSAGE, i);
                if(i==10){
                    throw  new RuntimeException("Custom Exception: " + getClass().getSimpleName());
                }
                updateMessage(message);
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();
            this.setThrowable(ex);
            this.cancel(true);
        }
        return null;
    }
}


