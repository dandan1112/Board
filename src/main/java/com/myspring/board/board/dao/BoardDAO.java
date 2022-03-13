package com.myspring.board.board.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.myspring.board.board.vo.ArticleVO;
import com.myspring.board.board.vo.ImageVO;


public interface BoardDAO {
	public List selectAllArticlesList(Map pagingMap) throws DataAccessException;
	public int insertNewArticle(Map articleMap) throws DataAccessException;
	public void insertNewImage(Map articleMap) throws DataAccessException;
	
	public ArticleVO selectArticle(int articleNO) throws DataAccessException;
	public void updateArticle(Map articleMap) throws DataAccessException;
	public void deleteArticle(int articleNO) throws DataAccessException;
	public List selectImageFileList(int articleNO) throws DataAccessException;
	
	public int selectTotalArticles() throws DataAccessException;
	public void updateViewNum(int articleNO) throws DataAccessException;
	public void deleteModImage(ImageVO imageVO) throws DataAccessException;
	public void updateImageFile(Map articleMap) throws DataAccessException;
}
