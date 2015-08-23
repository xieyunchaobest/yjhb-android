/**
 * All rights, including trade secret rights, reserved.
 */

package cn.com.xyc.util;

import java.util.List;

public class Result<T> {
	public int resultCode = 1; // s
	public T result;//fail
}

class ListResult<T> {
	public int resultCode = 1; // 
	public List<T> resultList;// 
}

class PageResult<T> extends ListResult<T> {
	public long totalCount = 0; 
}

