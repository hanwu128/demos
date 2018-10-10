package com.lenovo.iot.digitaltwin.controller.stream;


import com.lenovo.iot.digitaltwin.model.DigitalTwinInstance;
import com.lenovo.iot.digitaltwin.service.DigitalTwinInstanceStreamService;
import com.lenovo.iot.digitaltwin.util.JsonResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Desc: 为DT Stage提供的访问API，使用严格的REST风格
 * Name: com.lenovo.iot.digitaltwin.controller.stream.DigitalTwinInstanceStreamController
 * Author: chench9@lenovo.com
 * Date: 2018/6/22 11:24
 **/
@RestController
@RequestMapping("/dt/stream")
public class DigitalTwinInstanceStreamController {
    private static final Logger logger = LoggerFactory.getLogger(DigitalTwinInstanceStreamController.class);

    @Autowired
    private DigitalTwinInstanceStreamService digitalTwinInstanceStreamService;

    /**
     * 查询DT实例列表
     * @param req
     * @param resp
     * @return
     */
    @GetMapping("/instances")
    public Object list(HttpServletRequest req, HttpServletResponse resp,
                       @RequestParam(value = "detail", required = false) boolean detail) {
        try {
            List<DigitalTwinInstance> list = detail ? digitalTwinInstanceStreamService.getDigitalTwinInstanceListDetail() :
                    digitalTwinInstanceStreamService.getDigitalTwinInstanceListSummary();
            return JsonResp.getSuccessResp(list);
        } catch (Exception e) {
            logger.error("get dt instance list all error: {}", e.getMessage());
            e.printStackTrace();
        }
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return JsonResp.getErrorResp();
    }

    /**
     * 查询DT实例详情
     * @param req
     * @param resp
     * @param id
     * @return
     */
    @GetMapping("/instance/{id}")
    public Object detail(HttpServletRequest req, HttpServletResponse resp,
                         @PathVariable("id") Long id) {
        if(id == null || id <= 0) {
            logger.error("get dt instance detail error: {}", id);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
        }

        try {
            DigitalTwinInstance dt = digitalTwinInstanceStreamService.getDigitalTwinInstanceDetail(id);
            if(dt == null) {
                logger.error("dt instance not found: {}", id);
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return JsonResp.getErrorResp(HttpServletResponse.SC_NOT_FOUND, JsonResp.MSG_NOT_FOUND);
            }
            return JsonResp.getSuccessResp(dt);
        } catch (Exception e) {
            logger.error("get dt instance detail error: {}, {}", id, e.getMessage());
            e.printStackTrace();
        }

        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return JsonResp.getErrorResp();
    }

    /**
     * 锁住/解锁物实例
     * @param req
     * @param resp
     * @param lockup
     * @return
     */
    @PatchMapping("/instance/lockup/{id}")
    public Object lock(HttpServletRequest req, HttpServletResponse resp,
                       @PathVariable("id") Long id,
                       @RequestParam("lockup") short lockup) {
        try {
            DigitalTwinInstance instance = new DigitalTwinInstance();
            instance.setId(id);
            instance.setLockup(lockup);
            if(digitalTwinInstanceStreamService.updateDigitalTwinInstanceLock(instance) > 0) {
                return JsonResp.getSuccessResp();
            }
        } catch (Exception e) {
            logger.error("update dt instance lock error: {}, {}, {}", id, lockup, e.getMessage());
            e.printStackTrace();
        }
        return JsonResp.getErrorResp();
    }

}
