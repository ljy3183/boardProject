package com.spring.boarddao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.boardvo.BoardVO;

@Repository
public class BoardDaoImpl implements BoardDao {

	@Autowired
	private SqlSession session;

	@Override
	public List<BoardVO> boardList(BoardVO pvo) {
		return session.selectList("boardList", pvo);
	}

	@Override
	public int boardListCnt(BoardVO pvo) {
		return (Integer) session.selectOne("boardListCnt", pvo);
	}

	@Override
	public int boardInsert(BoardVO pvo) {
		return session.insert("boardInsert", pvo);
	}

	@Override
	public BoardVO boardDetail(BoardVO pvo) {
		return (BoardVO) session.selectOne("boardDetail", pvo);
	}

	@Override
	public int pwdConfirm(BoardVO pvo) {
		return (Integer) session.selectOne("pwdConfirm", pvo);
	}

	@Override
	public int boardUpdate(BoardVO pvo) {
		return session.update("boardUpdate", pvo);
	}

	@Override
	public int boardDelete(int b_num) {
		return session.delete("boardDelete", b_num);
	}

}
