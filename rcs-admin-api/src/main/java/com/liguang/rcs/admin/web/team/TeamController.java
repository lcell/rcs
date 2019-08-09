package com.liguang.rcs.admin.web.team;

import com.liguang.rcs.admin.common.response.ResponseObject;
import com.liguang.rcs.admin.service.TeamService;
import com.liguang.rcs.admin.web.account.AccountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/rcs/team")
@Api(tags = "团队管理")
public class TeamController {
    @Autowired
    private TeamService teamService;

    @PostMapping("/createDepartment")
    @ApiOperation("创建部门")
    public ResponseObject createDepartment(@Valid @RequestBody TeamVO teamVo) {

        return null;
    }

    @GetMapping("/queryTeamsByDepartmentId")
    @ApiOperation("查看部门下所有团队")
    public ResponseObject<List<TeamVO>> queryTeamByDepartmentId(@RequestParam("departmentId") String departmentId) {
        return null;

    }


    @PostMapping("/createTeam")
    @ApiOperation("创建团队")
    public ResponseObject createTeam(@Valid @RequestBody TeamVO teamVo) {
        return null;

    }

    @PostMapping("/addToTeam")
    @ApiOperation("加入到团队")
    public ResponseObject addToTeam(@Valid @RequestBody AddToTeamParams params) {
        return null;

    }

    @GetMapping("/queryMembersByTeamId")
    @ApiOperation("查看团队成员")
    public ResponseObject<List<AccountVO>> queryMembersByTeamId(@RequestParam("teamId") String teamId) {
        return null;

    }



}
