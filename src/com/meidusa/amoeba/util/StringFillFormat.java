package com.meidusa.amoeba.util;

/**
 * �ַ�������ʽ������
 * 
 * @author hexianmao
 * @version 2008-11-24 ����12:58:17
 */
public class StringFillFormat {

    // �Ҷ����ʽ���ַ���
    public static final int   ALIGN_RIGHT      = 0;

    // ������ʽ���ַ���
    public static final int   ALIGN_LEFT       = 1;

    private static final char defaultSplitChar = ' ';

    /**
     * ��ʽ���󷵻ص��ַ���
     * 
     * @param s ��Ҫ��ʽ����ԭʼ�ַ�����Ĭ�ϰ�����롣
     * @param fillLength ��䳤��
     * @return String
     */
    public static String format(String s, int fillLength) {
        return format(s, fillLength, defaultSplitChar, ALIGN_LEFT);
    }

    /**
     * ��ʽ���󷵻ص��ַ���
     * 
     * @param i ��Ҫ��ʽ�����������ͣ�Ĭ�ϰ��Ҷ��롣
     * @param fillLength ��䳤��
     * @return String
     */
    public static String format(int i, int fillLength) {
        return format(Integer.toString(i), fillLength, defaultSplitChar, ALIGN_RIGHT);
    }

    /**
     * ��ʽ���󷵻ص��ַ���
     * 
     * @param l ��Ҫ��ʽ�����������ͣ�Ĭ�ϰ��Ҷ��롣
     * @param fillLength ��䳤��
     * @return String
     */
    public static String format(long l, int fillLength) {
        return format(Long.toString(l), fillLength, defaultSplitChar, ALIGN_RIGHT);
    }

    /**
     * @param s ��Ҫ��ʽ����ԭʼ�ַ���
     * @param fillLength ��䳤��
     * @param fillChar �����ַ�
     * @param align ��䷽ʽ�������仹���ұ���䣩
     * @return String
     */
    public static String format(String s, int fillLength, char fillChar, int align) {
        if (s == null) {
            s = "";
        } else {
            s = s.trim();
        }
        int charLen = fillLength - s.length();
        if (charLen > 0) {
            char[] fills = new char[charLen];
            for (int i = 0; i < charLen; i++) {
                fills[i] = fillChar;
            }
            StringBuilder str = new StringBuilder(s);
            switch (align) {
                case ALIGN_RIGHT:
                    str.insert(0, fills);
                    break;
                case ALIGN_LEFT:
                    str.append(fills);
                    break;
                default:
                    str.append(fills);
            }
            return str.toString();
        } else {
            return s;
        }
    }
}
