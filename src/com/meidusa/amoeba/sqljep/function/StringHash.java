package com.meidusa.amoeba.sqljep.function;

import com.meidusa.amoeba.sqljep.ASTFunNode;
import com.meidusa.amoeba.sqljep.JepRuntime;
import com.meidusa.amoeba.sqljep.ParseException;

/**
 * ���String��hash�㷨
 * 
 * @author hexianmao
 * @version 2008-10-27 ����05:49:02
 */
public class StringHash extends PostfixCommand {

    private static final int  _hash_len = 8;
    private static final int  _bit_len  = 5;

    private static final long _unknown  = -1L;

    public int getNumberOfParameters() {
        return 1;
    }

    public Comparable<?>[] evaluate(ASTFunNode node, JepRuntime runtime) throws ParseException {
        node.childrenAccept(runtime.ev, null);
        Comparable<?> param = runtime.stack.pop();
        return new Comparable<?>[] { param };
    }

    public Comparable<?> getResult(Comparable<?>... comparables) throws ParseException {
        if (comparables[0] != null && comparables[0] instanceof String) {
            return hash((String) comparables[0]);
        }
        return _unknown;
    }

    /**
     * <pre>
     * �ַ���hash�㷨��s[0]*31&circ;(n-1) + s[1]*31&circ;(n-2) + ... + s[n-1]
     * ����s[]Ϊ�ַ������ַ����飬����ɳ���ı��ʽΪ��
     * h = 31*h + s.charAt(i); =&gt; h = (h &lt;&lt; 5) - h + s.charAt(i);
     * ע����hash���ַ������˳����޶�����֤�������ᳬ���������͵ķ�Χ��
     * </pre>
     */
    private static long hash(String s) {
        long h = 0;
        for (int i = 0; (i < _hash_len && i < s.length()); i++) {
            h = (h << _bit_len) - h + s.charAt(i);
        }
        return h;
    }
}
