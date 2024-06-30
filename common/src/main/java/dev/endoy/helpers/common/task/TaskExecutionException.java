package dev.endoy.helpers.common.task;

public class TaskExecutionException extends RuntimeException
{

    public TaskExecutionException( String message )
    {
        super( message );
    }

    public TaskExecutionException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public TaskExecutionException( Throwable cause )
    {
        super( cause );
    }
}
