package com.manage.mapper;

import com.manage.pojo.Visitor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VisitorMapper {
    Visitor selectVisitor();
    
    int updateVisitorCount(@Param("count") Integer count);
    
    int insertVisitor(@Param("count") Integer count);
}
