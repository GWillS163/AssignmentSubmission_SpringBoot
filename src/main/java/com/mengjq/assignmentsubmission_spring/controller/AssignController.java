package com.mengjq.assignmentsubmission_spring.controller;

import com.mengjq.assignmentsubmission_spring.mapper.AssignMapper;
import com.mengjq.assignmentsubmission_spring.model.Assign;
//import com.mengjq.assignmentsubmission_spring.model.AssignExample;
import com.mengjq.assignmentsubmission_spring.model.Clazz;
import com.mengjq.assignmentsubmission_spring.service.AssignService;
import com.mengjq.assignmentsubmission_spring.util.TimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

//添加注解

@RestController
@RequestMapping("/assign")
@CrossOrigin
public class AssignController {

    @Autowired
    private AssignService assignService;
    @Autowired
    private AssignMapper assignMapper;

//    //通过构�?�器注入service
//    public AssignController(AssignService assignService){
//        this.assignService=assignService;
//    }

    //查询所有数据 - GET
    @GetMapping("")
    public List<Assign> selectAssign(){
        System.out.println("查询所有数据");
        return assignService.getAllAssignsInfo();
    }

    //查询数据 指定 - GET d 
    @GetMapping("/{id}")
    public List<Assign> selectAssignById(@PathVariable("id") String assignId){
        System.out.println("查询指定数据" + assignId);
        Integer id = Integer.parseInt(assignId);
        return assignService.selectByPrimaryKey(id);
    }

//    根据班级ID获取所有作业信息
    @GetMapping("/class/{id}")
    public List<Assign> getAssignByClassId(@PathVariable("id") String classId){
        System.out.println("获取班级作业信息 classId : " + classId);
        Integer id = Integer.parseInt(classId);
        return assignService.getAssignByClassId(id);
    }

    // 根据教师Id 获取所有作业信息（班级内的）
    @GetMapping("/teacher/{id}")
    public List<Assign> getAssignByTeacherId(@PathVariable("id") String teacherId){
        System.out.println("获取教师作业信息 teacherId : " + teacherId);
        Integer id = Integer.parseInt(teacherId);
        return assignService.getAssignByTeacherId(id);
    }



    //查询数据 - GET
    @GetMapping("/map")
    public Dictionary<Integer, String> selectAssignMap(){
        System.out.println("获取作业Map");
        List<Assign> assigns =  assignService.getAllAssignsMap();

        Dictionary<Integer, String> assignsMap = new Hashtable<Integer, String>();
        for (Assign assign: assigns) {
            if (assign.getBriefName() == null) {
                assign.setBriefName("");
            }
            assignsMap.put(assign.getId(), assign.getBriefName());
        }
        System.out.println(assignsMap);  // {1=张三, 2=李四, 3=王五}
        return assignsMap;
    }
    //查询数据 - GET 根据班级
//    @GetMapping("/class/{id}")
//    public Dictionary<Integer, String> selectAssignByClass(){
//        System.out.println("获取作业Map");
//        List<Assign> assigns =  assignService.getAllAssignsByClass();
//
//        Dictionary<Integer, String> assignsMap = new Hashtable<Integer, String>();
//        for (Assign assign: assigns) {
//            if (assign.getBriefName() == null) {
//                assign.setBriefName("");
//            }
//            assignsMap.put(assign.getId(), assign.getBriefName());
//        }
//        System.out.println(assignsMap);  // {1=张三, 2=李四, 3=王五}
//        return assignsMap;
//    }


    //添加数据
    @PostMapping("")
    public Assign insertUser(Assign assign) throws ParseException {
        System.out.println("添加数据"+ assign);
        assign.setDdl(TimeFormat.verifyTimeOrDefault(assign.getDdl()));
        assign.setCreateTime(TimeFormat.getNowTime());

//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//        assign.setUploadTime(df.parse(assign.getUploadTime()));
        int i = assignService.insert(assign);
        return assign;
    }

    //修改数据
//    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    @PutMapping("/{id}")
    public Integer updateAssign(@PathVariable int id, Assign assign) {
        System.out.println("修改数据");
        assign.setDdl(TimeFormat.verifyTimeOrDefault(assign.getDdl())); // 验证时间格式
        assign.setCreateTime(TimeFormat.verifyTimeOrDefault(assign.getCreateTime())); // 验证时间格式， 如果没有创建时间就用 默认事件

        System.out.println("修改数据"+ assign);
        System.out.println(assign.getTimeoutSubmit());
        return assignService.updateAssign(id, assign);
//        return ResponseEntity.ok(assign);
    }


    //删除数据
    @DeleteMapping("/{id}")
    @ResponseBody
    public String delUser(@PathVariable("id") String assignId){
        System.out.println("删除数据"+ assignId);
        Integer id = Integer.parseInt(assignId);
        assignService.deleteByPrimaryKey(id);

        //返回状码
        return "200";
    }
}
