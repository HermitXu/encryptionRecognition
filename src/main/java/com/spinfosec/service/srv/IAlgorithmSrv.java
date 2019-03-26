package com.spinfosec.service.srv;

import com.spinfosec.dao.entity.SpEncryptionAlgorithm;

import java.util.List;

/**
 * @author Administrator
 * @version v 1.0
 * @title [算法接口]
 * @ClassName: com.spinfosec.service.srv.IAlgorithmSrv
 * @description [算法接口]
 * @create 2018/11/13 12:02
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public interface IAlgorithmSrv
{
    /**
     * 根据id查询算法信息
     * @param id
     * @return
     */
    SpEncryptionAlgorithm getAlgorithm(String id);

    /**
     * 根据算法名称查询算法信息
     * @param algorithm
     * @return
     */
    SpEncryptionAlgorithm getAlgorithmByName(String algorithm);
}
