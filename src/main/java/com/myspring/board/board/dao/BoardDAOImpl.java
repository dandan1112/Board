package com.myspring.board.board.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.board.board.vo.ArticleVO;
import com.myspring.board.board.vo.ImageVO;

@Repository("boardDAO")
public class BoardDAOImpl implements BoardDAO {
	@Autowired
	private SqlSession sqlSession;

	@Override
	public void updateViewNum(int articleNO) throws DataAccessException {
		sqlSession.update("mapper.board.updateView", articleNO);
	}

	@Override
	public int selectTotalArticles() throws DataAccessException {
		int totalArticles = sqlSession.selectOne("mapper.board.selectTotalArticles");
		return totalArticles;
	}

	@Override
	public List selectAllArticlesList(Map pagingMap) throws DataAccessException {
		List<ArticleVO> articlesList = articlesList = sqlSession.selectList("mapper.board.selectAllArticlesList",pagingMap);
		return articlesList;
	}

	@Override
	public int insertNewArticle(Map articleMap) throws DataAccessException {
		int articleNO = selectNewArticleNO();
		articleMap.put("articleNO", articleNO);
		sqlSession.insert("mapper.board.insertNewArticle", articleMap);
		return articleNO;
	}

	@Override
	public void insertNewImage(Map articleMap) throws DataAccessException {
		List<ImageVO> imageFileList = (ArrayList) articleMap.get("imageFileList");
		int articleNO = (Integer) articleMap.get("articleNO");
		int imageFileNO = selectNewImageFileNO();

		if (imageFileList != null && imageFileList.size() != 0) {
			for (ImageVO imageVO : imageFileList) {
				imageVO.setImageFileNO(++imageFileNO); // imageVO에 이미지 번호와 글 번호 설정해 리스트에 추가
				imageVO.setArticleNO(articleNO);
			}
			sqlSession.insert("mapper.board.insertNewImage", imageFileList);
		}
	}

	@Override
	public ArticleVO selectArticle(int articleNO) throws DataAccessException {
		return sqlSession.selectOne("mapper.board.selectArticle", articleNO);
	}

	@Override
	public void updateArticle(Map articleMap) throws DataAccessException {
		sqlSession.update("mapper.board.updateArticle", articleMap);
	}

	@Override
	public void updateImageFile(Map articleMap) throws DataAccessException {
		
		List<ImageVO> imageFileList = (ArrayList)articleMap.get("imageFileList");
		int articleNO = Integer.parseInt((String)articleMap.get("articleNO"));
		
		for(int i = imageFileList.size()-1; i >= 0; i--){
			ImageVO imageVO = imageFileList.get(i);
			String imageFileName = imageVO.getImageFileName();
			if(imageFileName == null) {  //기존에 이미지를 수정하지 않는 경우 파일명이 null 이므로  수정할 필요가 없다.
				imageFileList.remove(i);
			}else {
				imageVO.setArticleNO(articleNO);
			}
		}
		
		if(imageFileList != null && imageFileList.size() != 0) {
			sqlSession.update("mapper.board.updateImageFile", imageFileList);
		}
		
	}

	@Override
	public void deleteArticle(int articleNO) throws DataAccessException {
		sqlSession.delete("mapper.board.deleteArticle", articleNO);

	}

	@Override
	public List selectImageFileList(int articleNO) throws DataAccessException {
		List<ImageVO> imageFileList = null;
		imageFileList = sqlSession.selectList("mapper.board.selectImageFileList", articleNO);
		return imageFileList;
	}

	private int selectNewArticleNO() throws DataAccessException {
		return sqlSession.selectOne("mapper.board.selectNewArticleNO");
	}

	private int selectNewImageFileNO() throws DataAccessException {
		return sqlSession.selectOne("mapper.board.selectNewImageFileNO");
	}

	@Override
	public void deleteModImage(ImageVO imageVO) throws DataAccessException {
		sqlSession.delete("mapper.board.deleteModImage", imageVO );
	}

}
