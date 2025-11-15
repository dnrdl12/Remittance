package io.dnrdl12.remittance.service;

import io.dnrdl12.remittance.comm.api.PageDto;
import io.dnrdl12.remittance.dto.MemberDto;

/**
 * packageName    : io.dnrdl12.remittance.service
 * fileName       : MemberService
 * author         : JW.CHOI
 * date           : 2025-11-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-11-12        JW.CHOI              최초 생성
 */

public interface MemberService {

    MemberDto.IdResponse create(MemberDto.CreateReq req, String userId);
    MemberDto.IdResponse update(MemberDto.UpdateReq req, String userId);
    MemberDto.IdResponse deleteSoft(Long memberSeq, String userId);
    MemberDto.Res getById(Long memberSeq, boolean masked);
    PageDto.PageResponse<MemberDto.Res> search(MemberDto.SearchReq req, boolean masked);
}