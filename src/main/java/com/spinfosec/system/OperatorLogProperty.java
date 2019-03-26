package com.spinfosec.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ank
 * @version v 1.0
 * @title [操作日志配置文件]
 * @ClassName: com.spinfosec.system.OperatorLogDefinitionProperty
 * @description [操作日志配置文件]
 * @create 2018/11/23 10:28
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
@Component
public class OperatorLogProperty
{
    @Value("${operatorLogMap}")
    private String operatorLogMap;

    public String getOperatorLogMap()
    {
        return operatorLogMap;
    }

    public void setOperatorLogMap(String operatorLogMap)
    {
        this.operatorLogMap = operatorLogMap;
    }
}
