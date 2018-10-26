package org.schabi.newpipe.extractor.exceptions;

public class StackException extends Exception {
    public StackException(Exception...exceptions) {
        super(buildMessage(exceptions), buildThrowable(exceptions));
    }

    protected static String buildMessage(Exception...exceptions) {
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 0; i < exceptions.length; i++) {
            Exception exception = exceptions[i];
            messageBuilder.append('[').append(i).append(']').append(exception.getMessage()).append('\n');
            messageBuilder.append('[').append(i).append(']').append(exception.getLocalizedMessage()).append('\n');
        }
        return messageBuilder.toString();
    }

    protected static Throwable buildThrowable(Exception...exceptions) {
        Throwable throwable = new Throwable("Stack");
        for (Exception exception:exceptions) {
            throwable.addSuppressed(exception.getCause());
        }
        return throwable;
    }
}
