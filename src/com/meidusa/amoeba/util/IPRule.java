package com.meidusa.amoeba.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPRule {
	private static final Map<String,Pattern> ipPattern = new HashMap<String,Pattern>();
	
	/**
	 * IP Pattern
	 * @author struct
	 *
	 */
	static interface IPPattern{
		boolean isMatch(String ip);
	}
	
	/**
	 * ���֡�ֻ��С��256
	 * @author struct
	 *
	 */
	static class NumPattern implements IPPattern{
		int source;
		
		public NumPattern(String string){
			source = Integer.valueOf(string);
		}
		public boolean isMatch(String ip){
			return source == Integer.valueOf(ip);
		}
	}
	
	/**
	 * ֻ�ܰ���һ��"-"���ţ�������ǰ����С��256��ǰ�����ֱȺ�������С
	 * @author struct
	 *
	 */
	static class SegmentPattern implements IPPattern{
		int begin;
		int end;
		public SegmentPattern(String string){
			String[] temp = StringUtil.split(string,'-');
			begin = Integer.parseInt(temp[0]);
			end = Integer.parseInt(temp[1]);
		}
		
		public boolean isMatch(String ip){
			int ippart = Integer.valueOf(ip);
			return begin <= ippart && ippart <= end;
		}
	}
	
	/**
	 * IP Pattern ֻ�ܰ���һ��*�����ڽ�β�� 
	 * @author struct
	 *
	 */
	static class StarPattern implements IPPattern{
		String starString;
		
		public StarPattern(String ipRegexString){
			starString = StringUtil.replace(ipRegexString,"*","");
		}
		
		public boolean isMatch(String ip){
			return ip.startsWith(starString);
		}
	}
	
	static class IPRegex implements IPPattern{
		IPPattern[] patterns = new IPPattern[4];
		public IPRegex(String ip){
			String[] tmps = StringUtil.split(ip,'.');
			for(int i=0;i< tmps.length;i++){
				if(tmps[i].contains("-")){
					patterns[i] = new SegmentPattern(tmps[i]);
				}else if(tmps[i].contains("*")){
					patterns[i] = new StarPattern(tmps[i]);
				}else{
					patterns[i] = new NumPattern(tmps[i]);
				}
			}
		}
		
		public boolean isMatch(String ip) {
			String tmps[] = StringUtil.split(ip,'.');
			for(int i=0;i<tmps.length;i++){
				if(!patterns[i].isMatch(tmps[i])){
					return false;
				}
			}
			return true;
		}
		
	}
	
	static class Regex{
		static boolean IsMatch(String ip,String ipRegexString){
			Pattern pattern = ipPattern.get(ipRegexString);
			if(pattern == null){
				pattern = Pattern.compile(ipRegexString);
				ipPattern.put(ipRegexString, pattern);
			}
			Matcher m = pattern.matcher(ip);
			return m.matches();
		}
	}
	/**
	 * <pre>
	 * �ж�ָ����IP�Ƿ���ָ���� �����������(����������� -?*��
	 * rule[192.*.1.236-239:yes;192.*.1.226:no;218.85.*.*:no]���һ����Ҫ��";"�ֺ�
	 * ǰ��Ĺ������ȼ���
	 * ע�⣬�����е� * - ? ����ͬʱ������ͬһ������ ��: 192.168.*?.123 �����
	 * ����ͬһ����ֻ����һ��, �� 192.16*.1.*,  192.1**.1.1 �Ǵ���ģ������� ?�Ŵ���
	 *	<param name="rule">(192.*.1.236-239:yes;192.*.1.226:no;218.85.*.*:no) ���һ������Ҫ�ٶ��";"�ֺ�</param>
	 *	<param name="ip">192.168.1.237(����ȷ��IP�����)</param>
	 * </pre>
	 */

	public static boolean IsAllowIP(String rule, String ip) throws Exception
	{
        String[] ruleArray = StringUtil.split(rule,";");
        return isAllowIP(ruleArray,ip);
	}
	
	public static boolean isAllowIP(String[]ruleArray ,String ip) throws Exception{
		//IP������ʽ
	    String ipRegexString = "^((2[0-4][0-9]|25[0-5]|[01]?[0-9][0-9]?).){3}(2[0-4][0-9]|25[0-5]|[01]?[0-9][0-9]?)$";
	    //���IP��ַ�Ǵ�ģ���ֹ
	    if (!Regex.IsMatch(ip, ipRegexString))
	    {
	        throw new Exception("����ip���󣺴����IP��ַ" + ip);
	    }
	    
		//�������
        String[] ipdata = StringUtil.split(ip,".");
        boolean retValue = false;//Ĭ�Ϸ���ֵ

        //����������֤
        for(String s : ruleArray){
            boolean IsFind = false;
            String[] data = StringUtil.split(s,':');
            //���û����:�ֿ�
            if (data.length != 2) { throw new Exception("����:�ֿ� ��:192.168.1.1:yes"); }

            String ruleIp = data[0];//�õ� 192.168.20-60.*:yes �е� [192.168.20-60.*]����
            retValue = data[1].equalsIgnoreCase("yes") ? true : false;


            String[] ruleIpArray = StringUtil.split(ruleIp,'.');
            if (ruleIpArray.length != 4) { throw new Exception("IP���ִ���"); }

            //region
            for (int i = 0; i < 4; i++)
            {
            	boolean AA = ruleIpArray[i].contains("*");
            	boolean BB = ruleIpArray[i].contains("-");
            	boolean CC = ruleIpArray[i].contains("?");
                if ((AA && BB) || (AA && CC) || (BB && CC) || (AA && BB && CC))
                {
                    throw new Exception("�����ĸ�ʽ�Ǵ����,192.168.15-20*,*��-���ܰ�����ͬһ������! ");
                }
                else if (!AA && !BB && !CC) //û�а��� * �� - �� ?
                {
                    if (!Regex.IsMatch(ruleIpArray[i], "^2[0-4][0-9]|25[0-5]|[01]?[0-9][0-9]?$"))
                    {
                        throw new Exception("IP�δ���Ӧ����1~255֮��:" + ruleIpArray[i]);
                    }
                    else
                    {
                        //region �����ж� 111111111111
                        if (ruleIpArray[i].equalsIgnoreCase(ipdata[i]))
                        {
                            IsFind = true;
                        }
                        else
                        {
                            IsFind = false;
                            break;
                        }
                        //endregion
                    }
                }
                else if (AA && !BB && !CC) //���� [*] ��
                {
                    if (!ruleIpArray[i].equalsIgnoreCase("*"))
                    {
                        if (ruleIpArray[i].startsWith("*") || !ruleIpArray[i].endsWith("*") || ruleIpArray[i].contains("**"))
                        {
                            throw new Exception("IP�е�*���֣�������*��ͷ������������**��ֻ����*��β");
                        }
                    }
                    else
                    {
                        //region �����ж�22222222222222
                        if (ipdata[i].startsWith(ruleIpArray[i].replace("*", "")))
                        {
                            IsFind = true;
                        }
                        else
                        {
                            IsFind = false;
                            break;
                        }
                        //endregion
                    }
                }
                else if (BB && !AA && !CC) //���� [-] ��
                {

                    String[] temp = StringUtil.split(ruleIpArray[i],'-');
                    if (temp.length != 2)
                    {
                        throw new Exception("IP�δ���, ��:23-50,��1~255֮��");
                    }
                    else
                    {
                    	int[] nums = {Integer.parseInt(temp[0]),Integer.parseInt(temp[1])};
                        if (nums[0] < 1 || nums[1]  > 255)
                        {
                            throw new Exception("IP�δ���, ��:23-50,��1~255֮��");
                        }
                        else
                        {
                            int ipNum = Integer.parseInt(ipdata[i]);
                            if (ipNum >= nums[0] && ipNum <= nums[1])
                            {
                                IsFind = true;
                            }
                            else
                            {
                                IsFind = false;
                                break;
                            }
                        }
                    }
                }
                else if (CC && !AA & !BB) //���� [?] ��
                {
                    //ȥ���ʺź� 
                    String temp = ruleIpArray[i].replace("?", "");
                    if (!Regex.IsMatch(temp,"^[0-9][0-9]?$") || temp.length() > 2)
                    {
                        throw new Exception("IP�δ���:" + ruleIpArray[i]);
                    }
                    else
                    {
                        if (ruleIpArray[i].length() != ipdata[i].length())
                        {
                            IsFind = false;
                            break;
                        }
                        else
                        {
                            String tempRegstring = "^" + ruleIpArray[i].replace("?", "([0-9])*") + "$";
                            if (Regex.IsMatch(ipdata[i],tempRegstring))
                            {
                                IsFind = true;
                            }
                            else
                            {
                                IsFind = false;
                                break;
                            }
                        }
                        //endregion
                    }
                }
                else
                {
                    IsFind = false;
                    break;
                }


            }
            //endregion
            if (IsFind)
            {
                return retValue;//IP������ :����� yes/no ��Ӧ��  true false
            }
        }
        return false;
	}
	
	public static void main(String[] args){
		try {
			long start = System.currentTimeMillis();
			for(int i=0;i<1;i++){
				IPRule.IsAllowIP("192.*.1.236-239:yes","192.84.1.226");
			}
			System.out.println(System.currentTimeMillis()-start);
			
			start = System.currentTimeMillis();
			IPRegex regex = new IPRegex("192.1*.1.236-239");
			for(int i=0;i<1000000;i++){
				regex.isMatch("192.181.1.236");
				//regex.isMatch("192.181.1.236");
			}
			System.out.println(System.currentTimeMillis()-start);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
