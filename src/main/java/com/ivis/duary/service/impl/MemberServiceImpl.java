package com.ivis.duary.service.impl;

import com.ivis.duary.api.MemberApi;
import com.ivis.duary.config.exception.CustomException;
import com.ivis.duary.config.response.ResponseCode;
import com.ivis.duary.data.MemberReq;
import com.ivis.duary.data.MemberRes;
import com.ivis.duary.model.Member;
import com.ivis.duary.repository.MemberRepository;
import com.ivis.duary.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Page<MemberRes> list(MemberApi.SearchRequest request) {
        return memberRepository.findAll(request).map(MemberRes::of);
    }

    @Override
    public MemberRes update(Long id, MemberReq req) {
        Member member = memberRepository.findById(id).orElseThrow();
        if (req.getRole() == null) {
            throw new CustomException(ResponseCode.WRONG_REQUEST);
        }
        member.setRole(req.getRole());
        memberRepository.save(member);

        return MemberRes.of(member);
    }
}
