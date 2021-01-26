package lark.task.data;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

public class ExecuteLogger {
    private final int id;
    private final long time;
    private int rows;
    private final StringBuilder builder;

    public ExecuteLogger(int id, long time ) {
        this.id = id;
        this.time = time;
        this.rows = 0;
        this.builder = new StringBuilder( 64 );
    }

    public void info( String format, Object... arg ) {
        FormattingTuple tuple = MessageFormatter.format( format, arg );
        builder.append( tuple.getMessage() );
        builder.append( System.lineSeparator() );
        ++rows;
    }

    public String getLog() {
        return builder.toString();
    }

    public int getRows() {
        return rows;
    }
}
