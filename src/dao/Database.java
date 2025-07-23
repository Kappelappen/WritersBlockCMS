package dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.sql.*;

public abstract class Database {

    public static String DB_URL;  
    private static Connection connection;

    public static void prepareDatabaseFile() {
    	
        try {
            String userHome = System.getProperty("user.home");
            Path dbDir = Paths.get(userHome, "writersblockcms"); 
            Files.createDirectories(dbDir);
            Path dbFile = dbDir.resolve("writersblockcms.db");

            DB_URL = "jdbc:sqlite:" + dbFile.toAbsolutePath().toString();
            System.out.println("Database file path set to: " + DB_URL);

        } catch (Exception e) {
            System.err.println("Error preparing database directory:");
            e.printStackTrace();
        }
    }

    public static void initializeDatabase() {
        prepareDatabaseFile();  // Viktigt att anropa här så sökväg sätts innan connect()

        try {
            connect();

            if (!tablesExist()) {
                runSqlFile("main.sql");
                System.out.println("Database has been created.");
            } else {
                System.out.println("Tables already exist");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void connect() {
        if (connection == null) {
            try {
                loadDriver();

                if (DB_URL == null) {
                    throw new IllegalStateException("Database file path not set. Call prepareDatabaseFile() first.");
                }

                connection = DriverManager.getConnection(DB_URL);

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void runSqlFile(String resourcePath) {
        try (
            InputStream is = Database.class.getClassLoader().getResourceAsStream(resourcePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            Statement stmt = connection.createStatement()
        ) {
            if (is == null) {
                System.err.println("Could not locate sql file: " + resourcePath);
                return;
            }

            StringBuilder sqlBuilder = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("--") || line.startsWith("//")) continue;

                sqlBuilder.append(line).append(" ");
                if (line.endsWith(";")) {
                    String sql = sqlBuilder.toString();
                    stmt.execute(sql);
                    sqlBuilder.setLength(0);
                }
            }

        } catch (Exception e) {
            System.err.println("Error running SQL file:");
            e.printStackTrace();
        }
    }

    private static boolean tablesExist() throws SQLException {
    	
        String checkSql = "SELECT name FROM sqlite_master " + 
        "WHERE type='table' AND name='article';";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
            return rs.next();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
