package com.sparta.memo.entity;

//entity 패키지 : 데이터베이스와 소통할 때 사용하는 패키지

import com.sparta.memo.dto.MemoRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Memo {
    private Long id;
    private String username;
    private String contents;

    //클라이언트에게서 받아온 memoRequestDto 즉, 데이터를 현재 클래스 필드들에 각각 저장해주는 생성자
    public Memo(MemoRequestDto memoRequestDto) {
        this.username = memoRequestDto.getUsername();
        this.contents = memoRequestDto.getContents();
    }

    //메모 변경하기 기능을 위한 메서드
    public void update(MemoRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
    }
}
