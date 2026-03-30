package com.manage.service.impl;

import com.manage.mapper.VisitorMapper;
import com.manage.pojo.Visitor;
import com.manage.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitorServiceImpl implements VisitorService {

    @Autowired
    private VisitorMapper visitorMapper;

    @Override
    public int getVisitorCount() {
        Visitor visitor = visitorMapper.selectVisitor();
        if (visitor == null) {
            visitorMapper.insertVisitor(1);
            return 1;
        }
        return visitor.getCount();
    }

    @Override
    public int incrementVisitorCount() {
        Visitor visitor = visitorMapper.selectVisitor();
        if (visitor == null) {
            visitorMapper.insertVisitor(1);
            return 1;
        }
        int newCount = visitor.getCount() + 1;
        visitorMapper.updateVisitorCount(newCount);
        return newCount;
    }
}
