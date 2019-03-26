package com.spinfosec.system;

/**
 * @title [框架自定义异常类]
 * @description [一句话描述]
 * @copyright Copyright 2013 SHIPING INFO Corporation. All rights reserved.
 * @company SHIPING INFO.
 * @author Caspar Du
 * @version v 1.0
 * @create 2013-6-7 下午10:30:10
 */
public class TMCException extends RuntimeException
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 5835820833254771018L;

    private RspCode errCode;

    /**
     * @param code
     */
    public TMCException(RspCode code)
    {
        super(code.getDescription());
        this.errCode = code;
    }

    /**
     * @param err
     * @param cause
     */
    public TMCException(RspCode err, Throwable cause)
    {
        super(err.getDescription(),cause);
        this.errCode = err;
    }

    public RspCode getErrCode()
    {
        return errCode;
    }
}
