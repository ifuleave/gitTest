package org.zerock.b01.service;

import org.zerock.b01.dto.*;

public interface BoardService {
    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO); //수정

    void remove(Long bno);//삭제

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
    //댓글의 숫자까지 처리
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

}
