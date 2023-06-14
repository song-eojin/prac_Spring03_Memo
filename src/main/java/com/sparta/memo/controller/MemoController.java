package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MemoController {
    private final Map<Long, Memo> memoList = new HashMap<>();

    //메모 생성하기 기능 (POST)
    @PostMapping("memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) { //데이터가 넘어오는 형태가 Body 부분에 JSON 형태이므로 매개변수의 타입은 @RequestBody 이어야 한다.

        //RequestDto -> Entity 로 변경
        //데이터로 저장하기 위해서
        Memo memo = new Memo(requestDto);

        //Memo 의 MaxId 값을 찾기
        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1 ;
        memo.setId(maxId);

        //DB 저장
        memoList.put(memo.getId(), memo);

        //Entity -> ResponseDto 로 변경
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }


    //메모 조회하기 기능 (GET)
    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        List<MemoResponseDto> responseList = memoList.values().stream().map(com.sparta.memo.dto.MemoResponseDto::new).toList();

        return responseList;
    }


    //
}
