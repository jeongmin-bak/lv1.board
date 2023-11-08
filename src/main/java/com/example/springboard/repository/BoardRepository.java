package com.example.springboard.repository;

import com.example.springboard.dto.BoardResponseDto;
import com.example.springboard.entity.Board;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class BoardRepository {
    private final JdbcTemplate jdbcTemplate;

    public BoardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Board save(Board board) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // board 날짜
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM");
        board.setDate(dateTime.format(formatter));

        String sql = "INSERT INTO board (title, username, password, contents, date) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, board.getTitle());
                    preparedStatement.setString(2, board.getUsername());
                    preparedStatement.setString(3, board.getPassword());
                    preparedStatement.setString(4, board.getContents());
                    preparedStatement.setString(5, board.getDate());
                    return preparedStatement;
                },
                keyHolder);

        Long id = keyHolder.getKey().longValue();
        board.setId(id);

        return board;
    }

    public List<BoardResponseDto> findAll() {
        // DB 조회
        String sql = "SELECT * FROM board order by date desc";

        return jdbcTemplate.query(sql, new RowMapper<BoardResponseDto>() {
            @Override
            public BoardResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String title = rs.getString("title");
                String username = rs.getString("username");
                String contents = rs.getString("contents");
                String date = rs.getString("date");
                return new BoardResponseDto(id, title, username, contents, date);
            }
        });
    }

    public void delete(Long id) {
        String sql = "DELETE FROM board WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Board findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM board WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                Board board = new Board();
                board.setTitle(resultSet.getString("title"));
                board.setUsername(resultSet.getString("username"));
                board.setContents(resultSet.getString("contents"));
                board.setContents(resultSet.getString("date"));
                return board;
            } else {
                return null;
            }
        }, id);
    }



}
