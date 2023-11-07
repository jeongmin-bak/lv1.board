package com.example.springboard.controller;

import com.example.springboard.dto.BoardRequestDto;
import com.example.springboard.dto.BoardResponseDto;
import com.example.springboard.entity.Board;
import lombok.val;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/board")
public class BoardController {
    private final Map<Long, Board> boardList = new HashMap<>();
    @PostMapping("/create")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto){
        // requestdto -> entity 로
        Board board = new Board(requestDto);

        // board max id
        Long maxId = boardList.size() > 0 ? Collections.max(boardList.keySet())+1 : 1;
        board.setId(maxId);

        // board 날짜
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM");
        board.setDate(dateTime.format(formatter));

        boardList.put(board.getId(), board);
        BoardResponseDto boardResponseDto = new BoardResponseDto(board);

        return boardResponseDto;
    }

    // 목록보기
    @GetMapping("/list")
    public List<BoardResponseDto> getLists(){
        List<BoardResponseDto> responseList = boardList.values().stream()
                .map(BoardResponseDto::new).toList();

        return responseList;
    }

    //게시물 수정하기
    @PutMapping("/list/{id}")
    public Long updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto){
        // 해당 메모가 DB에 존재하는지 확인
        if(boardList.containsKey(id)){
            Board board = boardList.get(id);

            //수정
            board.update(requestDto);
            return board.getId();
        }else{
            throw new IllegalStateException("선택한 메모는 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/detail/{id}")
    public Long deleteMemo(@PathVariable Long id){
        if(boardList.containsKey(id)){
            boardList.remove(id);
            return id;
        }else{
            throw new IllegalStateException("선택한 메모는 존재하지 않습니다.");
        }
    }


}
