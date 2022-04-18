package multi.fclass.iMint.goods.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import multi.fclass.iMint.common.service.IFileService;
import multi.fclass.iMint.common.service.IUtilService;
import multi.fclass.iMint.goods.dao.IGoodsDAO;
import multi.fclass.iMint.goods.dto.GoodsDTO;
import multi.fclass.iMint.goods.dto.GoodsImagesDTO;

/**
 * @author Seongil, Yoon
 *
 */
@Service
public class GoodsServiceImpl implements IGoodsService {
	@Autowired
	IGoodsDAO goodsDAO;

	@Autowired
	IUtilService utilService;

	@Autowired
	IFileService fileService;

	@Autowired
	HttpSession httpSession;

	@Override
	public GoodsDTO goods(int goodsId) {
		return goodsDAO.goods(goodsId);
	}

	@Override
	public List<GoodsImagesDTO> goodsImageList(int goodsId) {
		List<GoodsImagesDTO> goodsImages = goodsDAO.goodsImageList(goodsId);
		if (goodsImages.isEmpty()) {
			goodsImages.add(new GoodsImagesDTO(null, goodsId, "/static/images/noimage.png", true, "noimage.png", null));
		}
		return goodsImages;
	}

	@Override
	public int goodsWrite(GoodsDTO goodsDto, List<MultipartFile> files) {
//		String sellerId = (String) httpSession.getAttribute("mbId");
//		String sellerNick = (String) httpSession.getAttribute("mbNick");

		if (!goodsDto.getSellerId().isEmpty()) {
//			throw new UnauthorizedException(String.format("unauthorized you"));
		}
//		int goodsId = goodsDAO.goodsInsert(goodsDto); // return 1
		goodsDAO.goodsInsert(goodsDto);
		int goodsId = goodsDto.getGoodsId();

		if (!files.isEmpty()) {
			// 상품글의 파일들을 올릴 경로("C:/iMint/goods/yyyy/MM/dd")를 배열로 반환
			List<String> paths = utilService.createGoodsPaths(goodsDAO.goodsDate(goodsId));
			fileService.mkDir(paths); // 폴더 생성
			// 경로에 파일업로드 and DB insert
			int goodsImagesId = fileService.uploadGoodsImageFiles(paths, goodsDto.getGoodsId(), files);
			if (goodsImagesId == -1) {
				return goodsImagesId;
//			throw new 
			}
		}

		return goodsId;
	}

	@Override
	public int goodsDelete(int goodsId, int sellerId) {
		goodsDAO.goodsDelete(goodsId, sellerId);
		// 파일삭제 로직 추가예정
		return 0;
	}
	
	

}
