package com.tecsun.card.common;

import java.util.List;

/**
 * Jquery easyUI 返回实体类
 * @author po_tan
 *
 * @param <T>
 */
public class DataVo<T> {

	private long total;

	private List<T> rows;
	
	public DataVo(long total , List<T> rows){
		this.rows = rows;
		this.total = total;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

}
