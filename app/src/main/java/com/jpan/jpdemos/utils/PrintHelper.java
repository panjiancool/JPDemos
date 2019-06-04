package com.jpan.jpdemos.utils;

public class PrintHelper {

    private static PrintHelper instance = null;

    private StringBuffer sb = new StringBuffer();

    public static PrintHelper getInstance() {
        if (instance == null) {
            instance = new PrintHelper();
        }
        return instance;
    }

    /**
     * 打印内容并换行
     *
     * @param content 被打印的内容
     * @return 打印结果
     */
    public String print(String content) {
        return sb.append(content).append("\r\n").toString();
    }

    /**
     * 清除内容
     */
    public void clear() {
        sb.setLength(0);
    }

}
