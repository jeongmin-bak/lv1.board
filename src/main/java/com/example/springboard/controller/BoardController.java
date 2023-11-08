package com.example.springboard.controller;

import com.example.springboard.dto.BoardRequestDto;
import com.example.springboard.dto.BoardResponseDto;
import com.example.springboard.entity.Board;
import com.example.springboard.service.BoardService;
import lombok.val;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;


@RestController
@RequestMapping("/board")
public class BoardController {
    private final JdbcTemplate jdbcTemplate;

    public BoardController(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/create")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto){
        // requestdto -> entity 로
        Board board = new Board(requestDto);

        //DB 저장
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

        // Entity -> ResponseDto
        BoardResponseDto boardResponseDto = new BoardResponseDto(board);

        return boardResponseDto;
    }

    // 목록보기
    @GetMapping("/list")
    public List<BoardResponseDto> getLists(){
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

    //게시물 수정하기
    @PutMapping("/list/{id}")
    public Long updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto){
        Board board = findById(id);
        if(board != null) {
            // memo 내용 수정
            // 비밀번호 유효성 검사
            if (isValidPassword(id, requestDto.getPassword())){
                String sql = "UPDATE board SET title = ?, username = ?, contents = ? WHERE id = ?";
                jdbcTemplate.update(sql, requestDto.getTitle(), requestDto.getUsername(), requestDto.getContents(), id);
                return id;
            }else{
                throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
            }

        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/detail/{id}")
    public Long deleteMemo(@PathVariable Long id){
        BoardService boardService = new BoardService(jdbcTemplate);
        return boardService.deleteBoard(id);
    }

    private Board findById(Long id) {
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

    private boolean isValidPassword(Long id, String password){
        // DB 조회
        String sql = "SELECT * FROM board WHERE id = ? and password = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                return true;
            } else {
                return false;
            }
        }, id, password);
    }


}
