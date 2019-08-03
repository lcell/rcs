package com.liguang.rcs.admin.util;

import java.util.Collection;

public class CollectionUtils {


    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean isEmpty(T[] arr) {
        return arr == null || arr.length == 0;
    }
}
