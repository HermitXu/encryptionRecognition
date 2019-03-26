package com.spinfosec.dao;

import com.spinfosec.dao.entity.SpEncryptionAlgorithm;

/**
 * @author ank
 * @version v 1.0
 * @title [算法dao接口]
 * @ClassName: com.spinfosec.dao.AlgorithmDao
 * @description [算法dao接口]
 * @create 2018/11/13 12:04
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public interface AlgorithmDao
{
    /**
     * 通过策略id来获取算法类型
     * @param id
     * @return
     */
    SpEncryptionAlgorithm getAlgorithm(String id);

    /**
     * 根据算法名称获取算法信息
     * @param algorithm
     * @return
     */
    SpEncryptionAlgorithm getAlgorithmByName(String algorithm);
}
