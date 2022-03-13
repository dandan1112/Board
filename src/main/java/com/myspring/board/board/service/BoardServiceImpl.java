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
	

	// ��ȸ�� ����
	@Override
	public void updateViewNO(int articleNO) throws Exception {
		boardDAO.updateViewNum(articleNO);
	}

	// ��ü �� ��ȸ
	@Override
	public Map listArticles(Map pagingMap) throws Exception{
		Map articlesMap = new HashMap();
		List<ArticleVO> articlesList =  boardDAO.selectAllArticlesList(pagingMap); // ��ü ���̺� ��� ��ȸ
		int totalArticles = boardDAO.selectTotalArticles(); // ��ü �� �� ��ȸ
		articlesMap.put("articlesList", articlesList);
		articlesMap.put("totalArticles", totalArticles);
        return articlesMap;
	}

	// ���� �߰��ϱ�
	@Override
	public int addNewArticle(Map articleMap) throws Exception{
		int articleNO = boardDAO.insertNewArticle(articleMap); // ������ �߰��ϰ� ���� ��ȣ �޾ƿ�
		articleMap.put("articleNO", articleNO);
		boardDAO.insertNewImage(articleMap); // �̹��� ���� ����
		return articleNO;
	}
	
	// �� ���̱�
	@Override
	public Map viewArticle(int articleNO) throws Exception {
		Map articleMap = new HashMap();
		ArticleVO articleVO = boardDAO.selectArticle(articleNO); // �� ��ȣ ��ġ�ϴ� ��� ������  ��������
		List<ImageVO> imageFileList = boardDAO.selectImageFileList(articleNO); // �۹�ȣ ��ġ�ϴ� ��� �̹��� ���� ������ ��������
		articleMap.put("article", articleVO);
		articleMap.put("imageFileList", imageFileList);
		return articleMap;
	}
   
	@Override
	public void modArticle(Map articleMap) throws Exception {
		boardDAO.updateArticle(articleMap); // �Խñ� ���� ������Ʈ
	}
	
	@Override
	public void removeArticle(int articleNO) throws Exception {
		boardDAO.deleteArticle(articleNO); // �Խñ� ����
	}
	

	
}
