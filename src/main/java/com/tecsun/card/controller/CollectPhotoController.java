package com.tecsun.card.controller;

import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
import com.tecsun.card.entity.vo.CollectVO;
import com.tecsun.card.service.CardService;
import com.tecsun.card.service.CollectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "collect")
public class CollectPhotoController {
    private final Logger logger = LoggerFactory.getLogger(CollectPhotoController.class);
    @Autowired
    private CollectService collectService;

    @Autowired
    private CardService cardService;
    // 采集库service类
    // private CollectServiceImpl collectService;
    // 中间库service类(存放公安照片)
    // private UserServiceImpl userService;
    // 正式库service类
    // private CardServiceImpl cardService;

    /**
     * TSB照片规集:
     * 1、基础人员信息校验
     * 2、TSB照片规集
     * @return
     */
    @RequestMapping(value = "TSBPhotoManage", method = RequestMethod.POST)
    @ResponseBody
    public Result TSBPxmlbeanshotoManage(@RequestBody CollectVO collectVO) {
        Result result = new Result();
        if (null == collectVO.getSynchroStatus()|| "".equals(collectVO.getSynchroStatus())) {
            result.setStateCode(400);
            result.setMsg("请重新输入同步状态");
            return result;
        }
        // 1、获取某特定状态的数据
        List<BasicPersonInfoPO> listBean = collectService.listQualifiedBasicPerson(collectVO);
        collectVO.setSynchroStatus(collectVO.getSynchroStatus());
        int a = collectService.updateBasicPersonInfoStatus(collectVO);

        // 2、根据线程动态划分数量cert
        // List<List<BasicPersonInfoPO>> dynamicList = ListThreadUtil.dynamicListThread(listBean,5);

        // 3、遍历集合,开始跑线程
        // for (int i = 0; i < 5; i++) {
        //     ThreadPoolUtil.getThreadPool().execute(new UserInfoValidateAndCopyPhotoThreadTask());
        // }

        result.setData(a);
        return result;
    }
}















