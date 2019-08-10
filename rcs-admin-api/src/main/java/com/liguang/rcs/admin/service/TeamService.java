package com.liguang.rcs.admin.service;

import com.google.common.base.Preconditions;
import com.liguang.rcs.admin.common.enumeration.TeamTypeEnum;
import com.liguang.rcs.admin.common.response.PageableBody;
import com.liguang.rcs.admin.db.domain.TeamEntity;
import com.liguang.rcs.admin.db.repository.TeamRepository;
import com.liguang.rcs.admin.exception.BaseException;
import com.liguang.rcs.admin.util.ResponseCode;
import com.liguang.rcs.admin.web.team.TeamVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private AccountService accountService;


    public TeamEntity queryTeamWithId(Long teamId) {
        if (teamId != null) {
            Optional<TeamEntity> teamEntity = teamRepository.findById(teamId);
            if (teamEntity.isPresent()) {
                return teamEntity.get();
            }
        }
        return null;
    }

    public void save(@Valid TeamVO teamVo) {
        TeamEntity entity = teamVo.toEntity();
        Preconditions.checkNotNull(entity, "Team entity can't be null");
        teamRepository.save(entity);
    }


    public PageableBody<TeamVO> queryTeamByParentId(Long parentId, int currentPage, int pageSize) {
        TeamEntity vo = new TeamEntity();
        vo.setParentRefTeamId(parentId);
        Example<TeamEntity> example = Example.of(vo);
        Page<TeamEntity> pageEntity = teamRepository.findAll(example, PageRequest.of(currentPage, pageSize, Sort.by("id")));
        return PageableBody.buildFrom(pageEntity, TeamVO::buildFrom);
    }

    public PageableBody<TeamVO> queryTeamByDepartmentId(Long departmentId, int currentPage, int pageSize) {
        return queryTeamByParentId(departmentId, currentPage, pageSize);
    }

    @Transactional
    public void deleteTeam(Long teamId, TeamTypeEnum teamType) throws BaseException {
        TeamEntity entity = new TeamEntity();
        entity.setParentRefTeamId(teamId);
        if (teamRepository.count(Example.of(entity)) > 0) {
            log.warn("[Team] team has subTeam, please delete them first, teamId:{}", teamId);
            throw new BaseException(ResponseCode.TEAM_DELETE_FAIL);
        }
        if (teamType == TeamTypeEnum.TEAM) {
            accountService.deleteTeamInfo(teamId);
        } else if (teamType == TeamTypeEnum.DEPARTMENT) {
            accountService.deleteDepartmentInfo(teamId);
        }
        teamRepository.deleteById(teamId);
    }

    public List<TeamVO> querList() {
        return teamRepository.findAll().stream().map(TeamVO::buildFrom).collect(Collectors.toList());
    }
}
