package com.liguang.rcs.admin.common.enumeration;

public enum OverdueDateEnum implements IEnum {
    DAY0("", 0), DAY1_5("1~5天", 1), DAY6_30("6~30天", 2),
    DAY1_30("1~30天", 3), DAY31_60("31～60天", 4), DAY61_90("61～90天", 5),
    DAY91_180("91～180天", 6), DAY181_365("181～365天", 7), DAY_LT_365(">365天", 8);

    private final String code;
    private final int order;

    OverdueDateEnum(String code, int order) {
        this.code = code;
        this.order = order;
    }

    public static OverdueDateEnum valueOfColumn(String overdueNumOfDate) {
        for (OverdueDateEnum overdueDateEnum : values()) {
            if (overdueNumOfDate.equals(overdueDateEnum.getCode())) {
                return overdueDateEnum;
            }
        }
        return DAY0;
    }

    public static OverdueDateEnum convertToEnumByMonth(int deltaMonth) {

        return deltaMonth < 1 ? DAY0
                : deltaMonth == 1 ? DAY1_30
                : deltaMonth == 2 ? DAY31_60
                : deltaMonth == 3 ? DAY61_90
                : deltaMonth < 7 ? DAY91_180
                : deltaMonth < 13 ? DAY181_365 : DAY_LT_365;
    }

    /**/
    public static OverdueDateEnum convertToEnum(long day) {
        return day < 1 ? DAY0
                : day <= 5 ? DAY1_5
                : day <= 30 ? DAY6_30
                : day <= 60 ? DAY31_60
                : day <= 90 ? DAY61_90
                : day <= 180 ? DAY91_180
                : day <= 365 ? DAY181_365 : DAY_LT_365;

    }

    public boolean isGt(OverdueDateEnum other) {
        return other == null || other.getOrder() < this.getOrder();
    }

    @Override
    public String getCode() {
        return code;
    }

    public int getOrder() {
        return order;
    }
}
