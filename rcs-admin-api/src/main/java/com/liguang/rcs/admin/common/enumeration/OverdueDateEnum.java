package com.liguang.rcs.admin.common.enumeration;

public enum OverdueDateEnum {
    DAY0("0") {
        @Override
        protected boolean isIn(int day) {
            return day < 1;
        }
    },
    DAY1_30("1~30天") {
        @Override
        protected boolean isIn(int day) {
            return (day >= 1 && day <= 30);
        }
    },
    DAY31_60("31～60天") {
        @Override
        protected boolean isIn(int day) {
            return (day >= 31 && day <= 60);
        }
    },
    DAY61_90("61～90天") {
        @Override
        protected boolean isIn(int day) {
            return (day >= 61 && day <= 90);
        }
    },
    DAY91_180("91～180天") {
        @Override
        protected boolean isIn(int day) {
            return (day >= 91 && day <= 180);
        }
    },
    DAY181_365("181～365天") {
        @Override
        protected boolean isIn(int day) {
            return (day >= 181 && day <= 365);
        }
    },
    DAY_LT_365(">365天") {
        @Override
        protected boolean isIn(int day) {
            return day > 365;
        }
    };

    private final String column;

    OverdueDateEnum(String column) {
        this.column = column;
    }

    public static OverdueDateEnum valueOfCloumn(String overdueNumOfDate) {
        for(OverdueDateEnum overdueDateEnum : values()) {
            if (overdueNumOfDate.equals(overdueDateEnum.getColumn())) {
                return  overdueDateEnum;
            }
        }
        return DAY0;
    }

    protected abstract boolean isIn(int day);

    public static OverdueDateEnum convertToEnum(int day) {
        for (OverdueDateEnum overdueDateEnum : OverdueDateEnum.values()) {
            if (overdueDateEnum.isIn(day)) {
                return overdueDateEnum;
            }
        }
        return DAY0;
    }

    public String getColumn() {
        return column;
    }
}
