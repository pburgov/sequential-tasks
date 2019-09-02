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
                if(i==25){
                    throw  new RuntimeException("Custom Exception");
                }
                updateMessage(message);
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();
            this.cancel(true);
        }
        return null;
    }
}


