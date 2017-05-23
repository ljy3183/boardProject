package com.spring.boardcontroller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.boardcommon.Paging;
import com.spring.boardcommon.Util;
import com.spring.boardexcel.ListExcelView;
import com.spring.boardfileupload.FileUploadUtil;
import com.spring.boardservice.BoardService;
import com.spring.boardvo.BoardVO;

@Controller
@RequestMapping(value = "/board")
public class BoardController {
	Logger logger = Logger.getLogger(BoardController.class);

	@Autowired
	private BoardService boardService;

	/* 글목록 구현하기 */
	@RequestMapping(value = "/boardList", method = RequestMethod.GET)
	public String boardList(@ModelAttribute BoardVO bvo, Model model) {
		logger.info("boardList 호출 성공");

		// 정렬에 대한 기본값 설정
		if (bvo.getOrder_by() == null)
			bvo.setOrder_by("b_num");
		if (bvo.getOrder_sc() == null)
			bvo.setOrder_sc("DESC");

		// 정렬에 대한 데이터 확인
		logger.info("order_by= " + bvo.getOrder_by());
		logger.info("order_sc= " + bvo.getOrder_sc());

		// 검색에 대한 데이터 확인
		logger.info("search= " + bvo.getSearch());
		logger.info("keyword = " + bvo.getKeyword());

		// 페이지 세팅
		Paging.setPage(bvo);

		// 전체 레코드 수 구하기
		int total = boardService.boardListCnt(bvo);
		logger.info("total = " + total);

		// 글번호 재설정
		int count = total - (Util.nvl(bvo.getPage()) - 1) * Util.nvl(bvo.getPageSize());
		logger.info("count = " + count);

		List<BoardVO> boardList = boardService.boardList(bvo);

		model.addAttribute("boardList", boardList);
		model.addAttribute("count", count);
		model.addAttribute("data", bvo);
		model.addAttribute("total", total);

		return "board/boardList";
	}

	/* 글쓰기 폼 출력하기 */
	@RequestMapping(value = "/writeForm")
	public String writeform(HttpSession session) {
		logger.info("writeForm 호출 성공");

		// 요청이 들어오면
		session.setAttribute("CSRF_TOKEN", UUID.randomUUID().toString());
		logger.info("CSRF_TOKEN : " + UUID.randomUUID().toString());

		return "board/writeForm";
	}

	/* 글쓰기 구현하기 */
	@RequestMapping(value = "/boardInsert", method = RequestMethod.POST)
	public String boardInsert(@ModelAttribute BoardVO bvo) {
		logger.info("boardInsert 호출 성공");

		int result = 0;
		String url = "";

		result = boardService.boardInsert(bvo);
		if (result == 1) {
			url = "/board/boardList.do";
		}

		return "redirect:" + url;
	}

	/* 글 상세보기 구현 */
	@RequestMapping(value = "/boardDetail", method = RequestMethod.GET)
	public String boardDetail(@ModelAttribute BoardVO pvo, Model model) {

		logger.info("boardDetail 호출 성공");

		logger.info("b_num= " + pvo.getB_num());

		BoardVO detail = new BoardVO();
		detail = boardService.boardDetail(pvo);

		if (detail != null) {
			detail.setB_content(detail.getB_content().toString().replaceAll("\n", "<br>"));
		}

		model.addAttribute("detail", detail);

		return "board/boardDetail";

	}

	/* 비밀번호확인 */
	@ResponseBody
	@RequestMapping(value = "/pwdConfirm", method = RequestMethod.POST)
	public String pwdConfirm(@ModelAttribute BoardVO bvo) {
		logger.info("pwdConfirm 호출 성공");

		// 아래 변수에는 입력 성공에 대한 상태값 저장(1or0)
		int result = 0;
		result = boardService.pwdConfirm(bvo);
		logger.info("result= " + result);

		return result + "";
	}

	/* 글 수정 폼 출력하기 */
	@RequestMapping(value = "/updateForm", method = RequestMethod.POST)
	public String updateForm(@ModelAttribute BoardVO pvo, Model model) {
		logger.info("updateForm 호출 성공");

		logger.info("b_num = " + pvo.getB_num());

		BoardVO updateData = new BoardVO();
		updateData = boardService.boardDetail(pvo);

		model.addAttribute("updateData", updateData);
		return "board/updateForm";
	}

	/* 글 수정 구현하기 */
	@RequestMapping(value = "/boardUpdate", method = RequestMethod.POST)
	public String boardUpdate(@ModelAttribute BoardVO bvo, HttpServletRequest request)
			throws IllegalStateException, IOException {
		logger.info("boardUpdate 호출 성공");

		int result = 0;
		String url = "";
		String b_file = "";

		if (!bvo.getFile().isEmpty()) {
			logger.info("=================b_file = " + bvo.getB_file());
			FileUploadUtil.fileDelete(bvo.getB_file(), request);
			b_file = FileUploadUtil.fileUpload(bvo.getFile(), request);
			bvo.setB_file(b_file);
		} else {
			logger.info("첨부파일 없음");
			bvo.setB_file("");
		}
		logger.info("============b_file = " + bvo.getB_file());
		result = boardService.boardUpdate(bvo);

		if (result == 1) {
			// url = "/board/boardList.do"; // 수정 후 목록으로 이동
			// 아래 url은 수정 후 상세 페이지로 이동
			url = "/board/boardDetail.do?b_num=" + bvo.getB_num();
			/*
			 * ra.addFlashAttribute("b_num",bvo.getB_num());
			 * url="/board/boardDetail.do";
			 */
		}
		return "redirect:" + url;
	}

	/* 글 삭제 구현하기 */
	@RequestMapping(value = "/boardDelete")
	public String boardDelete(@ModelAttribute BoardVO bvo, HttpServletRequest request) throws IOException {
		logger.info("boardDelete 호출 성공");

		// 아래 변수에는 입력 성공에 대한 상태값 담습니다.(1or0)
		int result = 0;
		String url = "";

		FileUploadUtil.fileDelete(bvo.getB_file(), request);
		result = boardService.boardDelete(bvo.getB_num());

		if (result == 1) {
			url = "/board/boardList.do";

		}
		return "redirect:" + url;
	}

	/*
	 * 액셀 다운로드 구현하기 참고: ListExcelView 클래스에서 맵타입으로 Model에 접근하게 된다.
	 */
	@RequestMapping(value = "/boardExcel", method = RequestMethod.GET)
	public ModelAndView boardExcel(@ModelAttribute BoardVO bvo) {
		logger.info("boardExcel 호출 성공");
		// 정렬에 대한 기본값 설정
		if (bvo.getOrder_by() == null)
			bvo.setOrder_by("b_num");
		if (bvo.getOrder_sc() == null)
			bvo.setOrder_sc("DESC");

		// 페이지 세팅
		Paging.setPage(bvo);

		List<BoardVO> boardList = boardService.boardList(bvo);

		ModelAndView mv = new ModelAndView(new ListExcelView());
		mv.addObject("list", boardList);
		mv.addObject("template", "board.xlsx");
		mv.addObject("file_name", "board");

		return mv;

	}

	@ResponseBody
	@RequestMapping(value = "/board", method = RequestMethod.GET)
	public String board(@ModelAttribute BoardVO bvo, Model model, ObjectMapper om) {
		logger.info("boardList 호출 성공");
		String listData = "";
		List<BoardVO> boardList = boardService.boardList(bvo);

		try {
			listData = om.writeValueAsString(boardList);
			// System.out.println(listData);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return listData;
	}
}
