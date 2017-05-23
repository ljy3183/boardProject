package com.spring.replyservice;

import java.util.List;

import com.spring.replyvo.ReplyVO;

public interface ReplyService {

	public List<ReplyVO> replyList(Integer b_num);

	public int replyInsert(ReplyVO rvo);

	/* public int pwdConfirm(ReplyVO rvo); */
	public int replyUpdate(ReplyVO rvo);

	public int replyDelete(int r_num);
}
