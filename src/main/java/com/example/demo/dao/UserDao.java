package com.example.demo.dao;

import com.example.demo.model.User;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDao {

    private final JdbcTemplate jdbc;

    public UserDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<User> USER_MAPPER = (rs, rowNum) ->
            new User(rs.getLong("id"), rs.getString("username"));

    public User create(User user) {
        // SQL insert with returning id
        String sql = "INSERT INTO users (username) VALUES (?) RETURNING id";
        Long id = jdbc.queryForObject(sql,
                new Object[]{user.getUsername()},
                Long.class);
        user.setId(id);
        return user;
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbc.update(sql, id);
    }

    public User findById(Long id) {
        String sql = "SELECT id, username FROM users WHERE id = ?";
        return jdbc.queryForObject(sql,
                new Object[]{id},
                USER_MAPPER);
    }

    public List<User> findAll() {
        String sql = "SELECT id, username FROM users";
        return jdbc.query(sql, USER_MAPPER);
    }

    public int deleteAll() {
        return jdbc.update("DELETE FROM users");
    }

    /* ---------- Batch insert  ---------- */
    public int[] batchInsert(List<User> users) {
        String sql = "INSERT INTO users (username) VALUES (?)";
        return jdbc.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, users.get(i).getUsername());
            }

            @Override
            public int getBatchSize() {
                return users.size();
            }
        });
    }
}