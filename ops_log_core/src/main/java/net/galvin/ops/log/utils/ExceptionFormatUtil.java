package net.galvin.ops.log.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 异常的格式化处理
 */
public final class ExceptionFormatUtil {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionFormatUtil.class);

	/**
	 * 将Exception的异常栈按简化输出
	 * 异常名 Caused by:(异常出处类名.方法名:行号<-..)
	 */
	public static String getTrace(Exception e) {
		if(e == null) {
			return "";
		}
		try {
			return format(e);
		} catch (Exception e1) {
			//如果格式化不出来，直接原文输出
			logger.error("格式化异常出错："+e.getMessage());
			logger.error("格式化异常出错：原文是：", e);
		}
		return "";
	}

	private static String format(Exception e) {
		StringBuilder exceptionResult = new StringBuilder(e.toString());
		getFormatString(e, exceptionResult);
		return exceptionResult.toString();
	}

	private static void getFormatString(Throwable e, StringBuilder exceptionResult) {
		StackTraceElement[] stList = e.getStackTrace();
		if(stList == null) {
			return ;
		}
		if(exceptionResult.length() > 0) {
			exceptionResult.append(" Caused by:(");
		}
		for(StackTraceElement ste : stList) {
			exceptionResult.append(ste.getFileName().replace(".java", "."));
			exceptionResult.append(ste.getMethodName());
			exceptionResult.append(":");
			exceptionResult.append(ste.getLineNumber());
			exceptionResult.append(" <- ");
		}
		if(exceptionResult.lastIndexOf(" <- ")==exceptionResult.length()-4) {
			exceptionResult.delete(exceptionResult.length()-4,exceptionResult.length());
		}
		exceptionResult.append(")");
		
		Throwable e1 = e.getCause();
		if(e1 != null){
			getFormatString(e1, exceptionResult);
		}
	}

}
