package cn.itcast.streaming.utils;

/**
 * 字符串处理的工具类
 * 1）将字符串进行反转
 */
public class StringUtil {
    /**
     * 1）将字符串进行反转：递归（不推荐）、数组倒序拼接、冒泡对调、使用StringBuffer的reverse方法等实现方式
     * @param orig
     * @return
     */
    public static String reverse(String orig){
        char[] chars = orig.toCharArray();
        int num = chars.length - 1;
        int halfLength = num / 2;
        for (int i = 0; i <= halfLength; i++) {
            char temp = chars[i];
            chars[i] = chars[num -1];
            chars[num - i] = temp;
        }
        return new String(chars);
    }

    public static void main(String[] args) {
        System.out.println(reverse("zhang"));
    }
}
