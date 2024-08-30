package com.zhf.service.impl;

import com.zhf.dao.SysJobMapper;
import com.zhf.entity.SysJob;
import com.zhf.service.ISysJobService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
public class SysJobServiceImpl implements ISysJobService {
	@Resource
	private SysJobMapper sysJobMapper;

	@Override
	public int getJobCount() {
		return sysJobMapper.getJobCount();
	}

	@Override
	public List<SysJob> querySysJobList(HashMap<String, String> map) {
		return sysJobMapper.querySysJobList(map);
	}

	@Override
	public int insertSelective(SysJob record) {
		return sysJobMapper.insertSelective(record);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return sysJobMapper.deleteByPrimaryKey(id);
	}

	@Override
	public SysJob selectByPrimaryKey(Integer id) {
		return sysJobMapper.selectByPrimaryKey(id);
	}

	@Override
	public SysJob selectByBean(SysJob bean) {
		return sysJobMapper.selectByBean(bean);
	}

	@Override
	public int updateByPrimaryKeySelective(SysJob bean) {
		return sysJobMapper.updateByPrimaryKeySelective(bean);
	}

}
