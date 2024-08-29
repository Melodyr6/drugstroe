package com.aaa.controller;

import com.aaa.entity.Baoque;
import com.aaa.entity.Huishou;
import com.aaa.entity.Record;
import com.aaa.entity.Ypharmacy;
import com.aaa.mapper.PharmacyMapper;
import com.aaa.service.PharmacyService;
import com.aaa.service.RecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("pharmacy")
public class PharmacyController {
    @Autowired
    private PharmacyService pharmacyService;
    @Autowired
    private RecordService rs;
    @Autowired
    private PharmacyMapper pharmacyMapper;

    //查询药品仓库
    @GetMapping("selectpharmacy")
    @ResponseBody
    public Object selectpharmary(Ypharmacy ypharmacy, Integer page, Integer limit) {
        System.out.print("进去查询药房方法");
        PageHelper.startPage(page, limit);
        List<Ypharmacy> drugstores = pharmacyService.selpharmacy(ypharmacy);
        PageInfo pageInfo = new PageInfo(drugstores);
        Map<String, Object> drugstoresData = new HashMap<String, Object>();
        //这是layui要求返回的json数据格式
        drugstoresData.put("code", 0);
        drugstoresData.put("msg", "");
        //将全部数据的条数作为count传给前台（一共多少条）
        drugstoresData.put("count", pageInfo.getTotal());
        //将分页后的数据返回（每页要显示的数据）
        drugstoresData.put("data", pageInfo.getList());
        return drugstoresData;
    }

    //添加一条药品采到报缺单
    @PostMapping("addbaoque")
    @ResponseBody
    public Integer addbaoque(Baoque baoque) {
        Integer selbaoqueName = pharmacyService.selbaoqueName(baoque);//查询报缺单是否已经有此条药品
        if (selbaoqueName == 0) {
            int addbaoque = pharmacyService.addbaoque(baoque);//添加药品
            return addbaoque;
        } else {
            int upbaoquenum = pharmacyService.upbaoquenum(baoque);//修改药品数量
            return upbaoquenum;
        }

    }

    @PostMapping("addInventory")
    @ResponseBody
    public Integer addInventory(Ypharmacy ypharmacy) {
        int resultStatus = pharmacyMapper.addInventory(ypharmacy);
        return resultStatus;
    }

    @PostMapping("delpharymary")
    @ResponseBody
    public Integer delpharymary(Ypharmacy ypharmacy, Huishou huishou) {
        //删除药房此药（回收给库房）
        System.out.println("进入回收");
        int delpharymacy = pharmacyService.delpharymacy(ypharmacy);
        if (delpharymacy == 1) {
            pharmacyService.addhuishou(huishou);
        }
        System.out.println(delpharymacy);
        return delpharymacy;

    }

    //查询药品仓库
    @GetMapping("selecthuishou")
    @ResponseBody
    public Object selecthuishou(Huishou huishou, Integer page, Integer limit) {
        System.out.print("进去查询药房方法");
        PageHelper.startPage(page, limit);
        List<Huishou> drugstores = pharmacyService.selhuishou(huishou);
        PageInfo pageInfo = new PageInfo(drugstores);
        Map<String, Object> drugstoresData = new HashMap<String, Object>();
        //这是layui要求返回的json数据格式
        drugstoresData.put("code", 0);
        drugstoresData.put("msg", "");
        //将全部数据的条数作为count传给前台（一共多少条）
        drugstoresData.put("count", pageInfo.getTotal());
        //将分页后的数据返回（每页要显示的数据）
        drugstoresData.put("data", pageInfo.getList());
        return drugstoresData;
    }

    @PostMapping("delhuishou")
    @ResponseBody
    public Integer delhuishou(Huishou huishou, Record record) {
        //删除回收表此药（处理）
        System.out.println("进入处理");
        int delhuishou = pharmacyService.delhuishou(huishou);
        if (delhuishou == 1) {
            System.out.print("添加记录表");
            int addjilu = rs.addjilu(record);//添加一条记录
        }

        return delhuishou;

    }


}
