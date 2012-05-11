/*****************************************************************************
      SQLJEP - Java SQL Expression Parser 0.2
      November 1 2006
         (c) Copyright 2006, Alexey Gaidukov
      SQLJEP Author: Alexey Gaidukov

      SQLJEP is based on JEP 2.24 (http://www.singularsys.com/jep/)
           (c) Copyright 2002, Nathan Funk
 
      See LICENSE.txt for license information.
*****************************************************************************/

package com.meidusa.amoeba.sqljep.function;

import java.sql.Timestamp;
import java.util.Calendar;

import static java.util.Calendar.*;

import com.meidusa.amoeba.sqljep.function.PostfixCommand;
import com.meidusa.amoeba.sqljep.ASTFunNode;
import com.meidusa.amoeba.sqljep.JepRuntime;
import com.meidusa.amoeba.sqljep.ParseException;

public class Year extends PostfixCommand {
	final public int getNumberOfParameters() {
		return 1;
	}
	
	public Comparable<?>[] evaluate(ASTFunNode node, JepRuntime runtime) throws ParseException {
		node.childrenAccept(runtime.ev, null);
		Comparable<?>  param = runtime.stack.pop();
		return new Comparable<?>[]{param};
	}

	public static Integer year(Comparable<?>  param, Calendar cal) throws ParseException {
		if (param == null) {
			return null;
		}
		if (param instanceof Timestamp || param instanceof java.sql.Date) {
			java.util.Date ts = (java.util.Date)param;
			cal.setTimeInMillis(ts.getTime());
			return new Integer(cal.get(YEAR));
		}
		throw new ParseException(WRONG_TYPE+" year("+param.getClass()+")");
	}

	public Comparable<?> getResult(Comparable<?>... comparables)
			throws ParseException {
		return year(comparables[0],JepRuntime.getCalendar());
	}
}

