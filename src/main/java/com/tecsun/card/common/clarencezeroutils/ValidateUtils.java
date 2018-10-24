package com.tecsun.card.common.clarencezeroutils;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一、字符串校验(英文、数字、整数、正整数)
 * 二、电话号码校验(座机、手机号码)
 * 三、Email校验
 * 四、域名校验(URL、IP)
 * 五、编号校验
 * 六、非法字符校验(%、\\、'、*)
 * 七、密码校验(8-20位,支持数字、大小写字母、标点符号、不允许有空格)
 * 八、密码强度校验(>8位、至少一个大写、一个小写、1个特殊字符)
 * 九、中文姓名校验(2-4个中文字符)
 * 十、字符串空格替换
 * 十一、日期格式检验(yyyyMMdd、yyyy-MM-dd的)
 */
public class ValidateUtils {

	private static final Pattern PCHARANDDIGIT =
			Pattern.compile("[0-9a-zA-Z]+");

	private static final Pattern PDATEY_M_D =
			Pattern.compile("\\d{4}\\-\\d{2}\\-\\d{2}");

	private static final Pattern PIP_4 =
			Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");// ip地址匹配192.168.1.3

	private static final Pattern PDOMAIN =
			Pattern.compile("[a-zA-Z0-9-\\u4e00-\\u9fa5]+(.[a-zA-Z0-9-]+)+");

	private static final Pattern PINTEGER =
			Pattern.compile("[+-]?\\d+");

	private static final Pattern PURL =
			Pattern.compile("(http|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?");

	private static final Pattern PSERIALNO =
			Pattern.compile("[a-z0-9-_]+");

	private static final Pattern PDATEYMD =
			Pattern.compile("\\d{8}");

	// (?=...):表示零宽度正向正断言,断言后面一定要出现的内容。
	//
	private static final Pattern PASSWORD =
			Pattern.compile("^(?!([a-z]*|[A-Z]*|[0-9]*|[\\p{Punct}]*|[a-zA-Z]*|[a-z0-9]*|[a-z\\p{Punct}]*|[A-Z0-9]*|[A-Z\\p{Punct}]*|[0-9\\p{Punct}]*)$)[a-zA-Z0-9\\p{Punct}]{8,20}$");

	private static final String DOMAINS =
			".com.cn|.net.cn|.org.cn|.gov.cn|.com|.net|.tv|.gd|.org|.cc|.vc|.mobi|.cd|.info|.name|.asia|.hk|.me|" +
					".la|.sh|.biz|.li|.kr|.in|.us|.io|.ac.cn|.bj.cn|.sh.cn|.tj.cn|.cq.cn|.he.cn|.sx.cn|.nm.cn|.ln.cn|" +
					".jl.cn|.hl.cn|.js.cn|.zj.cn|.ah.cn|.fj.cn|.jx.cn|.sd.cn|.ha.cn|.hb.cn|.hn.cn|.gd.cn|.gx.cn|.hi.cn|" +
					".sc.cn|.gz.cn|.yn.cn|.xz.cn|.sn.cn|.gs.cn|.qh.cn|.nx.cn|.xj.cn|.tw.cn|.hk.cn|.mo.cn|.cn";

	private ValidateUtils() {
		// 私有类构造方法
	}

	/**
	 * 字符串校验: 全英校验,不区分大小写
	 * 
	 * @author 
	 * @return
	 */
	public static boolean isEnglish(String value) {
		if (isEmptyString(value)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		Matcher m = pattern.matcher(value);
		return m.matches();
	}

	/**
	 * 校验数字,包括小数和负数
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNumber(String value) {
		if (isEmptyString(value)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^-?[0-9]*.?[0-9]+$");
		Matcher m = pattern.matcher(value);
		return m.matches();
	}

	/**
	 * 是否整数,可以带正负号
	 *
	 * @param value 目标字符串
	 * @return 是否整数
	 */
	public static boolean isInteger(String value) {
		if (isEmptyString(value)) {
			return false;
		}
		Matcher m = PINTEGER.matcher(value);
		return m.matches();
	}

	/**
	 * 校验正整数,0-9
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isPoInteger(String value) {
		if (isEmptyString(value)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[0-9]+$");
		Matcher m = pattern.matcher(value);
		return m.matches();
	}

	/**
	 * 校验电话号码（座机）
	 * 
	 * @author 
	 * @return
	 */
	public static boolean isPhone(String value) {
		if (isEmptyString(value)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^((\\d{3,4})\\-(\\d{8})|(\\d{4})\\-(\\d{7}))$");
		Matcher m = pattern.matcher(value);
		return m.matches();

	}

	/**
	 * 校验电话号码（手机）
	 * 
	 * @author 
	 * @return
	 */
	public static boolean isMobile(String value) {
		if (isEmptyString(value)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^1(4[0-9]|3[0-9]|5[0-9]|8[0-9]|7[0-9])\\d{8}$");
		Matcher m = pattern.matcher(value);
		return m.matches();
	}

	/**
	 * 运营商号段如下： 中国联通号码：130、131、132、145（无线上网卡）、155、156、185（iPhone5上市后开放）、186、176（4G号段）、 175（2015年9月10日正式启用，暂只对北京、上海和广东投放办理）
	 * 中国移动号码：134、135、136、137、138、139、147（无线上网卡）、150、151、152、157、158、159、182、183、187、188、178 中国电信号码：133、153、180、181、189、177、173、149 虚拟运营商：170、1718、1719 手机号前3位的数字包括： 1 :1 2 :3,4,5,7,8 3
	 * :0,1,2,3,4,5,6,7,8,9 总结： 目前java手机号码正则表达式有： a :"^1[3|4|5|7|8][0-9]\\d{4,8}$" 一般验证情况下这个就可以了 b :"^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$"
	 * Pattern和Matcher详解（字符串匹配和字节码）http://blog.csdn.net/u010700335/article/details/44616451
	 */
	public static boolean isMobileNo(String mobileNo) {
		if (isEmptyString(mobileNo)) {
			return false;
		}
		String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
		boolean isMatch = false;//默认不合格
		try {
			if (mobileNo.length() != 11) {
				isMatch = false;
			} else {
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(mobileNo);
				isMatch = m.matches();
			}
		} catch (Exception e) {
			isMatch = false;
			e.printStackTrace();
		}
		return isMatch;
	}

	/**
	 * 校验电子邮箱
	 * 
	 * @author 
	 * @return
	 */
	public static boolean isEmail(String value) {
		if (isEmptyString(value)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$");
		Matcher m = pattern.matcher(value);
		return m.matches();
	}

	/**
	 * 是否是url地址
	 *
	 * @param str http://www.oppo.com url地址
	 * @return 是否是url地址
	 */
	public static boolean checkURL(String str) {
		if (isEmptyString(str)) {
			return false;
		}
		Matcher m = PURL.matcher(str);
		return m.matches();
	}

	/**
	 * 是否是主域名如 baidu.com
	 *
	 * @param str 目标字符串
	 * @return 是否是主域名
	 */
	public static boolean checkMainDomain(String str) {
		if (isEmptyString(str)) {
			return false;
		}
		String[] domains = DOMAINS.split("\\|");
		for (String s : domains) {
			if (null != s && !s.trim().equals("") && str.indexOf(s) > 0) {
				str = str.replace(s, "");
				if (checkSerialNo(str)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	public static boolean checkDomain(String s) {
		if (isEmptyString(s)) {
			return false;
		}
		Matcher m = PDOMAIN.matcher(s);
		return m.matches();
	}

	public static boolean checkNotDomain(String s) {
		return !checkDomain(s);
	}

	/**
	 * 是不是IP地址
	 *
	 * @param s 目标字符串
	 * @return 是不是IP地址
	 */
	public static boolean checkIP(String s) {
		if (isEmptyString(s)) {
			return false;
		}
		Matcher m = PIP_4.matcher(s);
		return m.matches();
	}

	/**
	 * 校验编号, 只能是数字,字母, 下划线, 减号
	 *
	 * @param str 目标字符串 例如：http://www.oppo.com
	 * @return 是否是编号
	 */
	public static boolean checkSerialNo(String str) {
		if (isEmptyString(str)) {
			return false;
		}
		str = str.toLowerCase().trim();

		Matcher isSerialNo = PSERIALNO.matcher(str);
		return isSerialNo.matches();
	}

	/**
	 * 是否含有非法字符
	 *
	 * @param str 目标字符串
	 * @return 是否含有非法字符
	 */
	public static boolean checkForbid(String str) {
		if (isEmptyString(str)) {
			return false;
		}
		str = str.toLowerCase().trim();
		String[] forbids = { "%", "\\", "'", "*" };
		for (String s : forbids) {
			if (str.indexOf(s) > -1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 校验密码
	 * 
	 * @author 
	 * @return
	 */
	public static boolean checkPassWord(String value) {
		if (isEmptyString(value)) {
			return false;
		}
		Matcher m = PASSWORD.matcher(value);
		return m.matches();
	}

	/**
	 * 密码强度校验:最少8位，包括至少1个大写字母，1个小写字母，1个特殊字符
	 *
	 * @param value
	 * @return
	 */
	public static boolean checkStrongPassword(String value) {
		if (isEmptyString(value)) {
			return false;
		}

		Pattern pattern = Pattern.compile("(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");
		Matcher m = pattern.matcher(value);
		return m.matches();
	}

	/**
	 * 验证用户名
	 * 
	 * @param value
	 *            用户名
	 * @return 布尔
	 */
	public static boolean checkUserName(String value) {
		if (isEmptyString(value)) {
			return false;
		}
		// 用户名正则，4到16位（字母，数字，下划线，减号)
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9_-]{6,12}$");
		Matcher m = pattern.matcher(value);
		return m.matches();
	}

	/**
	 * 验证姓名,只能校验汉字,长度为2至4位字符
	 * 
	 * @param value
	 *            姓名
	 * @return 布尔
	 */
	public static boolean checkChinectName(String value) {
		if (isEmptyString(value)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]{2,4}$");
		Matcher m = pattern.matcher(value);
		return m.matches();
	}

	/**
	 * 获取字符长度
	 * 
	 * @param value
	 * @return 长度
	 */
	public static int getLength(String value) {
		return value.length();
	}

	/**
	 * 判断字符串是否为空或者空字符串。
	 * 
	 * @author weiguobin
	 * @param value
	 * @return 如果为空或者空字符串，返回true；否则返回false。
	 */
	public static boolean isEmptyString(String value) {
		return (value == null || "".equals(value.trim())) ? true : false;
	}

	/**
	 * 字符串不为空
	 * @param s
	 * @return
	 */
	public static boolean isNotEmptyString(String s) {
		return !isEmptyString(s);
	}

	/**
	 * 判断字符串是不是都为空
	 *
	 * @param str
	 * @return 只要有不空的字符串就返回false
	 */
	public static boolean isAllEmptyString(String... str) {
		if (str == null) {
			return true;
		}
		for (String s : str) {
			if (isNotEmptyString(s)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是不是所有字符串都不为空
	 *
	 * @param str 字符串
	 * @return 只要有为空的就返回false
	 */
	public static boolean isAllNotEmptyString(String... str) {
		if (str == null) {
			return false;
		}
		for (String s : str) {
			if (isEmptyString(s)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 移除空行、空格
	 *
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		if (StringUtils.isBlank(str)) {
			return str;
		}
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(str);
		return m.replaceAll("");
	}

	/**
	 * 通用长度为1-50验证
	 * 
	 * @param value
	 *            校验值
	 * @return 布尔
	 */
	public static Boolean checkCommon(String value) {
		if (isEmptyString(value)) {
			return false;
		}
		Pattern pattern = Pattern.compile("\\S{1,50}");
		Matcher m = pattern.matcher(value);
		return m.matches();
	}

	/**
	 * 验证地址
	 * 
	 * @param value
	 *            校验值
	 * @return 布尔
	 */
	public static Boolean checkAddress(String value) {
		if (isEmptyString(value)) {
			return false;
		}
		Pattern pattern = Pattern.compile("\\S{1,100}");
		Matcher m = pattern.matcher(value);
		return m.matches();
	}

	/**
	 * 通用长度为1-255验证
	 * 
	 * @param value
	 *            校验值
	 * @return 布尔
	 */
	public static Boolean checkConstant(String value) {
		if (isEmptyString(value)) {
			return false;
		}
		Pattern pattern = Pattern.compile("\\S{1,255}");
		Matcher m = pattern.matcher(value);
		return m.matches();
	}

	/**
	 * 判断某个字符串长度是不是在一个范围内（包含边界值）。
	 *
	 * @param str 字符串
	 * @param minLength 最小长度
	 * @param maxLength 最大长度
	 * @return
	 */
	public static boolean checkStringBetween(String str, int minLength, int maxLength) {
		if (str == null) {
			return false;
		}
		if (str.length() >= minLength && str.length() <= maxLength) {
			return true;
		}
		return false;
	}

	/**
	 * 验证经度
	 * 
	 * @param value
	 *            校验值
	 * @return 布尔
	 */
	public static boolean checkLongitude(String value) {
		if (isEmptyString(value)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[\\-\\+]?(0?\\d{1,2}\\.\\d{1,5}|1[0-7]?\\d{1}\\.\\d{1,12}|180\\.0{1,12})$");
		Matcher m = pattern.matcher(value);
		return m.matches();
	}

	/**
	 * 验证纬度
	 * 
	 * @param value
	 *            校验值
	 * @return 布尔
	 */
	public static boolean checkLatitude(String value) {
		if (isEmptyString(value)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[\\-\\+]?([0-8]?\\d{1}\\.\\d{1,12}|90\\.0{1,12})$");
		Matcher m = pattern.matcher(value);
		return m.matches();
	}

	/**
	 * 判断日期字符串是否是yyyyMMdd的格式
	 *
	 * @param s 日期字符串
	 * @return 格式是否符合
	 */
	public static boolean checkDateYMD(String s) {
		if (isEmptyString(s)) {
			return false;
		}

		Matcher m = PDATEYMD.matcher(s);
		return m.matches();
	}

	/**
	 * 判断日期字符串是否是yyyy-MM-dd的格式
	 *
	 * @param s 日期字符串
	 * @return 格式是否符合
	 */
	public static boolean checkDateY_M_D(String s) {
		if (isEmptyString(s)) {
			return false;
		}
		Matcher m = PDATEY_M_D.matcher(s);
		return m.matches();
	}

	/**
	 * 校验 0-9a-zA-Z
	 * @param s
	 * @return
	 */
	public static boolean checkCharAndDigit(String s) {
		if (isEmptyString(s)) {
			return false;
		}
		Matcher m = PCHARANDDIGIT.matcher(s);
		return m.matches();
	}




	public static void main(String[] args) {
		String s = "姜家_";
		System.out.println(checkChinectName(s));
	}

	/**
	 * 校验公民身份证号码
	 * @param idCard
	 * @return
	 */
	public static boolean isIdCard(String idCard) {
		if (idCard.length() == 15 || idCard.length() == 18) {
			if (!cardCodeVerifySimple(idCard)) {
			    return false;
			} else {
				if (idCard.length() == 18 && !cardCodeVerify(idCard)) {
				    return false;
				}
			}
		} else {
		    return false;
		}
		return true;
	}


	/**
	 * 正则校验身份证是否符合第一代第二代标准
	 * @param cardcode
	 * @return
	 */
	public static boolean cardCodeVerifySimple(String cardcode) {
		//第一代身份证正则表达式(15位)
		String isIDCard1 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
		//第二代身份证正则表达式(18位)
		String isIDCard2 ="^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-Z])$";

		//验证身份证
		if (cardcode.matches(isIDCard1) || cardcode.matches(isIDCard2)) {
			return true;
		}
		return false;
	}

	public static boolean cardCodeVerify(String cardcode) {
		int i = 0;
		String r = "error";
		String lastnumber = "";

		i += Integer.parseInt(cardcode.substring(0, 1)) * 7;
		i += Integer.parseInt(cardcode.substring(1, 2)) * 9;
		i += Integer.parseInt(cardcode.substring(2, 3)) * 10;
		i += Integer.parseInt(cardcode.substring(3, 4)) * 5;
		i += Integer.parseInt(cardcode.substring(4, 5)) * 8;
		i += Integer.parseInt(cardcode.substring(5, 6)) * 4;
		i += Integer.parseInt(cardcode.substring(6, 7)) * 2;
		i += Integer.parseInt(cardcode.substring(7, 8)) * 1;
		i += Integer.parseInt(cardcode.substring(8, 9)) * 6;
		i += Integer.parseInt(cardcode.substring(9, 10)) * 3;
		i += Integer.parseInt(cardcode.substring(10,11)) * 7;
		i += Integer.parseInt(cardcode.substring(11,12)) * 9;
		i += Integer.parseInt(cardcode.substring(12,13)) * 10;
		i += Integer.parseInt(cardcode.substring(13,14)) * 5;
		i += Integer.parseInt(cardcode.substring(14,15)) * 8;
		i += Integer.parseInt(cardcode.substring(15,16)) * 4;
		i += Integer.parseInt(cardcode.substring(16,17)) * 2;
		i = i % 11;
		lastnumber =cardcode.substring(17,18);
		if (i == 0) {
			r = "1";
		}
		if (i == 1) {
			r = "0";
		}
		if (i == 2) {
			r = "x";
		}
		if (i == 3) {
			r = "9";
		}
		if (i == 4) {
			r = "8";
		}
		if (i == 5) {
			r = "7";
		}
		if (i == 6) {
			r = "6";
		}
		if (i == 7) {
			r = "5";
		}
		if (i == 8) {
			r = "4";
		}
		if (i == 9) {
			r = "3";
		}
		if (i == 10) {
			r = "2";
		}
		if (r.equals(lastnumber.toLowerCase())) {
			return true;
		}
		return false;
	}


}
