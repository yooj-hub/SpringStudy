package toby.spring.vol1.user.dao;


import toby.spring.vol1.user.dao.statementstrategy.StatementStrategy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    //StatementStrategy 의 makePreparedStatement 는 ps를 리턴한다.
    public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = dataSource.getConnection();

            ps = stmt.makePreparedStatement(c);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ignored) {
                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException ignored) {
                }
            }
        }

    }

    public void executeSql(final String query) throws SQLException { workWithStatementStrategy((Connection c) -> c.prepareStatement(query)); }
}
