package dao;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 数据库操作基类
 */
@Component("BaseDao")
public class BaseDao {
    @Resource
    public SmartDao smartDao;

}