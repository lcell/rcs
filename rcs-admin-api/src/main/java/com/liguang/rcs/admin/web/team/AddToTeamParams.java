package com.liguang.rcs.admin.web.team;

import com.google.common.base.Strings;
import com.liguang.rcs.admin.util.NumericUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@ApiModel("添加成员到团队中参数")
@Data
@Slf4j
public class AddToTeamParams {
    @ApiModelProperty(value = "团队ID", dataType = "String", required = true)
    @NotBlank(message = "团队ID不可为空")
    private String teamId;

    @ApiModelProperty(value = "成员ID", dataType = "List<String>", required = true)
    @NotNull(message = "成员ID不可为空")
    private List<String> accountIds;


    List<Long> checkAndGetAccounts() {
        return accountIds.stream().map(NumericUtils::toLong)
                .filter(data -> data != null)
                .collect(Collectors.toList());
    }

}
