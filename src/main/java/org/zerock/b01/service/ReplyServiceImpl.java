package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Reply;
import org.zerock.b01.dto.ReplyDTO;
import org.zerock.b01.repository.ReplyRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService {
    //repluRepository  와 modelmapper를 주입받아 사용
    private final ReplyRepository replyRepository;
    private  final ModelMapper modelMapper;

    @Override
    public Long register(ReplyDTO replyDTO){
        Reply reply = modelMapper.map(replyDTO, Reply.class);
        Long rno = replyRepository.save(reply).getRno();
        return rno;
    }
    
    //service에 있는거 구현
    @Override
    public ReplyDTO read(Long rno){
        Optional<Reply> replyOptional = replyRepository.findById(rno);
        Reply reply = replyOptional.orElseThrow();
        return modelMapper.map(reply, ReplyDTO.class);
    }

    @Override
    public void modify(ReplyDTO replyDTO){
        Optional<Reply> replyOptional = replyRepository.findById(replyDTO.getRno());
        Reply reply = replyOptional.orElseThrow();
        reply.changeText(replyDTO.getReplyText());//댓글의 내용만 수정가능
        replyRepository.save(reply);
    }

    @Override //댓글 삭제
    public void remove(Long rno){
        replyRepository.deleteById(rno);
    }

}
