package com.ninethree.palychannelbusiness.bean;

import java.io.Serializable;

public class AscPackResult implements Serializable {

	private static final long serialVersionUID = 8530721519813704159L;

	private byte[] resultBytes;
	private Object resultValue;

	public AscPackResult() {

	}

	public byte[] getResultBytes() {
		return resultBytes;
	}

	public void setResultBytes(byte[] resultBytes) {
		this.resultBytes = resultBytes;
	}

	public Object getResultValue() {
		return resultValue;
	}

	public void setResultValue(Object resultValue) {
		this.resultValue = resultValue;
	}

}
