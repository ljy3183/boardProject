package com.spring.replyservice;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.replydao.ReplyDao;
import com.spring.replyvo.ReplyVO;

@Service
@Transactional
public class ReplyServiceImpl implements ReplyService {
	Logger logger = Logger.getLogger(ReplyServiceImpl.class);

	@Autowired
	private ReplyDao replyDao;

	// 글목록 구현
	@Override
	public List<ReplyVO> replyList(Integer b_num) {
		List<ReplyVO> myList = null;
		myList = replyDao.replyList(b_num);
		return myList;
	}

	// 글입력 구현
	@Override
	public int replyInsert(ReplyVO rvo) {
		int result = 0;
		result = replyDao.replyInsert(rvo);
		return result;
	}

	// 글수정 구현
	@Override
	public int replyUpdate(ReplyVO rvo) {
		int result = 0;
		result = replyDao.replyUpdate(rvo);
		return result;
	}

	// 글삭제 구현
	@Override
	public int replyDelete(int r_num) {
		int result = 0;
		result = replyDao.replyDelete(r_num);
		return result;
	}

}
