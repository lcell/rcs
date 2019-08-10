package com.liguang.rcs.admin.web.team;

import com.liguang.rcs.admin.common.enumeration.TeamTypeEnum;
import com.liguang.rcs.admin.common.response.PageableBody;
import com.liguang.rcs.admin.common.response.ResponseObject;
import com.liguang.rcs.admin.db.domain.TeamEntity;
import com.liguang.rcs.admin.exception.BaseException;
import com.liguang.rcs.admin.service.AccountService;
import com.liguang.rcs.admin.service.TeamService;
import com.liguang.rcs.admin.util.NumericUtils;
import com.liguang.rcs.admin.util.ResponseCode;
import com.liguang.rcs.admin.web.account.AccountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/rcs/team")
@Api(tags = "团队管理")
public class TeamController {
    @Autowired
    private TeamService teamService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/createDepartment")
    @ApiOperation("创建部门")
    public ResponseObject createDepartment(@Valid @RequestBody TeamVO teamVo) {
        if (teamVo == null || Strings.isNotBlank(teamVo.getId())) {
            log.warn("[Team] team info can't be empty and teamId must be empty.");
            return ResponseObject.badArgumentValue();
        }
        try {
            checkTeamInfo(teamVo);
            teamService.save(teamVo);
            return ResponseObject.success();
        } catch (BaseException ex) {
            return ResponseObject.fail(ex.getCode(), ex.getMessage());

        } catch (Exception ex) {
            log.error("[Team] createDepartment fail, Exception:{}", ex);
            return ResponseObject.serious();
        }
    }

    @GetMapping("/queryTeamsByDepartmentId")
    @ApiOperation("查看部门下所有团队")
    public ResponseObject<PageableBody<TeamVO>> queryTeamByDepartmentId(
            @RequestParam("departmentId") String departmentId,
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        if (Strings.isBlank(departmentId)
                || NumericUtils.toLong(departmentId) == null) {
            log.warn("[Team] departmentId is invalid, id:{}", departmentId);
            return ResponseObject.fail(ResponseCode.BAD_ARGUMENT_VALUE);
        }
        try {
            return ResponseObject.success(teamService.queryTeamByDepartmentId(NumericUtils.toLong(departmentId), currentPage, pageSize));
        } catch (Exception ex) {
            log.error("[Team] queryTeamByDepartmentId Exception:{}", ex);
            return ResponseObject.serious();
        }

    }


    @PostMapping("/createTeam")
    @ApiOperation("创建团队, 必须在某个部门下")
    public ResponseObject<Void> createTeam(@Valid @RequestBody TeamVO teamVo) {
        if (teamVo == null || Strings.isNotBlank(teamVo.getId())) {
            log.warn("[Team] team info can't be empty and teamId must be empty.");
            return ResponseObject.badArgumentValue();
        }
        if (Strings.isBlank(teamVo.getParentRefTeamId())) {
            log.warn("[Team] team parentId can't be empty.");
            return ResponseObject.badArgumentValue();
        }
        try {
            checkTeamInfo(teamVo);
            teamService.save(teamVo);
            return ResponseObject.success();
        } catch (BaseException ex) {
            return ResponseObject.fail(ex.getCode(), ex.getMessage());

        } catch (Exception ex) {
            log.error("[Team] createTeam fail, Exception:{}", ex);
            return ResponseObject.serious();
        }
    }


    @PostMapping("/addToTeam")
    @ApiOperation("加入到团队")
    public ResponseObject<Void> addToTeam(@Valid @RequestBody AddToTeamParams params) {
        log.info("[Team] add account to team, params:{}", params);
        try {
            TeamEntity entity = checkTeamExist(params.getTeamId());
            accountService.addToTeam(entity, params.checkAndGetAccounts());
            return ResponseObject.success();
        } catch (BaseException ex) {
            return ResponseObject.fail(ex.getCode(), ex.getMessage());
        } catch (Exception ex) {
            log.error("[Team] addToTeam fail, Exception:{}", ex);
            return ResponseObject.serious();
        }

    }

    @PostMapping("/addToDepartment")
    @ApiOperation("加入到部门")
    public ResponseObject<Void> addToDepartment(@Valid @RequestBody AddToTeamParams params) {
        log.info("[Team] add account to department, params:{}", params);
        try {
            TeamEntity entity = checkTeamExist(params.getTeamId());
            accountService.addToDepartment(entity, params.checkAndGetAccounts());
            return ResponseObject.success();
        } catch (BaseException ex) {
            return ResponseObject.fail(ex.getCode(), ex.getMessage());
        } catch (Exception ex) {
            log.error("[Team] addToDepartment fail, Exception:{}", ex);
            return ResponseObject.serious();
        }

    }

