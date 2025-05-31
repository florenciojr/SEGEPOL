package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexao {
    private static final Logger LOGGER = Logger.getLogger(Conexao.class.getName());
    private static final String URL = "jdbc:mysql://localhost:3306/sigepol_db?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    static {
        try {
            // Registrar o driver explicitamente
            Class.forName("com.mysql.cj.jdbc.Driver");
            LOGGER.info("Driver JDBC registrado com sucesso");
            
            // Configurar para desregistrar o driver ao encerrar
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    DriverManager.deregisterDriver(DriverManager.getDriver(URL));
                    LOGGER.info("Driver JDBC desregistrado com sucesso");
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Falha ao desregistrar driver JDBC", e);
                }
            }));
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Erro ao registrar driver JDBC", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection conectar() {
        try {
            Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            LOGGER.info("Conexão estabelecida com sucesso");
            return conn;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao conectar com o banco de dados", e);
            throw new RuntimeException("Erro na conexão com o banco de dados: ", e);
        }
    }

    public static void fechar(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    LOGGER.fine("Conexão fechada com sucesso");
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Erro ao fechar conexão", e);
            }
        }
    }

    public static void fechar(PreparedStatement stmt) {
        if (stmt != null) {
            try {
                if (!stmt.isClosed()) {
                    stmt.close();
                    LOGGER.fine("Statement fechado com sucesso");
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Erro ao fechar statement", e);
            }
        }
    }

    public static void fechar(ResultSet rs) {
        if (rs != null) {
            try {
                if (!rs.isClosed()) {
                    rs.close();
                    LOGGER.fine("ResultSet fechado com sucesso");
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Erro ao fechar ResultSet", e);
            }
        }
    }

    public static void fecharTudo(Connection conn, PreparedStatement stmt, ResultSet rs) {
        fechar(rs);
        fechar(stmt);
        fechar(conn);
    }

    public static void fechar(Connection conn, PreparedStatement stmt) {
        fechar(stmt);
        fechar(conn);
    }

    public static void fechar(PreparedStatement stmt, ResultSet rs) {
        fechar(rs);
        fechar(stmt);
    }

    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.getAutoCommit() && !conn.isClosed()) {
                    conn.rollback();
                    LOGGER.fine("Rollback executado com sucesso");
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Erro ao executar rollback", e);
            }
        }
    }
}
