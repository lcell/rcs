package com.liguang.rcs.db.service;

import com.liguang.rcs.db.dao.RcsUserMapper;
import com.liguang.rcs.db.domain.RcsUser;
import com.liguang.rcs.db.domain.RcsUserExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RcsUserService {

    @Resource
    private RcsUserMapper mapper;
    public List<RcsUser> findUser(String username) {
        RcsUserExample example = new RcsUserExample();
        example.or().andUsernameEqualTo(username).andDeletedEqualTo(false);
        return mapper.selectByExample(example);
    }
}
