package com.myspring.board.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.myspring.board.board.vo.ArticleVO;

public interface BoardService {
	public Map listArticles(Map pagingMap) throws Exception;
	public int addNewArticle(Map articleMap) throws Exception;
	public Map viewArticle(int articleNO) throws Exception;
	public void modArticle(Map articleMap) throws Exception;
	public void removeArticle(int articleNO) throws Exception;
	public void updateViewNO(int articleNO) throws Exception;
}
