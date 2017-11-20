package com.lvmama.log.utils;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * 使用Hession序列化
 */
public class BeanHessionSerializeUtil {

	protected static final Log LOG = LogFactory.getLog(BeanHessionSerializeUtil.class);

	private BeanHessionSerializeUtil() {
	}

	public static byte[] serialize(Object obj) {
		if (obj == null)
			return null;

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Hessian2Output ho = new Hessian2Output(os);
		try {
			ho.writeObject(obj);
			ho.flush();
			return os.toByteArray();
		} catch (IOException e) {
			LOG.error(ExceptionFormatUtil.getTrace(e));
		} finally {
			try {
				ho.close();
			} catch (IOException e) {
				LOG.error(ExceptionFormatUtil.getTrace(e));
			}
		}
		return null;
	}

	public static Object deserialize(byte[] by) {
		if (by == null)
			return null;

		ByteArrayInputStream is = new ByteArrayInputStream(by);
		Hessian2Input hi = new Hessian2Input(is);
		try {
			return hi.readObject();
		} catch (IOException e) {
			LOG.error(ExceptionFormatUtil.getTrace(e));
		} finally {
			try {
				hi.close();
			} catch (IOException e) {
				LOG.error(ExceptionFormatUtil.getTrace(e));
			}
		}
		return null;
	}

}
