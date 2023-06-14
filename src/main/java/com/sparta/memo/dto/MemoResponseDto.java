package com.sparta.memo.dto;

import com.sparta.memo.entity.Memo;
import lombok.Getter;

@Getter
public class MemoResponseDto {
    private Long id;
    private String username;
    private String contents;

    public MemoResponseDto(Memo memo) {
        this.id = memo.getId();
        this.username = memo.getUsername();
        this.contents = memo.getContents();
    }

    //DB 연결 과정에서 필요한 생성자 오버라이딩이었음
    public MemoResponseDto(Long id, String username, String contents) {
        this.id = id;
        this.username = username;
        this.contents = contents;
    }
}