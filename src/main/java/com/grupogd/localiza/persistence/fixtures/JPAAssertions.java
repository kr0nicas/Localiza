/**
 * Created By: jorge.ochoa
 * Creation date: 14/01/2014
 */
package com.grupogd.localiza.persistence.fixtures;

import static junit.framework.TestCase.fail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;

import org.hibernate.internal.SessionImpl;
import org.hibernate.jdbc.Work;

/**
 * @author jorge.ochoa
 * 
 */
public class JPAAssertions {

	public static void assertTableHasColumn(EntityManager manager, final String tableName, final String columnName) {
		SessionImpl session = manager.unwrap(SessionImpl.class);

		final ResultCollector rc = new ResultCollector();

		session.doWork(new Work() {

			@Override
			public void execute(Connection connection) throws SQLException {
				// TODO Auto-generated method stub
				ResultSet columns = connection.getMetaData().getColumns(null, null, tableName.toUpperCase(), null);
				while (columns.next()) {
					if (columns.getString(4).toUpperCase().equals(columnName.toUpperCase())) {
						rc.found = true;
					}
				}
			}

		});

		if (!rc.found) {
			fail("Column [" + columnName + "] not found on table [" + tableName + "]");
		}
	}

	public static void assertTableExists(EntityManager manager, final String tableName) {
		SessionImpl session = manager.unwrap(SessionImpl.class);

		final ResultCollector rc = new ResultCollector();

		session.doWork(new Work() {

			@Override
			public void execute(Connection connection) throws SQLException {
				// TODO Auto-generated method stub
				ResultSet tables = connection.getMetaData().getTables(null, null, "%", null);

				while (tables.next()) {
					if (tables.getString(3).toUpperCase().equals(tableName.toUpperCase())) {
						rc.found = true;
					}
				}
			}
		});

		if (!rc.found) {
			fail("Table not found in schema :" + tableName);
		}
	}

	static class ResultCollector {
		public boolean found = false;
	}

}
