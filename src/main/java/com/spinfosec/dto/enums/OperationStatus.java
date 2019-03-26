package com.spinfosec.dto.enums;

public enum OperationStatus
{
	PENDING, COMPLETED, COMPLETED_WITH_ERRORS, NOT_CONNECTED, FAILED,

    /**
     * 启动中
     */
    STARTING, EXPORTSUCCESS,

    /**
     * 停止
     */
    PAUSED;
}
