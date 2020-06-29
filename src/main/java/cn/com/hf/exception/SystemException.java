/**
 * llin 2019年4月13日下午12:35:08
 */
package cn.com.hf.exception;

/**
 * @author llin
 *
 */
public class SystemException extends Exception {

	private static final long serialVersionUID = -1L;

	private String msg;

	public SystemException(Throwable e) {
		if (e instanceof SystemException) {
			this.msg = ((SystemException) e).getMsg();
		} else {
			this.msg = e.toString();
		}
		initCause(e);
		init();
	}

	public SystemException(String msg) {
		setMsg(msg);
		this.msg = msg;
		init();
	}

	public SystemException(String msg, Throwable cause) {
		super(cause);
		this.msg = cause.getMessage();
		setMsg(msg);
		init();
	}

	public SystemException() {
		init();
	}

	protected void init() {
	}

	public void printStackTrace() {
		if (getCause() != null)
			getCause().printStackTrace();
		else
			super.printStackTrace();
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
