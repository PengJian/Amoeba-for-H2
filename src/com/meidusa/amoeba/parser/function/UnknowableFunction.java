package com.meidusa.amoeba.parser.function;

import java.util.List;

import com.meidusa.amoeba.parser.expression.Expression;
import com.meidusa.amoeba.sqljep.ParseException;

/**
 * δ֪�ĺ����������Ҫ��sqlparser���������������ֲ�ϣ��ʵ�֣�����Բ��øú���
 * @author struct
 *
 */
public class UnknowableFunction extends AbstractFunction {

	@SuppressWarnings("unchecked")
	public Comparable evaluate(List<Expression> list, Object[] parameters)
			throws ParseException {
		return null;
	}

}
