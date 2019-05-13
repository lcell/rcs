package com.liguang.rcs.admin.web.dashboard;

import com.liguang.rcs.admin.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 提供图标展示面板
 */
@RestController
@RequestMapping("/admin/dashboard")
@Validated
@Api(tags = "报表展示API")
public class DashBoardController {
    private static final Log LOG = LogFactory.getLog(DashBoardController.class);
    @GetMapping("")
    @ApiOperation(value = "统计信息")
    public Object info() {
        Map<String, Integer> data = new HashMap<>();
        data.put("userTotal", 10);
        data.put("goodsTotal", 123);
        data.put("productTotal", 456);
        data.put("orderTotal", 10);
        return ResponseUtil.ok(data);
    }
}
