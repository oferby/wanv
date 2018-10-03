package com.huawei.sdn.commons.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amir Kost on 7/22/14.
 *
 * This class is intended parse number expressions and return an int[].
 * For example: 100-110,200,250-257
 *
 */
public class NumberParser {

    public static int[] parseNumbers(String expression) {
        String[] groups = expression.split(",");
        List<Integer> numberList = new ArrayList<Integer>();
        for(String group : groups) {
            String[] range = group.split("-");
            if(range.length == 1) {
                numberList.add(Integer.decode(range[0]));
            }
            else {
                int min = Integer.decode(range[0]);
                int max = Integer.decode(range[1]);
                fillRange(numberList, min, max);
            }
        }
        Object[] objects = numberList.toArray();
        int[] res = new int[objects.length];
        for(int i = 0; i < objects.length; i++) {
            res[i] = (Integer) objects[i];
        }
        return res;
    }

    private static void fillRange(List<Integer> list, int min, int max) {
        for(int i = min; i <= max; i++) {
            list.add(i);
        }
    }

}
