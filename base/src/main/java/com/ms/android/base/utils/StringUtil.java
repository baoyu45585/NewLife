package com.ms.android.base.utils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串类
 * @author lin
 *
 */
public class StringUtil {
	/**
	 * 判断字符串是否为空白字符
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNullOrBlank(String s) {
		if ((null == s) || (0 == s.trim().length())) {
			return true;
		}

		return false;
	}

	/**
	 * 判断是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumericPattern(String str){
		if (str==null)return false;
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			return false;
		}
		return true;
	}

	public static boolean isNotNullOrBlank(String s) {
		return !isNullOrBlank(s);
	}

	public static boolean isNullOrBlank(String... ss) {
		for (String s : ss) {
			if ((null == s) || (0 == s.trim().length())) {
				return true;
			}
		}

		return false;
	}

	public static boolean isNotNullOrBlank(String... ss) {
		return !isNullOrBlank(ss);
	}

//	/**
//	 * 判断控件文本是否为空白字符
//	 *
//	 * @param tv
//	 * @return
//	 */
//	public static boolean isNullOrBlank(TextView tv) {
//		return isNullOrBlank(TextUtil.text(tv));
//	}
//
//	public static boolean isNotNullOrBlank(TextView tv) {
//		return !isNullOrBlank(TextUtil.text(tv));
//	}
//
//	public static boolean isNullOrBlank(TextView... tvs) {
//		for (TextView tv : tvs) {
//			if ((null == TextUtil.text(tv))
//					|| (0 == TextUtil.text(tv).length())) {
//				return true;
//			}
//		}
//
//		return false;
//	}

//	public static boolean isNotNullOrBlank(TextView... tvs) {
//		return !isNullOrBlank(tvs);
//	}

	/**
	 * 获取随机uuid
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}



	/**
	 * 截取字符串
	 */
	public static String Cut_String(String str, int start, int end) {
		return str.substring(start, end);
	}

	/**
	 * 根据特定字符截取字符串(前面一段)
	 */
	public static String Cut_String_char(String str, String c) {
		String urlpath = StringUtil.Cut_String(str, 0, str.lastIndexOf(c));
		return urlpath;
	}
	/**
	 * 根据特定字符截取字符串(后面一段)
	 */
	public static String Cut_String_char_hou(String str, String c) {
		String urlpath = StringUtil.Cut_String(str, str.lastIndexOf(c)+1,str.length() );
		return urlpath;
	}

	/**
	 * 截取字符串并拼接成服务器图片路径
	 */
	public static String Joint_String(String str) {
		String urlpath = StringUtil.Cut_String(str,

		0, str.lastIndexOf('.'))

		+ "_300x600" + StringUtil.Cut_String(str,

		str.lastIndexOf('.'), str.length());

		return urlpath;
	}

	/**
	 * 判断是否全是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 从数组中找出某个元素的下标
	 * @param str
	 * @return
     */
	public static int getArraySubscript(String[] strings,String str)
	{
		for(int i=0 ; i<strings.length ; i++)
		{
				if(strings[i].equals(str))
				{
					return i;
				}
		}
		return 0;
	}
}
