package multi.fclass.iMint.goods.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import multi.fclass.iMint.common.code.ErrorCode;
import multi.fclass.iMint.common.exception.HandlableException;
import multi.fclass.iMint.goods.dao.IGoodsDAO;
import multi.fclass.iMint.goods.dto.GoodsDTO;
import multi.fclass.iMint.goods.dto.GoodsImagesDTO;
import multi.fclass.iMint.goods.service.GoodsServiceImpl;
import multi.fclass.iMint.member.dto.MemberDTO;
import multi.fclass.iMint.security.parsing.mbid.ParseMbId;
import multi.fclass.iMint.wishlist.service.WishlistServiceImpl;

/**
 * @author Seongil, Yoon
 *
 */
@Controller
public class GoodsCotroller {
	@Autowired
	GoodsServiceImpl goodsSevice;
	@Autowired
	WishlistServiceImpl wishService;

	@Autowired
	ParseMbId parseService;
	
	@Autowired
	IGoodsDAO goodsDAO;

	@GetMapping("goods/detail")
	public String goodsDetail(Authentication auth, @RequestParam("goodsId") int goodsId, Model model) {
		String mbId = null;
		MemberDTO memberDTO = null;
		if (auth != null) {
			mbId = parseService.parseMbId(auth);
			memberDTO = parseService.getMemberMbId(mbId);
		}

		model.addAttribute("goods", goodsSevice.goods(goodsId));
		model.addAttribute("countWishes", wishService.countWishes(goodsId));
		model.addAttribute("member", memberDTO);
		return "goods/goods-detail";
	}

	@GetMapping("goods/write")
	public String goodsWriteView(Authentication auth, Model model) {
		if (auth == null) {
			throw new HandlableException(ErrorCode.UNAUTHORIZED);
		}
		String mbId = parseService.parseMbId(auth);
		MemberDTO memberDTO = parseService.getMemberMbId(mbId);

		model.addAttribute("member", memberDTO);
		return "goods/goods-write";
	}

	@GetMapping("goods/modify")
	public String goodsModifyView(@RequestParam("goodsId") int goodsId, Authentication auth, Model model) {
		if (auth == null) {
			throw new HandlableException(ErrorCode.UNAUTHORIZED);
		}
		String mbId = parseService.parseMbId(auth);
		MemberDTO memberDTO = parseService.getMemberMbId(mbId);
//		GoodsDTO goodsDTO = goodsDAO.goods(goodsId);
//		if (mbId.isEmpty() || !mbId.equals(goodsDTO.getSellerId())) {
//			throw new HandlableException(ErrorCode.FORBIDDEN);
//		}

		model.addAttribute("goods", goodsSevice.goods(goodsId));
		model.addAttribute("member", memberDTO);
		return "goods/goods-modify";
	}

	@ResponseBody
	@GetMapping("goods/detail-images")
	public List<GoodsImagesDTO> goodsDetailImages(@RequestParam("goodsId") int goodsId) {
		return goodsSevice.goodsImageList(goodsId);
	}

	@ResponseBody
	@PostMapping("goods/write")
	public GoodsDTO goodsWrite(Authentication auth, @RequestPart("GoodsDTO") GoodsDTO goodsDTO,
			@RequestPart(value = "files", required = false) List<MultipartFile> files) {
		if (auth == null) {
			throw new HandlableException(ErrorCode.UNAUTHORIZED);
		}
		String mbId = parseService.parseMbId(auth);
		int goodsId = goodsSevice.goodsWrite(mbId, goodsDTO, files);
		System.out.println("작성된 상품글ID : " + goodsId);
		if (goodsId != -1) {
			goodsDTO.setGoodsId(goodsId);
		}
		// 브라우저단에서 location.href로 상품상세
		return goodsDTO;
	}

	@ResponseBody
	@PostMapping("goods/modify")
	public GoodsDTO goodsModify(Authentication auth,@RequestParam("goodsId") int goodsId, @RequestPart("GoodsDTO") GoodsDTO goodsDTO,
			@RequestPart(value = "files", required = false) List<MultipartFile> files) {
		if (auth == null) {
			throw new HandlableException(ErrorCode.UNAUTHORIZED);
		}
		String mbId = parseService.parseMbId(auth);
		if (!mbId.equals(goodsDTO.getSellerId())) {
			System.out.println("불일치");
			throw new HandlableException(ErrorCode.FORBIDDEN);
		}
		goodsSevice.goodsModify(mbId, goodsDTO, files);
		
		// 브라우저단에서 location.href로 상품상세
		return goodsDTO;
	}

	@GetMapping("goods/delete")
	public String goodsDelete(Authentication auth, @RequestParam("goodsId") int goodsId) {
		String mbId = parseService.parseMbId(auth);
		goodsSevice.goodsDelete(goodsId, mbId);

		return "main";
	}
}
