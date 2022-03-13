package com.myspring.board.board.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.board.board.service.BoardService;
import com.myspring.board.board.vo.ArticleVO;
import com.myspring.board.board.vo.ImageVO;
import com.myspring.board.member.vo.MemberVO;

@Controller("boardController")
public class BoardControllerImpl implements BoardController {
	private static final String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";
	@Autowired
	private BoardService boardService;
	@Autowired
	private ArticleVO articleVO;

	// 전체 글 리스트 출력
	@Override
	@RequestMapping(value = "/board/listArticles.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// pagingMap에 현재 페이지와 섹션정보를 넣어 전달한 뒤 반환된 Map을 mav로 전달 (articlesList,
		// totalArticles, section, pageNum)
		String _section = request.getParameter("section");
		String _pageNum = request.getParameter("pageNum");
		int section = Integer.parseInt(((_section == null) ? "1" : _section));
		int pageNum = Integer.parseInt(((_pageNum == null) ? "1" : _pageNum));
		Map pagingMap = new HashMap();
		pagingMap.put("section", section);
		pagingMap.put("pageNum", pageNum);

		Map articlesMap = boardService.listArticles(pagingMap);
		articlesMap.put("section", section);
		articlesMap.put("pageNum", pageNum);

		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("articlesMap", articlesMap);

		// 폼으로 오늘 날짜 전달
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1); // 하루 동안 유지
		String nowday = format.format(cal.getTime());
		mav.addObject("nowday", nowday);

