package com.example.attractionReservationSystem.service;

import com.example.attractionReservationSystem.dao.Member;
import com.example.attractionReservationSystem.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MemberService {
    private MemberRepository memberRepository;

    public List<Member> getMemberList() {
        return memberRepository.findAll();
    }



}
