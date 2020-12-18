import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Properties;

public class TestUtil
{
    static SQLiteDataSource getDataSource()
    {
        Properties properties = new Properties();
        properties.put("auto_vacuum", "full");
        SQLiteConfig config = new SQLiteConfig(properties);
        config.setEncoding(SQLiteConfig.Encoding.UTF8);
        config.enforceForeignKeys(true);
        config.setJournalMode(SQLiteConfig.JournalMode.WAL);
        config.setSynchronous(SQLiteConfig.SynchronousMode.NORMAL);
        SQLiteDataSource dataSource = new SQLiteDataSource(config);
        try
        {
            dataSource.setLogWriter(new PrintWriter(System.out));
        }
        catch (SQLException exception)
        {
            System.err.println(exception.getMessage());
        }
        dataSource.setUrl("jdbc:sqlite:data" + File.separator + "database.db");
        return dataSource;
    }

}
