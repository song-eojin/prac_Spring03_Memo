package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.service.MemoService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class MemoController {

    //Dummy DB
    //private final Map<Long, Memo> memoList = new HashMap<>();


    //실제 DB 연결 by MySQL
    //MemoService memoService = new MemoService(jdbcTemplate); 중복 제거
    private final MemoService memoService;

    /*데이터를 전달하는 역할만 담당*/
    public MemoController(JdbcTemplate jdbcTemplate) {

        this.memoService = new MemoService(jdbcTemplate);
    }

    //메모 생성하기 기능 (Post : CREATE)
    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
    //데이터가 넘어오는 형태가 Body 부분에 JSON 형태이므로 매개변수의 타입은 @RequestBody 이어야 한다.

        return memoService.createMemo(requestDto);
    }


    //메모 조회하기 기능 (Get : READ)
    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        return memoService.getMemos();
    }


    //메모 변경하기 기능 (Put : UPDATE)
    @PutMapping("/memos/{id}") //UPDATE 하는 API 는 PUT

    //@RequestBody 는 JSON 형식 데이터를 의미
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        return memoService.updateMemo(id, requestDto);
    }


    //메모 삭제하기 기능 (Delete : DELETE)
    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        return memoService.deleteMemo(id);
    }
}
