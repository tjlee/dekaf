package org.jetbrains.jdba.jdbc.pooling;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import static java.lang.String.format;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class SimpleDataSource implements DataSource {


  //// STATE \\\\

  @NotNull
  private final String myConnectionString;

  @NotNull
  private final Properties myConnectionProperties;

  @NotNull
  private final Driver myDriver;

  @Nullable
  private PrintWriter myLogWriter;


  //// CONSTRUCTORS \\\\

  public SimpleDataSource(@NotNull final String connectionString,
                          @Nullable final Properties connectionProperties,
                          @NotNull final Driver driver) {
    myConnectionString = connectionString;
    myConnectionProperties = cloneProperties(connectionProperties);
    myDriver = driver;
  }

  @NotNull
  private static Properties cloneProperties(final @Nullable Properties properties) {
    Properties p = new Properties();
    if (properties != null) p.putAll(properties);
    return p;
  }


  //// IMPLEMENTATION \\\\

  @Override
  public Connection getConnection() throws SQLException {
    return myDriver.connect(myConnectionString, myConnectionProperties);
  }

  @Override
  public Connection getConnection(final String username, final String password) {
    throw new IllegalArgumentException("SimpleDataSource.getConnection(username, password) is not supported. You can pass credentials via connection string or via connection properties.");
  }

  @Nullable
  @Override
  public PrintWriter getLogWriter() {
    return myLogWriter;
  }

  @Override
  public void setLogWriter(@Nullable final PrintWriter writer) {
    myLogWriter = writer;
  }

  @Override
  public void setLoginTimeout(final int seconds) {
    // TODO implement SimpleDataSource.setLoginTimeout
    throw new RuntimeException("The SimpleDataSource.setLoginTimeout has not been implemented yet.");
  }

  @Override
  public int getLoginTimeout() {
    // TODO implement SimpleDataSource.getLoginTimeout
    throw new RuntimeException("The SimpleDataSource.getLoginTimeout has not been implemented yet.");
  }

  @Override
  @NotNull
  public <T> T unwrap(@SuppressWarnings("SpellCheckingInspection") final Class<T> iface) {
    if (iface.isAssignableFrom(SimpleDataSource.class)) {
      //noinspection unchecked
      return (T) this;
    }
    else {
      throw new IllegalArgumentException(format("%s is not a wrapper for %s", SimpleDataSource.class.getSimpleName(), iface.getName()));
    }
  }

  @Override
  public boolean isWrapperFor(@SuppressWarnings("SpellCheckingInspection") final Class<?> iface) throws SQLException {
    return iface.isAssignableFrom(SimpleDataSource.class);
  }
}
