package com.jojoldu.book.domain.posts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostRepositoryTest {
    @Autowired
    PostsRepository postRepository;

    @AfterEach
    public void cleanUp() {
        postRepository.deleteAll();
    }

    @Test
    void 게시글저장_불러오기() throws Exception {
        //given
        String title = "테스트 게시글";
        String content = "테스트 본문";
        postRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("jojoldu@gmail.com")
                .build());

        //when
        List<Posts> postsList = postRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
        assertThat(posts.getAuthor()).isEqualTo("jojoldu@gmail.com");
    }

}
