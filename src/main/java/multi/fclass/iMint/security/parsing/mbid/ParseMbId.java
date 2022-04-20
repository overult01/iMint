package multi.fclass.iMint.security.parsing.mbid;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

import multi.fclass.iMint.member.dto.Role;
import multi.fclass.iMint.member.dto.MemberDTO;
import multi.fclass.iMint.security.dao.ISecurityDAO;

/**
 * @author Junming, Yang
 *
 */

// 모듈화
@Component
public class ParseMbId {

	MemberDTO user;
	
	@Autowired
	ISecurityDAO SecurityDAO;
	
	// mbId파싱
	public String parseMbId(Authentication auth) {
		DefaultOAuth2User authorization = (DefaultOAuth2User) auth.getPrincipal();

		String mbId = null;
		String customerId = null;
		
		// 구글(아이 테스트용) 
		if(auth.getName().length() <= 40 & SecurityDAO.findByMbId("google_" + auth.getName().toString()) != null) { 
			mbId = "google_" + auth.getName();
			return mbId;
		}
		
		// 카카오 
		else {
			customerId = authorization.getName();
			mbId = "kakao_" + customerId;
			
			// 네이버 (임시 숫자 크기 지정)
			Map<String, Object> naverMap = null;
			if (customerId.length() >= 40) { 
				naverMap = authorization.getAttributes();
				naverMap = (Map<String, Object>) naverMap.get("response");
				customerId = (String) naverMap.get("id");
				
				mbId = "naver_" + customerId;
			}
			
		}
		
		System.out.println("mbId: " + mbId);

		return mbId;
	}
	
	// mbId를 가진 Member 조회 
	public MemberDTO getMemberMbId(String mbId) {
		MemberDTO user = SecurityDAO.findByMbId(mbId);
        System.out.println("mbId가 " + mbId + "인 유저 조회 결과: " + user);
		return user;
	}
	
	// mbId를 가진 Member의 Role 
	public Role getRoleMbId(String mbId) {
		MemberDTO user = getMemberMbId(mbId);
		Role mbRole = user.getMbRole();
        System.out.println("mbId가 " + mbId + "인 유저의 권한은 " + mbRole);
		return mbRole;
	}
	
}
