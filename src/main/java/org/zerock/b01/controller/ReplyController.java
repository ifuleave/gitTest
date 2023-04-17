package org.zerock.b01.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.ReplyDTO;
import org.zerock.b01.service.ReplyService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RestController //jsp나 Thymeleaf로 전송되는게 아니라 바로  Json 이나 xml로 처리
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor // 의존성주입을 위한 
public class ReplyController {
    private final ReplyService replyService; //replyService 주입
    

    //등록 기능 확인 
    @ApiOperation(value = "Replies POST", notes = "POST 방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> register(@Valid @RequestBody ReplyDTO replyDTO, BindingResult bindingResult) throws BindException{
        log.info(replyDTO);

        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = new HashMap<>();
        Long rno = replyService.register(replyDTO);
        resultMap.put("rno",rno);
        return resultMap;
    }
    
    @ApiOperation(value = "Replies of Board", notes = "GET 방식으로 특정 게시물의 댓글 목록")
    @GetMapping(value = "/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO){
                                            // 호출하는 경로의 값을 파라미터의 변수로 처리할 수 있는 방법을 제공
        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno,pageRequestDTO);

        return responseDTO;
    }
    
    //특정한 댓글을 조회할 떄 Reply의 rno를 경로로 이용해서 Get 방식으로 처리한다
    //웹브라우저에 댓글 번호 입력
    @ApiOperation(value = "Read Reply",notes = "GET 방식으로 특정 댓글 조회")
    @GetMapping("/{rno}")
    public ReplyDTO getReplyDTO(@PathVariable("rno") Long rno){
         ReplyDTO replyDTO = replyService.read(rno);
         return replyDTO;
    }
    
    //삭제
    @ApiOperation(value = "Delete Reply", notes = "DELETE 방식으로 특정 댓글 삭제")
    @DeleteMapping("/{rno}")
    public Map<String,Long> remove(@PathVariable("rno") Long rno){
        replyService.remove(rno);
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);
        return resultMap;
    }

    //특정 댓글 수정
    @ApiOperation(value = "Modify Reply", notes = "PUT 방식으로 특정 댓글 수정")
    @PutMapping(value = "/{rno}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> remove(@PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO){
                                                                //  jason 문자열이 전송되므로 이를 처리
            replyDTO.setRno(rno); //번호를 일치시킴
            replyService.modify(replyDTO);
            Map<String, Long> resultMap = new HashMap<>();
            resultMap.put("rno",rno);
            return resultMap;
    }

}
