package com.back.boundedContext.member.app;


import com.back.boundedContext.member.domain.Member;
import com.back.boundedContext.member.out.MemberRepository;
import com.back.global.eventPublisher.EventPublisher;
import com.back.global.exception.DomainException;
import com.back.global.rsData.RsData;
import com.back.shared.member.dto.MemberDto;
import com.back.shared.member.event.MemberJoinedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberJoinUseCase {
    private final MemberRepository memberRepository;
    private final EventPublisher eventPublisher;

    public RsData<Member> join(String username, String password, String nickname) {
        memberRepository.findByUsername(username).ifPresent(m -> {
            throw new DomainException("409-1", "이미 존재하는 username 입니다.");
        });

        Member member =  memberRepository.save(new Member(username, password, nickname));

        eventPublisher.publish(new MemberJoinedEvent(new MemberDto(member)));

        return new RsData<>(
                "201 - 1",
                "%d번 회원이 생성되었습니다.".formatted(member.getId()),
                member
        );
    }
}