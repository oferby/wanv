package com.huawei.sdn.pathselector.persistancy;

import com.huawei.sdn.commons.db.EventDbController;
import com.huawei.sdn.commons.db.EventDbControllerImpl;
import com.huawei.sdn.commons.db.model.EventCategory;
import com.huawei.sdn.commons.db.model.EventEntity;
import com.huawei.sdn.commons.db.model.EventStatus;
import com.huawei.sdn.commons.db.model.Severity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 4/13/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = EventLogTestConfigurator.class)
public class TestEventLogger {

    @Autowired
    private EventDbController eventDbController;


    @Test
    public void testEventLoggerCrud(){


        assert eventDbController!=null;

        ((EventDbControllerImpl)eventDbController).setup();

        eventDbController.save(Severity.NORMAL, EventCategory.SYSTEM,"System Start", EventStatus.NEW);

        List<EventEntity> entityList = eventDbController.getAll();

        assert entityList.size()==1;

        EventEntity eventEntity = entityList.get(0);

        assert eventEntity.getCategory()==EventCategory.SYSTEM;
        assert eventEntity.getStatus()==EventStatus.NEW;


        entityList = eventDbController.getNew();

        assert entityList.size()==1;

        eventDbController.setObserved(eventEntity.getId());

        entityList = eventDbController.getAll();

        assert entityList.size()==1;

        eventEntity = entityList.get(0);

        assert eventEntity.getStatus()==EventStatus.OBSERVED;


        entityList = eventDbController.getNew();

        assert entityList.size()==0;


        entityList = eventDbController.getOpen();

        assert entityList.size()==1;


        eventDbController.deleteById(eventEntity.getId());


        entityList = eventDbController.getAll();

        assert entityList.size()==0;



    }


}
