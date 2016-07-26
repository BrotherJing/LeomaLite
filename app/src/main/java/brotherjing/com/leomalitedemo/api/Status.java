package brotherjing.com.leomalitedemo.api;

/**
 * 
 * @author wang_hong
 *
 */
public class Status {
	private int code;
	/**
	 * 
	 * @return code 
	 * 		   0-success
	 * 		   100-handler_not_available
	 * 		   101-fail
	 * 		   102-unexcepted_error
	 */
	public int getCode() {
		return code;
	}

	/**
	 * 
	 * @param code 
	 * 		  0-success
	 * 		  100-handler_not_available
	 * 		  101-fail
	 * 		  102-unexcepted_error
	 */
	public void setCode(int code) {
		this.code = code;
	}
	
	
}