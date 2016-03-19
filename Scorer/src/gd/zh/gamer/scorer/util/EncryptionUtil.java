package gd.zh.gamer.scorer.util;

public class EncryptionUtil {
	String ori = "abc00{\"account\": \"account\",\"nickname\": \"nickname\",\"password\": \"pwd\"}110";
	String pwd = getEncryptedString(ori);

	private static String getEncryptedString(String ori) {
		String content = ori.substring(0, ori.length() - 3);
		String numStr = ori.substring(ori.length() - 3, ori.length());
		byte num = Byte.valueOf(numStr);
		byte contentBs[] = content.getBytes();
		for (int i = 0; i < contentBs.length; i++) {
			contentBs[i] ^= num;
		}
		String encryptedContent = new String(contentBs);
		byte[] bs = numStr.getBytes();
		for (int i = 0; i < bs.length; i++) {
			bs[i] += 100;
		}
		String encryptedNum = new String(bs);
		return encryptedContent + encryptedNum;
	}
}
