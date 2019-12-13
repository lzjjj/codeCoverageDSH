package ci.monitor.server.service.basic;

import java.io.IOException;


import java.util.Calendar;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;

/**
 * @author JimmyHuang
 */
public abstract class BaseCacheService<T> implements IService<T> {

	private JSONObject cacheResult = null;
	private Date lastCaheDate = Calendar.getInstance().getTime();
	private boolean isUpdating = false;

	protected abstract int getCacheTime();

	@Override
	public JSONObject process(T param) throws IOException {
		if (cacheResult == null || getCacheTime() == -1) {
			if(!isUpdating) {
				isUpdating = true;
				cacheResult = business(param);
				lastCaheDate = Calendar.getInstance().getTime();	
				isUpdating = false;
			}
		} else if ((Calendar.getInstance().getTime().getTime() - lastCaheDate.getTime()) > getCacheTime()) {
			if (!isUpdating) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						JSONObject tmp = null;
						try {
							isUpdating = true;
							tmp = business(param);
							isUpdating = false;
							lastCaheDate = Calendar.getInstance().getTime();
						} catch (IOException e) {
							e.printStackTrace();
							isUpdating = false;
						}
						cacheResult = tmp == null ? cacheResult : tmp;
					}
				}).start();
			}
		}
		return cacheResult;

	}

}
