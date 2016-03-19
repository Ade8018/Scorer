package gd.zh.gamer.scorer.decode;

import android.os.Handler;

public class IDecodeImpl implements IDecode {
	private static IDecodeImpl sInstance;

	private IDecodeImpl() {
	}

	public synchronized static IDecodeImpl getInstance() {
		if (sInstance == null) {
			sInstance = new IDecodeImpl();
		}
		return sInstance;
	}

	@Override
	public void decode(String text, final DecodeResultListener listener) {
		MainDecodeStr mainDecode = new MainDecodeStr();
		String result = mainDecode.decodeStr(text);
		DecodeResult dr = new DecodeResult();
		if ("校验错误，二维码无效".equals(result)) {
			dr.result = false;
		} else {
			dr.result = true;
		}
		dr.text = result;
		listener.onResult(dr);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
		}
	};

}
