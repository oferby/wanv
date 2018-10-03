package com.huawei.sdn.commons.db;

import com.huawei.sdn.commons.db.model.EventCategory;
import com.huawei.sdn.commons.db.model.EventEntity;
import com.huawei.sdn.commons.db.model.EventStatus;
import com.huawei.sdn.commons.db.model.Severity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 4/13/2015.
 */
@Component
public class EventDbControllerImpl implements EventDbController {

    private Logger logger = LoggerFactory.getLogger(EventDbControllerImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");


    public void setup() {

        logger.debug("building event table");

        try {

            jdbcTemplate.execute("create table event_log ( id IDENTITY, " +
                            "eventTime VARCHAR(19), " +
                            "severity varchar(15), " +
                            "category VARCHAR(10), " +
                            "description VARCHAR(255)," +
                            "status VARCHAR(10))"
            );

        } catch (DataAccessException e) {
//            table exists
            logger.debug("event table exists");

        } catch (Exception e) {
            logger.debug("Got Exception: ", e);
        }

    }


    @Override
    public void save(EventEntity eventEntity) {

        logger.debug("Adding event to db: " + eventEntity.toString());


    }

    @Override
    public void save(Severity severity, EventCategory category, String description, EventStatus status) {

        logger.debug("Adding event to db");

        Object[] params = {dateFormat.format(new Date()), severity.name(), category.name(), description, status.name()};
        jdbcTemplate.update("insert into event_log values(NULL,?,?,?,?,?)", params);

    }

    @Override
    public EventEntity getById(long id) {

        Object[] params = {id};
        List<EventEntity> entityList = jdbcTemplate.query("select * from event_log where id=?", params, new BeanPropertyRowMapper(EventEntity.class));

        if (entityList.size() != 0) {
            return entityList.get(0);
        }

        return null;
    }

    @Override
    public List<EventEntity> getAll() {
        return jdbcTemplate.query("select * from event_log order by id desc", new BeanPropertyRowMapper(EventEntity.class));
    }

    @Override
    public List<EventEntity> getNew() {
        return jdbcTemplate.query("select * from event_log where status='NEW' order by id desc", new BeanPropertyRowMapper(EventEntity.class));
    }

    @Override
    public List<EventEntity> getOpen() {
        return jdbcTemplate.query("select * from event_log where status='NEW' or status='OBSERVED' order by id desc", new BeanPropertyRowMapper(EventEntity.class));
    }

    @Override
    public List<EventEntity> getCleared() {
        return jdbcTemplate.query("select * from event_log where status='CLEARED' order by id desc", new BeanPropertyRowMapper(EventEntity.class));
    }

    @Override
    public void setObserved(long id) {

        logger.debug("setting event id " + id + " to status observed");
        Object[] params = {EventStatus.OBSERVED.name(), id};
        jdbcTemplate.update("update event_log set status=? where id=? ", params);

    }

    @Override
    public void setCleared(long id) {
        logger.debug("setting event id " + id + " to status cleared");
        Object[] params = {EventStatus.CLEARED.name(), id};
        jdbcTemplate.update("update event_log set status=? where id=? ", params);
    }

    @Override
    public void setClearAll() {
        logger.debug("Clear all events");
        Object[] params = {EventStatus.CLEARED.name()};
        jdbcTemplate.update("update event_log set status=?", params);
    }

    @Override
    public void deleteById(long id) {

        logger.debug("deleting event id " + id);
        Object[] params = {id};
        jdbcTemplate.update("delete from event_log where id=? ", params);

    }

    @Override
    public void deleteAll() {

        logger.debug("deleting all events from db");
        jdbcTemplate.update("delete from event_log ");


        Object[] params = {Severity.NORMAL.name(), EventCategory.SYSTEM.name(), "All events where deleted from the database", EventStatus.NEW.name()};
        jdbcTemplate.update("insert into event_log values(NULL,?,?,?,?)", params);

    }

    @Override
    public void purge() {

        logger.debug("deleting cleared events from db");
        Object[] clearParams = {EventStatus.CLEARED.name()};
        jdbcTemplate.update("delete from event_log where status=?",clearParams);

    }
}
