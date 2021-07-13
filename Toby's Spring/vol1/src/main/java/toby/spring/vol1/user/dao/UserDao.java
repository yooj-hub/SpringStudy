package toby.spring.vol1.user.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import toby.spring.vol1.user.dao.statementstrategy.AddStatement;
import toby.spring.vol1.user.dao.statementstrategy.DeleteAllStatement;
import toby.spring.vol1.user.dao.statementstrategy.StatementStrategy;
import toby.spring.vol1.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao {
    //    private ConnectionMaker connectionMaker;
    @Autowired
    private JdbcContext jdbcContext;

    @Autowired
    private DataSource dataSource;


    public void setJdbcContext(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    //
//    public UserDao(ConnectionMaker connectionMaker) {
//        this.connectionMaker = connectionMaker;
//    }
//    public UserDao(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }

    public UserDao() {
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addUser(final User user) throws SQLException {
        this.jdbcContext.workWithStatementStrategy(c -> {
            PreparedStatement ps = c.prepareStatement("insert into users(id,name,password) values(?,?,?)"
            );
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            return ps;
        });
    }

//    public void addUser(final User user) throws SQLException {
//        jdbcContextWithStatementStrategy(c -> {
//            PreparedStatement ps = c.prepareStatement("insert into users(id,name,password) values(?,?,?)"
//            );
//            ps.setString(1, user.getId());
//            ps.setString(2, user.getName());
//            ps.setString(3, user.getPassword());
//            return ps;
//        });
//    }


//
//    public void addUser(final User user) throws SQLException {
//        jdbcContextWithStatementStrategy(new StatementStrategy() {
//            @Override
//            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//                PreparedStatement ps = c.prepareStatement("insert into users(id,name,password) values(?,?,?)"
//                );
//                ps.setString(1, user.getId());
//                ps.setString(2, user.getName());
//                ps.setString(3, user.getPassword());
//                return ps;
//            }
//        };
//    }
//    public void addUser(final User user) throws SQLException {
//        StatementStrategy stmt = c -> {
//            PreparedStatement ps = c.prepareStatement("insert into users(id,name,password) values(?,?,?)"
//            );
//            ps.setString(1, user.getId());
//            ps.setString(2, user.getName());
//            ps.setString(3, user.getPassword());
//            return ps;
//        };
//        jdbcContextWithStatementStrategy(stmt);
//    }
//    public void addUser(final User user) throws SQLException {
//        class AddStatement implements StatementStrategy {
//            @Override
//            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//                PreparedStatement ps =
//                        c.prepareStatement("insert into users(id,name,password) values(?,?,?)"
//                        );
//                ps.setString(1, user.getId());
//                ps.setString(2, user.getName());
//                ps.setString(3, user.getPassword());
//                return ps;
//            }
//        }
//        StatementStrategy stmt = new AddStatement();
//        jdbcContextWithStatementStrategy(stmt);
//    }
//    public void addUser(User user) throws SQLException {
//        StatementStrategy stmt = new AddStatement(user);
//        jdbcContextWithStatementStrategy(stmt);
//    }


//    public void addUser(User user) throws ClassNotFoundException, SQLException {
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection c = dataSource.getConnection();
//        PreparedStatement ps = c.prepareStatement(
//                "insert into users(id,name,password) values(?,?,?)"
//        );
//        ps.setString(1, user.getId());
//        ps.setString(2, user.getName());
//        ps.setString(3, user.getPassword());
//        ps.executeUpdate();
//        ps.close();
//        c.close();
//    }

    public void deleteByName(String name) throws SQLException, ClassNotFoundException {
//        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement(
                "delete from users where id = ?"
        );
        ps.setString(1, name);
        ps.executeUpdate();
        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
//        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ?"
        );
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();

        User user = null;
        if (rs.next()) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        rs.close();
        ps.close();
        c.close();
        if (user == null) throw new EmptyResultDataAccessException(1);

        return user;

    }

//    public void executeSql(final String query) throws SQLException {
//        this.jdbcContext.workWithStatementStrategy(c -> c.prepareStatement(query));
//    }


//    public void deleteAll2() throws SQLException {
//        executeSql("delete from users");
//    }
    public void deleteAll() throws SQLException{ this.jdbcContext.executeSql("delete from users"); }


//    public void deleteAll() throws SQLException {
//        this.jdbcContext.workWithStatementStrategy(c -> c.prepareStatement("delete from users"));
//    }

//    public void deleteAll2() throws SQLException {
//        this.jdbcContext.workWithStatementStrategy(new StatementStrategy() {
//            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//                return c.prepareStatement("delete from users");
//            }
//        });
//    }
//
//
//    public void deleteAll() throws SQLException {
//        jdbcContextWithStatementStrategy(c -> {
//            PreparedStatement ps = c.prepareStatement("delete from users");
//            return ps;
//        });
//    }

//    public void deleteAll() throws SQLException {
//        Connection c = null;
//        PreparedStatement ps = null;
////        try {
////        c = dataSource.getConnection();
//        StatementStrategy strategy = new DeleteAllStatement();
//        jdbcContextWithStatementStrategy(strategy);
//    }


//            ps = c.prepareStatement("delete from users");
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            throw e;
//        } finally {
//            if (ps != null) {
//                try {
//                    ps.close();
//                } catch (SQLException e) {
//
//                }
//            }
//            if (c != null) {
//                try {
//                    c.close();
//                } catch (SQLException e) {
//
//                }
//            }
//
//        }


    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = dataSource.getConnection();

            ps = c.prepareStatement("select count(*) from users");
            rs = ps.executeQuery();
            rs.next();

            return rs.getInt(1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {

                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {

                }
            }

        }
//        Connection c = dataSource.getConnection();
//        PreparedStatement ps = c.prepareStatement("select count(*) from users");
//        ResultSet rs = ps.executeQuery();
//        rs.next();
//        int count = rs.getInt(1);
//        rs.close();
//        ps.close();
//        c.close();

    }

    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
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
                } catch (SQLException e) {

                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {

                }
            }

        }

    }


}



