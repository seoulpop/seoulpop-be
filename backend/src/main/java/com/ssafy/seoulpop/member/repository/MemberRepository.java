package com.ssafy.seoulpop.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.seoulpop.member.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
