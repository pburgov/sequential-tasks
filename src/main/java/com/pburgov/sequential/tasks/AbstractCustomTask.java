package com.pburgov.sequential.tasks;

import javafx.concurrent.Task;

public abstract class AbstractCustomTask <V> extends Task <String> {

    protected static final String TASK_SUCCESS_TEXT = "Success!!";
    protected static final String TASK_CANCELLED_TEXT = "Cancelled!!";
    protected Boolean hasLink;
    protected Throwable throwable;

    public AbstractCustomTask( String title ) {
        this.updateTitle(title);
        this.hasLink = false;
    }

    @Override
    abstract protected String call() throws Exception;

    @Override
    protected void updateMessage( String message ) {
        super.updateMessage(message);
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        updateValue(TASK_SUCCESS_TEXT);
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        updateValue(TASK_CANCELLED_TEXT);

    }

    @Override
    protected void updateProgress( long workDone, long max ) {
        super.updateProgress(workDone, max);
    }

    public Boolean hasLink() {
        return hasLink;
    }

    protected void setHasLink( Boolean hasLink ) {
        this.hasLink = hasLink;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable( Throwable throwable ) {
        this.throwable = throwable;
    }
}
