package com.spring.boardservice;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.boarddao.BoardDao;
import com.spring.boardvo.BoardVO;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {
	Logger logger = Logger.getLogger(BoardServiceImpl.class);

	@Autowired
	private BoardDao boardDao;

	// 글목록 구현
	@Override
	public List<BoardVO> boardList(BoardVO pvo) {
		List<BoardVO> myList = null;
		myList = boardDao.boardList(pvo);
		return myList;
	}

	// 전체 레코드 수 구현
	@Override
	public int boardListCnt(BoardVO pvo) {
		return boardDao.boardListCnt(pvo);
	}

	// 글입력 구현
	@Override
	public int boardInsert(BoardVO pvo) {
		int result = 0;
		result = boardDao.boardInsert(pvo);
		return result;
	}

	// 글상세보기 구현
	@Override
	public BoardVO boardDetail(BoardVO pvo) {
		BoardVO detail = null;
		detail = boardDao.boardDetail(pvo);
		return detail;
	}

	// 글수정 구현
	@Override
	public int boardUpdate(BoardVO pvo) {
		int result = 0;
		result = boardDao.boardUpdate(pvo);
		return result;
	}

	// 비밀번호 확인 구현
	@Override
	public int pwdConfirm(BoardVO pvo) {
		int result = 0;
		result = boardDao.pwdConfirm(pvo);
		return result;
	}

	// 글삭제 구현
	@Override
	public int boardDelete(int b_num) {
		int result = 0;
		result = boardDao.boardDelete(b_num);
		return result;
	}

}
