package com.ivis.duary.service;

import com.ivis.duary.api.MemberApi;
import com.ivis.duary.data.MemberReq;
import com.ivis.duary.data.MemberRes;
import org.springframework.data.domain.Page;

public interface MemberService {
    Page<MemberRes> list(MemberApi.SearchRequest request);


    MemberRes update(Long id, MemberReq req);
}
