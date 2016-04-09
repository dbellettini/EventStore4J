package eu.bellettini.eventstore4j.data;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLUnitOfWork {
    void execute() throws SQLException;
}
