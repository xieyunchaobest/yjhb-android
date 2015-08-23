package cn.com.xyc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class StringUtil {

	/**
	 * 删除str指定的后�?
	 * 
	 * @param str
	 * @param suffix
	 * @return
	 */
	public static String removeSuffix(String str, String suffix) {
		if (null == str)
			return null;
		if ("".equals(str.trim()))
			return "";

		if (null == suffix || "".equals(suffix))
			return str;

		if (str.endsWith(suffix)) {
			return str.substring(0, str.length() - suffix.length());
		}

		return "";
	}

	
	/**
	 * 按长度截取字符串
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String subString(String str, int length) {
		if (isBlank(str)) {
			return "";
		} else if (str.length() > length) {
			return str.substring(0, length);
		} else {
			return str;
		}
	}

	/**
	 * 是否是空的（包括null�?""、空格）
	 * 
	 * @param str
	 * @return
	 * @throws UtilException
	 */
	public static boolean isBlank(String str) {
		if (null == str)
			return true;
		if ("".equals(str.trim()))
			return true;
		if("null".equals(str)) return true;

		return false;
	}

	public static boolean isBlank(Long str) {
		if (null == str)
			return true;
		return false;
	}

	/**
	 * 将对象转成String
	 * 
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString().trim();
	}
	
	
	public static String add(String str,int num){
		int i = num;
		if(!isBlank(str)){
			int intStr = Integer.parseInt(str);
			i = i + intStr;
		}
		
		return Integer.toString(i) ;
	}
	
	/**
	 * 判断�?个字符串是否都为数字
	 */
	public  static boolean isDigit(String strNum) {  
	    Pattern pattern = Pattern.compile("[0-9]{1,}");  
	    Matcher matcher = pattern.matcher((CharSequence) strNum);  
	    return matcher.matches();  
	}
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String getString(String str) {
		if (null == str)
			return "";
		return str;

	}

	/**
	 * 计算两个数字字符串的�?
	 * 
	 * @param augend
	 * @param addend
	 * @return
	 * @throws UtilException
	 */
	public static String getSum(String augend, String second, String addend) {
		if (augend == null)
			augend = "0";
		if (second == null)
			second = "0";
		if (addend == null)
			addend = "0";
		int sum = Integer.parseInt(augend) + Integer.parseInt(second) + Integer.parseInt(addend);
		return new Integer(sum).toString();
	}

	public static String change(String str, int n, boolean isLeft) {
		if (str == null || str.length() >= n)
			return str;
		String s = "";
		for (int i = str.length(); i < n; i++)
			s += "0";
		if (isLeft)
			return s + str;
		else
			return str + s;
	}

	public static String getInString(String str) {
		if (str == null)
			return null;
		StringBuffer sb = new StringBuffer("");
		String s[] = str.split(",");
		if (s != null && s.length > 0) {
			for (int i = 0; i < s.length; i++) {
				if (i != 0)
					sb.append(",");
				sb.append("'").append(s[i]).append("'");
			}
		}
		return sb.toString();
	}
	
	public static String subInStringByFlag(String str,String flag) {
		if (isBlank(str))
			return null;
		StringBuffer sb = new StringBuffer(str);
		int index = str.lastIndexOf(flag);
		if (index < 0) {
			return str;
		} else {
			str = sb.delete(sb.length()-flag.length(), sb.length()).toString();
			index = str.indexOf(flag);
			if (index < 0) {
				return str;
			} else {
				return sb.deleteCharAt(0).toString();
			}
		}
	}
	/**
	 * 根据标识获取str中最后一个flag后的内容
	 * 
	 * @param str
	 * @param flag
	 * @return
	 */
	public static String getLastStr(String str, String flag) {
		if (isBlank(str))
			return null;
		int index = str.lastIndexOf(flag);
		if (index < 0) {
			return str;
		} else {
			return str.substring(index + flag.length());
		}

	}

	/**
	 * 获取正则表达式匹配的字符串，�?$符处理一下，不然匹配时会认作分组
	 * 
	 * @param str
	 * @return
	 */
	public static String getRegexStr(String str) {
		String ret = "";
		if (isBlank(str))
			return "";
		if (str.indexOf('$', 0) > -1) {
			while (str.length() > 0) {
				if (str.indexOf('$', 0) > -1) {
					ret += str.subSequence(0, str.indexOf('$', 0));
					ret += "\\$";
					str = str.substring(str.indexOf('$', 0) + 1, str.length());
				} else {
					ret += str;
					str = "";
				}
			}

		} else {

			ret = str;
		}

		return ret;

	}
	
	
	/**
	 * 根据正则表达式截取匹配的字符串列�?
	 * 从指定字符串中，按正则表达式要求，找出其中能匹配上的字符串列�?
	 * @param str
	 * @param regx
	 * @return
	 */
	public static List catchStr(String str,String regx){
		if (isBlank(str))
			return null;
		
		List ret = new ArrayList();
		Pattern pattern = Pattern.compile(regx);
		Matcher matcher = pattern.matcher(str);
		int start = 0;
		int end = 0;
		while(matcher.find()){
			start = matcher.start();
			end = matcher.end();
			ret.add(str.substring(start,end));
		}
		return ret;
		
	}

	/**
	 * 过滤换行�?
	 * 
	 * @param str
	 * @return
	 */
	public static String filterNextLine(String str) {
		if (isBlank(str))
			return "";
		return str.replaceAll("[\n\r\u0085\u2028\u2029]", "");
	}
	public static String lpad(String str ,int length) {  
		if (isBlank(str))
			return "";
		int  j= str.length();
		for(int i=j;i<length;i++){
        	str ="0"+str;
        }
        return str;
    }
	
 
	
 
 
	
	public static void main(String[] args)  {
		//String str = "kkjlj l.class";
//		String str = "where instr($GET_PHONE_GROUP_TYPE,'[31]')>0 and nvl(replace($GET_PHONE_GROUP_SPEC_STR,'[31]',''),'N')<>'N' and $GET_PHONE_GROUP_TYPE=1";
//		//log.debug(StringUtil.removeSuffix(str, ".class"));
//
//		//System.out.println(StringUtil.getLastStr("com.cattsoft.sp.component.domain.SoManagerDOM","."));
//
//		//String a = "com.cattsoft.sp. \r com \n ponent.domain.SoManagerDOM";
//		//System.out.println(StringUtil.filterNextLine(a));
//		//System.out.println(StringUtil.subString("aa",10));
//		
//		//System.out.println(StringUtil.add("1", 11));
//		
//		System.out.println(StringUtil.catchStr(str,"\\$\\w*\\b").get(0));
//		System.out.println(StringUtil.catchStr(str,"\\$\\w*\\b").get(1)); 
//		System.out.println(StringUtil.catchStr(str,"\\$\\w*\\b").get(2)); 
		
		unicodeToChinese("");
	}

 
	
	public static String unicodeToChinese(String unicode) {
		 char[]c=unicode.toCharArray();
		 System.out.println(new String(c));
		 return new String(c);
	}
	
	

    public static String decodeUnicode(String theString) {  
    	if(isBlank(theString))return "";
     char aChar;  

     int len = theString.length();  

     StringBuffer outBuffer = new StringBuffer(len);  

     for (int x = 0; x < len;) {  

      aChar = theString.charAt(x++);  

      if (aChar == '\\') {  

       aChar = theString.charAt(x++);  

       if (aChar == 'u') {  

        // Read the xxxx  

        int value = 0;  

        for (int i = 0; i < 4; i++) {  

         aChar = theString.charAt(x++);  

         switch (aChar) {  

         case '0':  

         case '1':  

         case '2':  

         case '3':  

         case '4':  

         case '5':  

        case '6':  
         case '7':  
         case '8':  
         case '9':  
          value = (value << 4) + aChar - '0';  
          break;  
         case 'a':  
         case 'b':  
         case 'c':  
         case 'd':  
         case 'e':  
         case 'f':  
          value = (value << 4) + 10 + aChar - 'a';  
         break;  
         case 'A':  
         case 'B':  
         case 'C':  
         case 'D':  
         case 'E':  
         case 'F':  
          value = (value << 4) + 10 + aChar - 'A';  
          break;  
         default:  
          throw new IllegalArgumentException(  
            "Malformed   \\uxxxx   encoding.");  
         }  

       }  
        outBuffer.append((char) value);  
       } else {  
        if (aChar == 't')  
         aChar = '\t';  
        else if (aChar == 'r')  
         aChar = '\r';  

        else if (aChar == 'n')  

         aChar = '\n';  

        else if (aChar == 'f')  

         aChar = '\f';  

        outBuffer.append(aChar);  

       }  

      } else 

      outBuffer.append(aChar);  

     }  

     return outBuffer.toString();  

    }  
	
	
	public static boolean isMobileNO(String mobiles){  
		if(isBlank(mobiles)) {
			return true;
		}
		Pattern p1 = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"); 
		Matcher m1 = p1.matcher(mobiles); 
		
		return  m1.matches();  
		  
	}  
}
