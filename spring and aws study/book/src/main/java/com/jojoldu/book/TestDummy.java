package com.jojoldu.book;

import com.jojoldu.book.service.PostsService;
import com.jojoldu.book.web.dto.PostsSaveRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;

@Component
public class TestDummy {

    @Component
    static class Dummy {
        public Dummy(PostsService postsService) {
            this.postsService = postsService;
        }

        @Autowired
        private PostsService postsService;

        @PostConstruct
        public void init() {
            postsService.save(PostsSaveRequestDto.builder()
                    .title("테스트1")
                    .content("테스트 본문1")
                    .author("테스트 계정1").build());
            postsService.save(PostsSaveRequestDto.builder()
                    .title("테스트2")
                    .content("테스트 본문2")
                    .author("테스트 계정2").build());

        }


    }
}
