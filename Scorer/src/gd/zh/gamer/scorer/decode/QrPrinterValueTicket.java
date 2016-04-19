package gd.zh.gamer.scorer.decode;

/**
 * Created by Administrator on 2016/4/10.
 */
public class QrPrinterValueTicket extends QrPrinterBase {

	private int mValue;
	private int mAddtionValueN;
	private int mRatioX;
	private String mPin;

	public QrPrinterValueTicket(String sn, String time, int value, int n,
			int x, String pin) {
		super(sn, time);
		this.mValue = value;
		this.mAddtionValueN = n;
		this.mRatioX = x;
		this.mPin = pin;
	}

	public int getValue() {
		return this.mValue;
	}

	public int getAddtionValueN() {
		return this.mAddtionValueN;
	}

	public int getRatioX() {
		return this.mRatioX;
	}

	public String getPin() {
		return this.mPin;
	}

	@Override
	public String toString() {
		return "QrPrinterValueTicket [mValue=" + mValue + ", mAddtionValueN="
				+ mAddtionValueN + ", mRatioX=" + mRatioX + ", mPin=" + mPin
				+ "]";
	}

}
