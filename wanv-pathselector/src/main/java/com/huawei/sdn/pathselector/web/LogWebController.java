package com.huawei.sdn.pathselector.web;

import com.huawei.sdn.commons.db.EventDbController;
import com.huawei.sdn.commons.db.model.EventEntity;
import com.huawei.sdn.commons.db.model.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 4/13/2015.
 */
@Controller
@RequestMapping("/log")
public class LogWebController {


    @Autowired
    private EventDbController eventDbController;

    @RequestMapping(value = "event", method = RequestMethod.GET)
    @ResponseBody
    public List<EventEntity>getAll(){

        return eventDbController.getAll();

    }

    @RequestMapping(value = "event/new", method = RequestMethod.GET)
    @ResponseBody
    public List<EventEntity>getNew(){

        return eventDbController.getNew();

    }

    @RequestMapping(value = "event/open", method = RequestMethod.GET)
    @ResponseBody
    public List<EventEntity>getOpen(){

        return eventDbController.getOpen();

    }

    @RequestMapping(value = "event/cleared", method = RequestMethod.GET)
    @ResponseBody
    public List<EventEntity>getCleared(){

        return eventDbController.getCleared();

    }


    @RequestMapping(value = "event/purge",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity purge(){
        eventDbController.purge();

        return new ResponseEntity(HttpStatus.OK);

    }

    @RequestMapping(value = "event/clear/all",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity clearAll(){

        eventDbController.setClearAll();

        return new ResponseEntity(HttpStatus.OK);

    }


    @RequestMapping(value = "event/id/{id}/status/{status}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity setState(@PathVariable long id, @PathVariable EventStatus status){

        if(status == EventStatus.OBSERVED){

            eventDbController.setObserved(id);
            return new ResponseEntity(HttpStatus.OK);


        } else if(status == EventStatus.CLEARED) {

            eventDbController.setCleared(id);
            return new ResponseEntity(HttpStatus.OK);

        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }


}
