package com.huawei.sdn.commons.selector.routing.selector;

import com.huawei.sdn.commons.db.EventDbController;
import com.huawei.sdn.commons.db.model.EventCategory;
import com.huawei.sdn.commons.db.model.EventEntity;
import com.huawei.sdn.commons.db.model.EventStatus;
import com.huawei.sdn.commons.db.model.Severity;

import java.util.List;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 4/16/2015.
 */
public class EventDbControllerMock implements EventDbController {
    @Override
    public void save(EventEntity eventEntity) {

    }

    @Override
    public void save(Severity severity, EventCategory category, String description, EventStatus status) {

    }

    @Override
    public EventEntity getById(long id) {
        return null;
    }

    @Override
    public List<EventEntity> getAll() {
        return null;
    }

    @Override
    public List<EventEntity> getNew() {
        return null;
    }

    @Override
    public List<EventEntity> getOpen() {
        return null;
    }

    @Override
    public List<EventEntity> getCleared() {
        return null;
    }

    @Override
    public void setObserved(long id) {

    }

    @Override
    public void setCleared(long id) {

    }

    @Override
    public void setClearAll() {

    }

    @Override
    public void deleteById(long id) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void purge() {

    }
}
