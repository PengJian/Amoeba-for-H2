package com.meidusa.amoeba.sqljep.function;

import com.meidusa.amoeba.sqljep.ASTFunNode;
import com.meidusa.amoeba.sqljep.JepRuntime;
import com.meidusa.amoeba.sqljep.ParseException;

/**
 * ���hash�ķ�������
 * 
 * @author hexianmao
 * @version 2008-11-14 ����03:45:03
 */
public class HashParttion extends PostfixCommand {

    private static final int   _unknown = -1;

    /**
     * ���ݶηֲ����壬����ȡģ����һ��Ҫ��2^n����Ϊ����ʹ��x % 2^n == x & (2^n - 1)��ʽ�����Ż����ܡ�
     */
    private static final int[] parttion = new int[] { 0, 64, 128, 192, 256, 320, 384, 448, 512, 576, 640, 704, 768, 832, 896, 960, 1024 };

    /**
     * ÿ������ӳ������ݶ�
     */
    private static final int[] parttionMap;

    private static final long  andValue;

    /**
     * ӳ�����
     */
    static {
        int x = parttion[parttion.length - 1];
        checkModValue(x);
        parttionMap = new int[x];
        andValue = x - 1;
        for (int i = 1; i < parttion.length; i++) {
            for (int j = parttion[i - 1]; j < parttion[i]; j++) {
                parttionMap[j] = (i - 1);
            }
        }
    }

    /**
     * ���x�Ƿ�Ϊ2^n
     */
    private static void checkModValue(int x) {
        if (x <= 0) {
            throw new RuntimeException("error mod value:" + x);
        }
        int n = 0;
        int c = 0;
        int y = 1;
        while (y <= x) {
            if ((x & y) > 0) {
                c++;
                if (c > 1) {
                    throw new RuntimeException("error mod value:" + x);
                }
            }
            y = 1 << (++n);
        }
    }

    public Comparable<?>[] evaluate(ASTFunNode node, JepRuntime runtime) throws ParseException {
        node.childrenAccept(runtime.ev, null);
        Comparable<?> param = runtime.stack.pop();
        return new Comparable<?>[] { param };
    }

    public int getNumberOfParameters() {
        return 1;
    }

    public Comparable<?> getResult(Comparable<?>... comparables) throws ParseException {
        if (comparables[0] != null && comparables[0] instanceof Long) {
            Long l = (Long) comparables[0];
            if (l == -1L) {
                return _unknown;
            }
            return parttionMap[(int) (l & andValue)];
        }
        return _unknown;
    }

}
