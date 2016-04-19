package gd.zh.gamer.scorer.decode;

/**
 * Created by Administrator on 2016/4/10.
 */
public class QrPrinterBase {
	private String sn;
	private String time;

	public QrPrinterBase(String sn, String time) {
		this.sn = sn;
		this.time = time;
	}

	public QrPrinterBase() {
		this.sn = null;
		this.time = null;
	}

	public void setQrPrinterSn(String sn) {
		this.sn = sn;
	}

	public void setQrPrinterTime(String time) {
		this.time = time;
	}

	public String getQrPrinterSn() {
		return this.sn;
	}

	public String getQrPrinterTime() {
		return this.time;
	}

	@Override
	public String toString() {
		return "QrPrinterBase [sn=" + sn + ", time=" + time + "]";
	}
}
