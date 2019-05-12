package com.liguang.rcs.db.service;

import com.github.pagehelper.PageHelper;
import com.liguang.rcs.db.dao.RcsUserMapper;
import com.liguang.rcs.db.domain.RcsUser;
import com.liguang.rcs.db.domain.RcsUserExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.liguang.rcs.db.domain.RcsUser.Column;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RcsUserService {
    private final Column[] result = new Column[]{Column.id, Column.username, Column.avatar, Column.roleIds};

    @Resource
    private RcsUserMapper mapper;

    public List<RcsUser> findUser(String username) {
        RcsUserExample example = new RcsUserExample();
        example.or().andUsernameEqualTo(username).andDeletedEqualTo(false);
        return mapper.selectByExample(example);
    }

    public List<RcsUser> querySelective(String username, Integer page, Integer limit, String sort, String order) {
        RcsUserExample example = new RcsUserExample();
        RcsUserExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(username)) {
            criteria.andUsernameLike("%" + username + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return mapper.selectByExampleSelective(example, result);
    }

    public void add(RcsUser user) {
        user.setAddTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        mapper.insertSelective(user);
    }

    public RcsUser findById(Integer id) {
        return mapper.selectByPrimaryKeySelective(id, result);
    }

    public int updateById(RcsUser user) {
        user.setUpdateTime(LocalDateTime.now());
        return mapper.updateByPrimaryKeySelective(user);
    }

    public void deleteById(Integer id) {
        mapper.logicalDeleteByPrimaryKey(id);
    }

    public List<RcsUser> all() {
        RcsUserExample example = new RcsUserExample();
        example.or().andDeletedEqualTo(false);
        return mapper.selectByExample(example);
    }
}
