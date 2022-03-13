package com.myspring.board.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.board.board.dao.BoardDAO;
import com.myspring.board.board.vo.ArticleVO;
import com.myspring.board.board.vo.ImageVO;


@Service("boardService")
@Transactional(propagation = Propagation.REQUIRED)
public class BoardServiceImpl implements BoardService{
	@Autowired
	BoardDAO boardDAO;
	

	// 조회수 증가
	@Override
	public void updateViewNO(int articleNO) throws Exception {
		boardDAO.updateViewNum(articleNO);
	}

	// 전체 글 조회
	@Override
	public Map listArticles(Map pagingMap) throws Exception{
		Map articlesMap = new HashMap();
		List<ArticleVO> articlesList =  boardDAO.selectAllArticlesList(pagingMap); // 전체 테이블 목록 조회
		int totalArticles = boardDAO.selectTotalArticles(); // 전체 글 수 조회
		articlesMap.put("articlesList", articlesList);
		articlesMap.put("totalArticles", totalArticles);
        return articlesMap;
	}

	// 새글 추가하기
	@Override
	public int addNewArticle(Map articleMap) throws Exception{
		int articleNO = boardDAO.insertNewArticle(articleMap); // 데이터 추가하고 새글 번호 받아옴
		articleMap.put("articleNO", articleNO);
		boardDAO.insertNewImage(articleMap); // 이미지 정보 저장
		return articleNO;
	}
	
	// 글 보이기
	@Override
	public Map viewArticle(int articleNO) throws Exception {
		Map articleMap = new HashMap();
		ArticleVO articleVO = boardDAO.selectArticle(articleNO); // 글 번호 일치하는 모든 데이터  가져오기
		List<ImageVO> imageFileList = boardDAO.selectImageFileList(articleNO); // 글번호 일치하는 모든 이미지 파일 데이터 가져오기
		articleMap.put("article", articleVO);
		articleMap.put("imageFileList", imageFileList);
		return articleMap;
	}
   
	@Override
	public void modArticle(Map articleMap) throws Exception {
		boardDAO.updateArticle(articleMap); // 게시글 정보 업데이트
	}
	
	@Override
	public void removeArticle(int articleNO) throws Exception {
		boardDAO.deleteArticle(articleNO); // 게시글 삭제
	}
	

	
}
