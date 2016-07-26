package brotherjing.com.leomalitedemo.api;

public class CorpApi {
	public static final int ID_NONET_IMAGEVIEW = 1000;
	public static final int ID_NONET_TEXT_REST = 1001;
	public static final int ID_NONET_BTN_REST = 1002;

	public static final int NAVI_TYPE_PUSH = 1;
	public static final int NAVI_TYPE_MODAL = 2;
	public static final int NAVI_TYPE_TAB = 3;
	public static final int NAVI_TYPE_ROOT = 100;

	public static final int RESULT_COMMON = 1000;

	public static enum ResponseStatusCode {
		Success(0), Fail(101), Error(102);
		int val;

		private ResponseStatusCode(int val) {
			this.val = val;
		}

		public int value() {
			return val;

		}
	}

	public static enum NavigationType {
		none(0), modal(1), push(2), tab(3), modalBack(4), pushBack(5), silentModal(11), silentPush(12), silentModalBack(
				14), silentPushBack(15);
		int action;
		boolean animated;

		NavigationType(int val) {
			this.action = val - val / 10 * 10;
			this.animated = val < 10;
		}

		public NavigationType action() {
			return valueOf(this.action);
		}

		public int actionInt() {
			return this.action;
		}

		public boolean isAnimated() {
			return this.animated;
		}

		public static NavigationType valueOf(int val) {
			int isSilent = val / 10;
			int type = val - isSilent * 10;
			switch (type) {
			case 0:
				return none;
			case 1:
				if (isSilent > 0)
					return silentModal;
				return modal;
			case 2:
				if (isSilent > 0)
					return silentPush;
				return push;
			case 3:
				return tab;
			case 4:
				if (isSilent > 0)
					return silentModalBack;
				return modalBack;
			case 5:
				if (isSilent > 0)
					return silentPushBack;
				return pushBack;
			default:
				return push;
			}
		}
	}

}