		return mav;
	}

	// 글 작성
	@Override
	@RequestMapping(value = "/board/addNewArticle.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity addNewArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response) throws Exception {
		multipartRequest.setCharacterEncoding("utf-8");
		String imageFileName = null;
		Map articleMap = new HashMap();

		// 폼에서 전송된 파라미터의 타입이 file이 아닌 파라미터들(title, content, notice)을 받아와 name과 value를 map으로 저장해 addNewArticle()로 전달
		Enumeration enu = multipartRequest.getParameterNames();

		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = multipartRequest.getParameter(name);
			articleMap.put(name, value);
		}

		// 로그인 시 세션에 저장된 회원 ID를 가져와 저장
		HttpSession session = multipartRequest.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("member");
		String id = memberVO.getId();
		articleMap.put("id", id);

		String parentNO = (String)session.getAttribute("parentNO")  ;
		articleMap.put("parentNO" , (parentNO == null ? 0 : parentNO));
		
		// upload 메소드를 통해 이미지 파일 이름이 저장된 fileList를 받아온 뒤 파일 이름을 imageVO 속성에 저장하고 다시 List에 저장 후 이를 Map에 저장해 서비스 클래스로 전달
		List<String> fileList = upload(multipartRequest);
		List<ImageVO> imageFileList = new ArrayList<ImageVO>();
		
		if (fileList != null && fileList.size() != 0) {
			for (String fileName : fileList) {
				ImageVO imageVO = new ImageVO();
				imageVO.setImageFileName(fileName);
				imageFileList.add(imageVO);
			}
			articleMap.put("imageFileList", imageFileList);
		}

		String message;
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		// 새글 추가 성공
		try {
			int articleNO = boardService.addNewArticle(articleMap);
			if (imageFileList != null && imageFileList.size() != 0) {
				for (ImageVO imageVO : imageFileList) {
					imageFileName = imageVO.getImageFileName();
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
					FileUtils.moveFileToDirectory(srcFile, destDir, true); // 파일 존재 시 article가 이름인 폴더로 이동
				}
			}
			message = "<script>";
			message += " alert('새글을 추가했습니다.');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/listArticles.do'; ";
			message += " </script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		} catch (Exception e) {
		// 새글 추가 실패
			if (imageFileList != null && imageFileList.size() != 0) {
				for (ImageVO imageVO : imageFileList) {
					imageFileName = imageVO.getImageFileName();
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
					srcFile.delete(); // 실패 시 삭제
				}
			}

			message = " <script>";
			message += " alert('오류가 발생했습니다. 다시 시도해 주세요');');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/articleForm.do'; ";
			message += " </script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
		}
		return resEnt;
	}

	// 게시글 보기
	@RequestMapping(value = "/board/viewArticle.do", method = RequestMethod.GET)
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		
		Map articleMap = boardService.viewArticle(articleNO);
		
		boardService.updateViewNO(articleNO); // 조회수 증가
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		mav.addObject("articleMap", articleMap);
		
		return mav;
	}

	// 게시글 수정
	@RequestMapping(value = "/board/modArticle.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response) throws Exception {
		multipartRequest.setCharacterEncoding("utf-8");
		Map<String, Object> articleMap = new HashMap<String, Object>();
		Enumeration enu = multipartRequest.getParameterNames();
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();

			// 파라미터로 넘어온 데이터를 저장
			if (name.equals("imageFileNO")) {
				String[] values = multipartRequest.getParameterValues(name);
				articleMap.put(name, values);
			} else if (name.equals("oldFileName")) {
				String[] values = multipartRequest.getParameterValues(name);
				articleMap.put(name, values);
			} else {
				String value = multipartRequest.getParameter(name);
				articleMap.put(name, value);
			}
		}

		// 수정한 이미지 파일을 업로드하고 파일명이 담긴 리스트 가져와 저장
		List<String> fileList = uploadModImageFile(multipartRequest);

		int added_img_num = Integer.parseInt((String) articleMap.get("added_img_num"));
		int pre_img_num = Integer.parseInt((String) articleMap.get("pre_img_num"));
		
		List<ImageVO> imageFileList = new ArrayList<ImageVO>();
		List<ImageVO> modAddimageFileList = new ArrayList<ImageVO>();

		if (fileList != null && fileList.size() != 0) {
			String[] imageFileNO = (String[]) articleMap.get("imageFileNO");
			for (int i = 0; i < added_img_num; i++) {
				String fileName = fileList.get(i);
				ImageVO imageVO = new ImageVO();
				if (i < pre_img_num) { // 기존 이미지 수만큼 반복 
					// 파일 이름, 번호 list에 저장해 map에 다시 저장
					imageVO.setImageFileName(fileName);
					imageVO.setImageFileNO(Integer.parseInt(imageFileNO[i]));
					imageFileList.add(imageVO);
					articleMap.put("imageFileList", imageFileList);
				} else {
					// 파일 이름 저장 list에 저장해 map에 다시 저장
					imageVO.setImageFileName(fileName);
					modAddimageFileList.add(imageVO);
					articleMap.put("modAddimageFileList", modAddimageFileList);
				}
			}
		}

		String articleNO = (String) articleMap.get("articleNO");
		String message;
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		try {
			boardService.modArticle(articleMap); // 글 수정
			if (fileList != null && fileList.size() != 0) { // 수정한 파일들을 차례대로 업로드
				for (int i = 0; i < fileList.size(); i++) {
					String fileName = fileList.get(i);
					if (i  < pre_img_num) {
						if (fileName != null) {
							System.out.println(fileName);
							File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + fileName);
							File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
							FileUtils.moveFileToDirectory(srcFile, destDir, true);

							String[] oldName = (String[]) articleMap.get("oldFileName");
							String oldFileName = oldName[i];

							File oldFile = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO + "\\" + oldFileName);
							oldFile.delete();
						}
					}else {
						System.out.println(fileName);
						if (fileName != null) {
							File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + fileName);
							File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
							FileUtils.moveFileToDirectory(srcFile, destDir, true);
						}
					}
				}
			}

			message = "<script>";
			message += " alert('글을 수정했습니다.');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/viewArticle.do?articleNO="
					+ articleNO + "';";
			message += " </script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		} catch (Exception e) {

			if (fileList != null && fileList.size() != 0) { // 오류 발생 시 temp 폴더에 업로드된 이미지 파일들 삭제
				for (int i = 0; i < fileList.size(); i++) {
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + fileList.get(i));
					srcFile.delete();
				}

				e.printStackTrace();
			}

			message = "<script>";
			message += " alert('오류 발생! 다시 수정해주세요.');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/viewArticle.do?articleNO="
					+ articleNO + "';";
			message += " </script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		}
		return resEnt;
	}

	// 게시글 삭제
	@Override
	@RequestMapping(value = "/board/removeArticle.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity removeArticle(@RequestParam("articleNO") int articleNO, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		String message;
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		try {
			boardService.removeArticle(articleNO);
			File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO); //게시글번호로 파일 생성
			FileUtils.deleteDirectory(destDir);

			message = "<script>";
			message += " alert('글을 삭제했습니다.');";
			message += " location.href='" + request.getContextPath() + "/board/listArticles.do';";
			message += " </script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);

		} catch (Exception e) {
			message = "<script>";
			message += " alert('작업중 오류가 발생했습니다.다시 시도해 주세요.');";
			message += " location.href='" + request.getContextPath() + "/board/listArticles.do';";
			message += " </script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
		}
		return resEnt;
	}

	// 폼 화면 요청 시 뷰이름을 가져와 mav로 넘겨줌
	@RequestMapping(value = "/board/*Form.do", method = RequestMethod.GET)
	private ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}


	// 새 글 쓰기 시 이미지 업로드
		private List<String> upload(MultipartHttpServletRequest multipartRequest) throws Exception {
			List<String> fileList = new ArrayList<String>();
			Iterator<String> fileNames = multipartRequest.getFileNames();
			while (fileNames.hasNext()) {
				String fileName = fileNames.next();
				MultipartFile mFile = multipartRequest.getFile(fileName);
				String originalFileName = mFile.getOriginalFilename();
				if (originalFileName != "" && originalFileName != null) {
					fileList.add(originalFileName);
					File file = new File(ARTICLE_IMAGE_REPO + "\\" + fileName);
					if (mFile.getSize() != 0) { // File Null Check
						if (!file.exists()) { // 경로상에 파일이 존재하지 않을 경우
							file.getParentFile().mkdirs(); // 경로에 해당하는 디렉토리들을 생성
							mFile.transferTo(new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + originalFileName)); // 임시로
						}
					}
				}

			}
			return fileList;
		}
	
	// 글 수정 시 이미지 업로드
	private List<String> uploadModImageFile(MultipartHttpServletRequest multipartRequest) throws Exception {
		List<String> fileList = new ArrayList<String>();
		Iterator<String> fileNames = multipartRequest.getFileNames();
		while (fileNames.hasNext()) {
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			String originalFileName = mFile.getOriginalFilename(); // 파일명 중 확장자만 추출
			if (originalFileName != "" && originalFileName != null) {
				fileList.add(originalFileName);
				File file = new File(ARTICLE_IMAGE_REPO + "\\" + fileName); // 해당 경로에 파일명으로 폴더 생성
				if (mFile.getSize() != 0) { // File Null Check
					if (!file.exists()) { // 경로상에 파일이 존재하지 않을 경우
						file.getParentFile().mkdirs(); // 디렉토리의 상위 경로가 존재하지 않을 경우 상위 디렉토리까지 생성
						mFile.transferTo(new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + originalFileName)); // temp 폴더에 임시로 업로드된 파일 객체 생성
					}
				}
			} else {
				fileList.add(null);
			}

		}
		return fileList;
	}
}
