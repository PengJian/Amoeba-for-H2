/**
 * <pre>
 * 	This program is free software; you can redistribute it and/or modify it under the terms of 
 * the GNU AFFERO GENERAL PUBLIC LICENSE as published by the Free Software Foundation; either version 3 of the License, 
 * or (at your option) any later version. 
 * 
 * 	This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU AFFERO GENERAL PUBLIC LICENSE for more details. 
 * 	You should have received a copy of the GNU AFFERO GENERAL PUBLIC LICENSE along with this program; 
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * </pre>
 */
package com.meidusa.amoeba.util;

import org.apache.log4j.Level;

/**
 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
 */
public interface Reporter {

    /**
     * @param buffer ��װreport��Ϣ�� StringBuilder
     * @param now report ����ʼ����report�ĵ�ʱʱ�䣬��λms
     * @param sinceLast ��һ��report��ʱ��
     * @param reset �Ƿ���Ҫ����ͳ��
     */
    public void appendReport(StringBuilder buffer, long now, long sinceLast, boolean reset, Level level);

    // public void alertStartReport();

    public static interface SubReporter {

        public void appendReport(StringBuilder buffer, long now, long sinceLast, boolean reset, Level level);
    }
}
