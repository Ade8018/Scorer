package gd.zh.gamer.scorer.verification;

import android.os.Handler;

public class VerificationImpl implements IVerification {
	private static VerificationImpl sInstance;

	private VerificationImpl() {
	}

	public synchronized static VerificationImpl getInstance() {
		if (sInstance == null) {
			sInstance = new VerificationImpl();
		}
		return sInstance;
	}

	@Override
	public void verify(String text, final VerificationResultListener listener) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (listener != null) {
					listener.onResult(true);
				}
			}
		}, 3000);
	}

}