    @PostMapping("/deleteTeam/{teamId}")
    @ApiModelProperty("解散团队")
    public ResponseObject<Void> deleteTeam(@PathVariable("teamId") String teamId) {
        log.info("[Team] disband team, teamId:{}", teamId);
        if (Strings.isBlank(teamId)
                || NumericUtils.toLong(teamId) == null) {
            return ResponseObject.badArgumentValue();
        }

        try {
            teamService.deleteTeam(NumericUtils.toLong(teamId), TeamTypeEnum.TEAM);
            return ResponseObject.success();

        } catch (BaseException ex) {
            return ResponseObject.fail(ex.getCode(), ex.getMessage());
        } catch (Exception ex) {
            log.error("[Team] deleteTeam fail, Exception:{}", ex);
            return ResponseObject.serious();
        }

    }

    @PostMapping("/deleteDepartment/{departmentId}")
    @ApiOperation("解散部门，解散之前必须先解散部门下的所有团队")
    public ResponseObject<Void> deleteDepartment(@PathVariable("departmentId") String departmentId) {
        log.info("[Team] disband department, departmentId:{}", departmentId);
        if (Strings.isBlank(departmentId)
                || NumericUtils.toLong(departmentId) == null) {
            return ResponseObject.badArgumentValue();
        }
        try {
            teamService.deleteTeam(NumericUtils.toLong(departmentId),TeamTypeEnum.DEPARTMENT);
            return ResponseObject.success();

        }  catch (BaseException ex) {
            return ResponseObject.fail(ex.getCode(), ex.getMessage());
        } catch (Exception ex) {
            log.error("[Team] deleteDepartment fail, Exception:{}", ex);
            return ResponseObject.serious();
        }

    }

    @GetMapping("/queryList")
    @ApiOperation("查询团队列表 forTest")
    public ResponseObject<List<TeamVO>> queryList() {
        return ResponseObject.success(teamService.querList());
    }


    @GetMapping("/queryMembersByTeamId")
    @ApiOperation("查看团队成员")
    public ResponseObject<PageableBody<AccountVO>> queryMembersByTeamId(
            @RequestParam("teamId") String teamId,
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        try {
            checkTeamExist(teamId);
            return ResponseObject.success(accountService.queryByTeamId(NumericUtils.toLong(teamId), currentPage, pageSize));
        } catch (BaseException ex) {
            return ResponseObject.fail(ex.getCode(), ex.getMessage());
        } catch (Exception ex) {
            log.error("[Team] queryMembersByTeamId fail, Exception:{}", ex);
            return ResponseObject.serious();
        }

    }

    private void checkTeamInfo(TeamVO team) throws BaseException {
        if (Strings.isNotBlank(team.getParentRefTeamId())) {
            checkTeamExist(team.getParentRefTeamId());
        }
        if (Strings.isNotBlank(team.getId())) {
            checkTeamExist(team.getId());
        }
        if (Strings.isNotBlank(team.getLeaderId())) {
            checkAndFillAccount(team);
        }

    }

    private void checkAndFillAccount(TeamVO team) throws BaseException {
        if (NumericUtils.toLong(team.getLeaderId()) == null) {
            log.warn("[Team] parentTeamId is invalid, leaderId:{}.", team.getLeaderId());
            throw new BaseException(ResponseCode.BAD_ARGUMENT_VALUE.getCode());
        }
        AccountVO accountVO = accountService.findById(NumericUtils.toLong(team.getLeaderId()));
        if (accountVO == null) {
            log.warn("[Team] parentTeamId is not exist, leaderId:{}.", team.getLeaderId());
            throw new BaseException(ResponseCode.NOT_EXIST);
        }
        if (Strings.isNotBlank(team.getLeaderName())
                && !team.getLeaderId().equals(accountVO.getName())) {
            log.warn("[Team] teamLeaderName is invalid, leaderName:{}.", team.getLeaderName());
            throw new BaseException(ResponseCode.BAD_ARGUMENT_VALUE);
        }
        team.setLeaderName(accountVO.getName());
    }

    private TeamEntity checkTeamExist(String teamId) throws BaseException {
        if (NumericUtils.toLong(teamId) == null) {
            log.warn("[Team] parentTeamId is invalid, ParentId:{}.", teamId);
            throw new BaseException(ResponseCode.BAD_ARGUMENT_VALUE.getCode());
        }
        TeamEntity teamEntity = teamService.queryTeamWithId(NumericUtils.toLong(teamId));
        if (teamEntity == null) {
            log.warn("[Team] parentTeam is not exist, ParentId:{}.", teamId);
            throw new BaseException(ResponseCode.NOT_EXIST);
        }
        return teamEntity;
    }

}
