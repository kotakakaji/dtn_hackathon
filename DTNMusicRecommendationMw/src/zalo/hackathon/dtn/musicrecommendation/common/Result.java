/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zalo.hackathon.dtn.musicrecommendation.common;

import org.json.simple.JSONAware;

/**
 *
 * @author Kiss
 */
public class Result {
	private final ErrorCode errCode;
	private final JSONAware data;

	public static final Result RESULT_SUCCESS = new Result(ErrorCode.SUCCESS);
	public static final Result RESULT_ACCESS_DENIED = new Result(ErrorCode.ACCESS_DENIED);
	public static final Result RESULT_INVALID_PARAM = new Result(ErrorCode.INVALID_PARAMETER);
	public static final Result RESULT_DATABASE_ERROR = new Result(ErrorCode.DATABASE_ERROR);
	public static final Result RESULT_NOT_EXIST = new Result(ErrorCode.NOT_EXIST);
	
	public Result(ErrorCode errCode, JSONAware data) {
		this.errCode = errCode;
		this.data = data;
	}

	public Result(ErrorCode errorCode) throws RuntimeException{
		if (errorCode == null) {
			throw new RuntimeException("null");
		}
		this.errCode = errorCode;
		this.data = null;
	}

	public JSONAware getData() {
		return data;
	}

	public ErrorCode getErrorCode() {
		return errCode;
	}
}
