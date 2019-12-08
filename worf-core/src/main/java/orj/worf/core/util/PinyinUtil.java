package orj.worf.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PinyinUtil {
	
	private static final Map<Character, String[]> cachedPinyinMap = new HashMap<Character, String[]>();
	private static final Properties unicodeProps = new Properties();

	public static void main(String[] args) throws IOException {
		String str = "茜绿旅,一个、游灭约穷晃好美";
		for (char ch : str.toCharArray()) {
			String[] pinyinArray = getPinyinArray(ch, true);
			if (pinyinArray != null) {
				System.out.println("------" + ch + "------");
				System.out.println("首字母：" + pinyinArray[0].charAt(0));
				System.out.println("首读音：" + pinyinArray[0]);
				System.out.println("全部读音" + Arrays.toString(pinyinArray));
				System.out.println("不带音调全部读音" + Arrays.toString(getPinyinArray(ch, false)));
			}
		}
	}
	
	public static String[] getPinyinArray(Character ch, boolean withTone) {
		if (cachedPinyinMap.get(ch) != null) {
			return format(cachedPinyinMap.get(ch), withTone);
		}
		
		String pinyin = unicodeProps.getProperty(Integer.toHexString(ch).toUpperCase());
		if (pinyin != null) {
			pinyin = pinyin.substring(1, pinyin.length() - 1);
			cachedPinyinMap.put(ch, pinyin.split(","));
			return format(cachedPinyinMap.get(ch), withTone);
		}
		return null;
	}
	
	private static String[] format(String[] pinyins, boolean withTone) {
		String[] formatedPinyin = new String[pinyins.length];
		int completeIndex = 0;
		if (withTone == false) {
			for (String pinyin : pinyins) {
				formatedPinyin[completeIndex++] = pinyin.substring(0, pinyin.length()-1);
			}
			return formatedPinyin;
		}
		
		char unmarkedVowel = '/';
	    int indexOfUnmarkedVowel = -1;
		final char charA = 'a';
		final char charE = 'e';
		final String ouStr = "ou";
		final String allUnmarkedVowelStr = "aeiouv";
		final String allMarkedVowelStr = "āáăàaēéĕèeīíĭìiōóŏòoūúŭùuǖǘǚǜü";
		
		
		for (String pinyin : pinyins) {
			if ((indexOfUnmarkedVowel = pinyin.indexOf(charA)) != -1
					|| (indexOfUnmarkedVowel = pinyin.indexOf(charE)) != -1
					|| (indexOfUnmarkedVowel = pinyin.indexOf(ouStr)) != -1) {
				unmarkedVowel = pinyin.charAt(indexOfUnmarkedVowel);
			} else {
				for (int i = pinyin.length()-1; i > 0; i--) {
					if (allUnmarkedVowelStr.indexOf(pinyin.charAt(i)) != -1) {
						indexOfUnmarkedVowel = i;
						unmarkedVowel = pinyin.charAt(indexOfUnmarkedVowel);
						break;
					}
				}
			}
			if (indexOfUnmarkedVowel != -1) {
				int index = allUnmarkedVowelStr.indexOf(unmarkedVowel);
				int toneNumber = Character.getNumericValue(pinyin.charAt(pinyin.length() - 1));
				char markedVowel = allMarkedVowelStr.charAt(index*5+toneNumber-1);
				StringBuffer resultBuffer = new StringBuffer();
				resultBuffer.append(pinyin.substring(0, indexOfUnmarkedVowel).replaceAll("v", "ü"));
				resultBuffer.append(markedVowel);
				resultBuffer.append(pinyin.substring(indexOfUnmarkedVowel + 1, pinyin.length() - 1).replaceAll("v", "ü"));
				formatedPinyin[completeIndex++] = resultBuffer.toString();
			} else {
				formatedPinyin[completeIndex++] = pinyins[completeIndex];
			}
		}
		return formatedPinyin;
	}
	
	static {
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void init() throws IOException {
		unicodeProps.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("pinyindb/unicode_to_pinyin.txt"));
	}
	
	
	
	@SuppressWarnings("unused")
	private static void generateUTF8FileFromUnicode() throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("pinyindb/unicode_to_pinyin.txt");
		InputStreamReader reader = new InputStreamReader(is);
		BufferedReader bf = new BufferedReader(reader);
		String line;
		Pattern pattern = Pattern.compile("(\\S+) (\\S+)");
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/resources/pinyindb/utf8_to_pinyin.txt")));
		while ( (line = bf.readLine()) != null) {
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				String utf8 = UnicodeToUTF8(matcher.group(1));
				writer.write(utf8 + " " + matcher.group(2) + "\n");
				System.out.println(matcher.group(1) + ":" + matcher.group(2));
			}
		}
		bf.close();
		writer.close();
	}
	
	private static String UnicodeToUTF8(String hex) throws UnsupportedEncodingException {
		int i = Integer.parseInt(hex, 16);
		char ch = (char)i;
		byte[] bytes = new String(ch+"").getBytes("utf8");
		StringBuffer sb = new StringBuffer();
		for (byte b : bytes) {
			sb.append(Integer.toHexString(b&0xff).toUpperCase());
		}
		return sb.toString();
	}
}
