package com.example.twoproject.controller;

import com.example.twoproject.dto.ArticleForm;
import com.example.twoproject.entity.Article;
import com.example.twoproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Slf4j
@Controller
public class ArticleController {
    // @Autowired -------------------------------
    @Autowired
    private ArticleRepository articleRepository;


    // @PostMapping -------------------------------
    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form) {
        log.info(form.toString());
        // System.out.println(form.toString());

        // 1. DTO를 엔티티로 변환
        Article article = form.toEntity();
        log.info(article.toString());
        // System.out.println(article.toString());

        // 2. 리파지터리로 엔티티를 DB에 저장
        Article saved = articleRepository.save(article);
        log.info(saved.toString());
        // System.out.println(saved.toString());

        //리다이렉트 작성할 위치
        return "redirect:/articles/" + saved.getId();

        //목록페이지로 재요청
    }


    // @GetMapping("/articles/new") ------------------
    @GetMapping("/articles/new")
    public String newArticleForm() {
        return "articles/new";
    }


    // @GetMapping("/articles/{id}") ------------------
    @GetMapping("/articles/{id}") // 데이터 조회 요청 접수
    public String show(@PathVariable Long id,Model model) { // 매개변수로 id 받아오기
        log.info("id = " + id); // id를 잘 받았는지 확인하는 로그 찍기

        // 1. id를 조회하여 데이터 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);

        // 2. 모델에 데이터 등록하기
        model.addAttribute("article", articleEntity);

        // 3. 뷰 페이지 반환하기
        return "articles/show"; //목록으로 돌아가기 링크를 넣을 뷰 파일 확인함.

    }

    @GetMapping("/articles")
    public String index(Model model){
        //1.모든 데이터 가져오기->  DB에서 모든 Article 데이터 가져오기!
        List<Article>  articleEntityList = articleRepository.findAll();

        //2.모델에 데이터 등록하기-> 가져온 Article 묶음을 모델에 등록!
        model.addAttribute("articleList",articleEntityList);

        //3.뷰 페이지 설정하기-> 사용자에게 보여 줄 뷰 페이지 설정!
        return "articles/index";/**/
    }

    @GetMapping("/articles/{id}/edit")  //url
    public String edit(@PathVariable Long id,Model model){   //id를 매개변수로 받아오기
        //수정할 데이터 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);
        //모델에 데이터 등록하기
        model.addAttribute("article",articleEntity);
        //뷰 페이지
        return "articles/edit";
    }

    @PostMapping("/articles/update") //*오타 있었음 ★★★
    public String update(ArticleForm form){ //매개변수로 DTO 받아오기
        log.info(form.toString());
        //(1) DTO를 엔티티 변환
        Article articleEntity = form.toEntity(); //DTO -> 엔티티로 변환
        log.info(articleEntity.toString()); //엔티티로 잘 변환되었는지

        //(2) 엔티티를 DB에 저장
        //(2-1).DB에서 기존 데이터 가져오기
        Article target = articleRepository.
                findById(articleEntity.getId()).orElse(null);

        //(2-2).기존 데이터 값을 갱신하기
        if(target != null){  //*기존의 데이터가 있다면
            //엔티티를 DB에 갱신(저장)
            articleRepository.save(articleEntity);
        }
        //(3) 수정 결과 페이지로 리다이렉트
        return "redirect:/articles/"+articleEntity.getId();
    }

    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr){
        log.info("삭제 요청이 들어왔습니다!!");
        // 1. 삭제할 대상 가져오기
        Article target = articleRepository.findById(id).orElse(null);
        log.info(target.toString());

        // 2. 대상 엔티티 삭제하기
        if(target != null) {    //삭제할 대상이 있는지 확인
            articleRepository.delete(target); // delete()메서드로 대상 삭제
            rttr.addFlashAttribute("msg","삭제됐습니다!");
        }

        // 3. 결과 페이지로 리다이렉트
        return "redirect:/articles";
    }
}