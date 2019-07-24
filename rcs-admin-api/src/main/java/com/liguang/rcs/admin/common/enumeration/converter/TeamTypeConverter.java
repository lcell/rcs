package com.liguang.rcs.admin.common.enumeration.converter;

import com.liguang.rcs.admin.common.enumeration.TeamTypeEnum;

public class TeamTypeConverter extends BaseStringToIEnumConverter<TeamTypeEnum> {
    @Override
    protected TeamTypeEnum[] values() {
        return TeamTypeEnum.values();
    }
}
