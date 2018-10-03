package com.huawei.sdn.commons.db;

import com.huawei.sdn.commons.db.model.EventCategory;
import com.huawei.sdn.commons.db.model.EventEntity;
import com.huawei.sdn.commons.db.model.EventStatus;
import com.huawei.sdn.commons.db.model.Severity;

import java.util.List;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 4/13/2015.
 */
public interface EventDbController {

    void save(EventEntity  eventEntity);
    void save(Severity severity,
            EventCategory category,
            String description,
            EventStatus status);
    EventEntity getById(long id);
    List<EventEntity>getAll();
    List<EventEntity>getNew();
    List<EventEntity>getOpen();
    List<EventEntity>getCleared();
    void setObserved(long id);
    void setCleared(long id);
    void setClearAll();
    void deleteById(long id);
    void deleteAll();
    void purge();

}
