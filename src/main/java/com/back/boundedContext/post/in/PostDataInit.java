package com.back.boundedContext.post.in;

import com.back.boundedContext.post.app.PostFacade;
import com.back.boundedContext.post.domain.Post;
import com.back.boundedContext.post.domain.PostMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Slf4j
public class PostDataInit {
    private final PostDataInit self;
    private final PostFacade postFacade;

    public PostDataInit(@Lazy PostDataInit self, PostFacade postFacade) {
        this.self = self;
        this.postFacade = postFacade;
    }

    @Bean
    @Order(2)
    public ApplicationRunner postDataInitApplicationRunner() {
        return args -> {
            self.makeBasePosts();
            self.makeBasePostComments();
        };
    }

    @Transactional
    public void makeBasePosts() {
        if (postFacade.count() > 0) return;

        PostMember user1Member = postFacade.findMemberByUsername("user1").get();
        PostMember user2Member = postFacade.findMemberByUsername("user2").get();
        PostMember user3Member = postFacade.findMemberByUsername("user3").get();

        Post post1 = postFacade.write(user1Member, "제목1", "내용1").getData();
        Post post2 = postFacade.write(user1Member, "제목2", "내용2").getData();
        Post post3 = postFacade.write(user1Member, "제목3", "내용3").getData();
        Post post4 = postFacade.write(user2Member, "제목4", "내용4").getData();
        Post post5 = postFacade.write(user2Member, "제목5", "내용5").getData();
        Post post6 = postFacade.write(user3Member, "제목6", "내용6").getData();
    }

    @Transactional
    public void makeBasePostComments() {
        Post post1 = postFacade.findById(1).get();
        Post post2 = postFacade.findById(2).get();
        Post post3 = postFacade.findById(3).get();
        Post post4 = postFacade.findById(4).get();
        Post post5 = postFacade.findById(5).get();
        Post post6 = postFacade.findById(6).get();

        PostMember user1Member = postFacade.findMemberByUsername("user1").get();
        PostMember user2Member = postFacade.findMemberByUsername("user2").get();
        PostMember user3Member = postFacade.findMemberByUsername("user3").get();

        if (post1.hasComments()) return;

        post1.addComment(user1Member, "댓글1");
        post1.addComment(user2Member, "댓글2");
        post1.addComment(user3Member, "댓글3");

        post2.addComment(user2Member, "댓글4");
        post2.addComment(user2Member, "댓글5");

        post3.addComment(user3Member, "댓글6");
        post3.addComment(user3Member, "댓글7");

        post4.addComment(user1Member, "댓글8");
    }
}