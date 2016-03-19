package gd.zh.gamer.scorer.base;

import gd.zh.gamer.scorer.decode.IDecodeImpl;
import gd.zh.gamer.scorer.verification.VerificationImpl;

public class InterfaceFactory {
//	public static final int INTERFACE_TYPE_VERIFICATION = 0;
	public static final int INTERFACE_TYPE_DECODE = 1;

	public static BaseInterface getInterface(int type) {
		switch (type) {
//		case INTERFACE_TYPE_VERIFICATION:
//			return VerificationImpl.getInstance();
		case INTERFACE_TYPE_DECODE:
			return IDecodeImpl.getInstance();

		default:
			return null;
		}
	}
}
