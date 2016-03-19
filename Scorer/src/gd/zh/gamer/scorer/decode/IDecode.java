package gd.zh.gamer.scorer.decode;

import gd.zh.gamer.scorer.base.BaseInterface;

public interface IDecode extends BaseInterface {
	void decode(String text, DecodeResultListener listener);
}
