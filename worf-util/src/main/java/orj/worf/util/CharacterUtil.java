package orj.worf.util;

import java.io.UnsupportedEncodingException;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.lang3.StringUtils;

/**
 * 汉子转拼音
 * @author LiuZhenghua
 * 2019年9月10日 下午2:38:25
 */
public class CharacterUtil {

	
	public static void main(String[] args) throws Exception {
//		byte[] b = "中在这".getBytes(Charset.forName("utf8"));
		byte[] b = "一在这a".getBytes("unicode");
		System.out.println(StringUtils.join(b, ','));
		
		b="abc".getBytes("GBK");
		System.out.println(StringUtils.join(b, ','));
		System.out.println(getFirstLetters("中在这", HanyuPinyinCaseType.UPPERCASE));
		System.out.println(new String(new byte[]{0x75}));
		
		for (int i = 0x4e00; i <= 0x9fa5; i++) {
			System.out.println(Integer.toHexString(i) + ":" + new String(new byte[]{(byte)((i>>8) & 0xff), (byte)(i&0xff)}, "unicode"));
			if (i == 0x4e37) {
				break;
			}
		}
	}
	//01001011 00000000
	
	
	
	public static String getFirstLetters(String ChineseLanguage,HanyuPinyinCaseType caseType) {
	    char[] cl_chars = ChineseLanguage.trim().toCharArray();
	    String hanyupinyin = "";
	    HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
	    defaultFormat.setCaseType(caseType);// 输出拼音全部大写
	    defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
	    try {
	        for (int i = 0; i < cl_chars.length; i++) {
	            String str = String.valueOf(cl_chars[i]);
	            if (str.matches("[\u4e00-\u9fa5]+")) {// 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
	                hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0].substring(0, 1);
	            } else if (str.matches("[0-9]+")) {// 如果字符是数字,取数字
	                hanyupinyin += cl_chars[i];
	            } else if (str.matches("[a-zA-Z]+")) {// 如果字符是字母,取字母
	                hanyupinyin += cl_chars[i];
	            } else {// 否则不转换
	                hanyupinyin += cl_chars[i];//如果是标点符号的话，带着
	            }
	        }
	    } catch (BadHanyuPinyinOutputFormatCombination e) {
	        System.out.println("汉语不能转拼音");
	    }
	    return hanyupinyin;
	}

}


