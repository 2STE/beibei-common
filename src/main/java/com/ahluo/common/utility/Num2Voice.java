package com.ahluo.common.utility;

/**
 * @author luochao . 2017-05-08.
 */
public class Num2Voice {
    private static String[] units = {"", "十", "百", "千", "万", "十万", "百万", "千万", "亿"};
    private static String[] digts = {"零", "壹", "贰", "参", "肆", "伍", "陆", "柒", "捌", "玖"};

    public static void main(String[] args) {
        work(1024);
    }

    private static void work(int num) {
        int old_num = num;
        int cur_num = 0;
        int level = 0;
        StringBuilder res = new StringBuilder("");
        while (num > 0 && (cur_num = num % 10) >= 0) {
            res.append(process(cur_num, level));
            num = num / 10;
            level++;
        }
        print(old_num + ":" + res.reverse());
    }

    private static String process(int cur_num, int level) {
        return (cur_num == 0 ? "" : units[level]) + "" + digts[cur_num];
    }

    private static <T> void print(T t) {
        System.out.println("current: " + t);
    }
}