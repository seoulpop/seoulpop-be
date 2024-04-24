package com.ssafy.seoulpop.member.repository;

import com.ssafy.seoulpop.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

}
