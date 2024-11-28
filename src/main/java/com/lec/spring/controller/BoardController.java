package com.lec.spring.controller;

import com.lec.spring.domain.Post;
import com.lec.spring.domain.PostValidator;
import com.lec.spring.service.BoardService;
import com.lec.spring.util.U;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        System.out.println("BoardController() 생성");
        this.boardService = boardService;
    }


    @GetMapping("/write")
    public void write(){

    }

    @PostMapping("/write")
    public String writeOk(
            @RequestParam Map<String, MultipartFile> files // 첨부파일들<name, file>
            ,@Valid Post post
            , BindingResult bindingResult    // <- Validator 가 유효성 검사를 마친 결과가 담긴 객체
            , Model model    // 매개변수 선언시 BindingResult 보다 Model 을 뒤에 두어야 한다.
            , RedirectAttributes redirectAttributes    // redirect 시 넘겨줄 값들
    ){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("subject", post.getSubject());
            redirectAttributes.addFlashAttribute("content", post.getContent());

            List<FieldError> errorList = bindingResult.getFieldErrors();
            for(FieldError error : errorList){
                redirectAttributes.addFlashAttribute("error_" + error.getField(), error.getCode());
            }


            return "redirect:/board/write";  // GET
        }

        model.addAttribute("result", boardService.write(post, files));
        return "board/writeOk";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model){
        Post detail = boardService.detail(id);
        model.addAttribute("post", detail);
        return "board/detail";
    }

    @GetMapping("/list")
//    public void list(Model model){
    public void list(Integer page, Model model){
//        model.addAttribute("list", boardService.list());
        boardService.list(page, model);
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model){
        model.addAttribute("post", boardService.selectById(id));
        return "board/update";
    }

    @PostMapping("/update")
    public String updateOk(
            @RequestParam Map<String, MultipartFile> files // 새로 추가될 첨부 파일들.
            ,Long[] delfile // 삭제될 파일들의 아이디
            ,@Valid Post post
            , BindingResult bindingResult
            , Model model
            , RedirectAttributes redirectAttributes
    ){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("subject", post.getSubject());
            redirectAttributes.addFlashAttribute("content", post.getContent());

            List<FieldError> errorList = bindingResult.getFieldErrors();
            for(FieldError error : errorList){
                redirectAttributes.addFlashAttribute("error_" + error.getField(), error.getCode());
            }


            return "redirect:/board/update/" + post.getId();
        }

        model.addAttribute("result", boardService.update(post, files, delfile));   // <- post(id, subject, content)
        return "board/updateOk";
    }

    @PostMapping("/delete")
    public String deleteOk(Long id, Model model){
        model.addAttribute("result", boardService.deleteById(id));
        return "board/deleteOk";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder){
        System.out.println("initBinder() 호출");
        binder.setValidator(new PostValidator());
    }

    // 페이징
    // pageRows 변경시 동작
    @PostMapping("/pageRows")
    public String pageRows(Integer page, Integer pageRows){
        U.getSession().setAttribute("pageRows", pageRows);
        return "redirect:/board/list?page=" + page;
    }

}










