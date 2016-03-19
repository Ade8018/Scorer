package gd.zh.gamer.scorer.verification;

import gd.zh.gamer.scorer.base.BaseInterface;

public interface IVerification extends BaseInterface {
	void verify(String text, VerificationResultListener listener);
}
