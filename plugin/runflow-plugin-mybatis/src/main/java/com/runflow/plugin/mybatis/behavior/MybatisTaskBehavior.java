package com.runflow.plugin.mybatis.behavior;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.behavior.TaskActivityBehavior;
import com.runflow.engine.behavior.UserTaskActivityBehavior;
import com.runflow.engine.utils.Conv;
import com.runflow.plugin.mybatis.constant.MyBatisTaskConstant;
import com.runflow.plugin.mybatis.model.MyBatisTask;
import com.runflow.spring.boot.SpringContextUtil;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MybatisTaskBehavior extends TaskActivityBehavior {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserTaskActivityBehavior.class);

    protected MyBatisTask mybatisTask;

    public MybatisTaskBehavior(MyBatisTask mybatisTask) {
        this.mybatisTask = mybatisTask;
    }

    @Override
    public void execute(ExecutionEntityImpl execution) {
        String name = execution.getCurrentFlowElement().getName();
        LOGGER.debug("mybatis任务  名称：{}  id:{}  线程名称:{} ", name, execution.getId(), Thread.currentThread().getName());
        MyBatisTaskConstant.SelectTypeEnum selectTypeEnum = MyBatisTaskConstant.SelectTypeEnum.valueOf(Conv.NS(mybatisTask.getSelectType(), MyBatisTaskConstant.SelectTypeEnum.SELECTONE.name()));
        boolean nb = Conv.NB(mybatisTask.isPage()) && selectTypeEnum.name().equals(MyBatisTaskConstant.SelectTypeEnum.SELECTLIST);
        if (nb) {
            int pageNum = Conv.NI(mybatisTask.getPageNum(), MyBatisTaskConstant.DEFAULT_PAGENUM);
            int pageSize = Conv.NI(mybatisTask.getPageSize(), MyBatisTaskConstant.DEFAULT_PAGESIZE);
            PageHelper.startPage(pageNum, pageSize);
        }
        SqlSession sqlSession = SpringContextUtil.getApplicationContext().getBean(SqlSession.class);
        Object result = null;
        switch (selectTypeEnum) {
            case DELETE:
                result = sqlSession.delete(mybatisTask.getStatementId(), mybatisTask.getParam());
                break;
            case INSERT:
                result = sqlSession.insert(mybatisTask.getStatementId(), mybatisTask.getParam());
                break;
            case SELECTLIST:
                result = sqlSession.selectList(mybatisTask.getStatementId(), mybatisTask.getParam());
                break;
            case SELECTONE:
                result = sqlSession.selectOne(mybatisTask.getStatementId(), mybatisTask.getParam());
                break;
        }
        if (nb) {
            result = new PageInfo((List) result);

        }
        ExecutionEntityImpl rootParent = execution.findRootParent(execution);
        rootParent.getVariableInstances().put(execution.getCurrentFlowElement().getId(), result);
        leave(execution);
    }


}
