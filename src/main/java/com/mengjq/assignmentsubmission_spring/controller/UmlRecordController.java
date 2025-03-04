package com.mengjq.assignmentsubmission_spring.controller;

import com.mengjq.assignmentsubmission_spring.mapper.UmlRecordMapper;
import com.mengjq.assignmentsubmission_spring.model.UmlRecord;
import com.mengjq.assignmentsubmission_spring.util.AIUmlGenerator;
import com.mengjq.assignmentsubmission_spring.util.TimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Dictionary;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/uml")
public class UmlRecordController {

    @Autowired
    private UmlRecordMapper umlRecordMapper;

    // select all uml records
    @GetMapping("")
    public List<UmlRecord> selectAllUmlRecord() {
        List<UmlRecord> umlRecordMappers = umlRecordMapper.selectAllUmlRecord();
        return umlRecordMappers;
    }

    // select uml without hide = true
    @GetMapping("/withoutHide")
    public List<UmlRecord> selectAllUmlRecordWithoutHide() {
        List<UmlRecord> umlRecordMappers = umlRecordMapper.selectAllUmlRecordWithoutHide();
        return umlRecordMappers;
    }

    // continue to generate uml
    @GetMapping("/{id}/refresh")
    public UmlRecord continueUmlRecord(@PathVariable("id") int id) {
        UmlRecord umlRecord = umlRecordMapper.selectUmlRecordById(id);
        System.out.println("continueUmlRecord: " + umlRecord);
        return startRequest(umlRecord);
    }

    // refresh uml code
    @GetMapping("/{id}/runUmlCode")
    public UmlRecord runUmlCode(@PathVariable("id") int id) {
        UmlRecord umlRecord = umlRecordMapper.selectUmlRecordById(id);
        System.out.println("runUmlCode: " + umlRecord);
        return startRunUmlCode(umlRecord);
    }

    @PostMapping("/{id}/runUmlCode")
    private UmlRecord startRunUmlCode(UmlRecord umlRecord) {
        System.out.println("runUmlCode: " + umlRecord);
//        if ("@startuml" not in umlRecord.getUml_code()) {
        if (!umlRecord.getUml_code().contains("@startuml")) {
            return umlRecord;
        }
        // 通过uml code生成png
        AIUmlGenerator aiUmlGenerator = new AIUmlGenerator();
        String umlPngSrc = aiUmlGenerator.getNewUmlUrl(umlRecord.getUml_code());
        umlRecord.setUml_png_src(umlPngSrc);
        umlRecord.setLast_edit_time(TimeFormat.getNowTime());

//        System.out.println("umlRecord after draw: " + umlRecord);
        int res = umlRecordMapper.updateById(umlRecord);
        return umlRecord;
    }

    // 虚拟删除
    @PutMapping("/{id}")
    public int putUmlRecord(UmlRecord umlRecord) {
        umlRecord.setIs_hide(true);
        String onHandling = "正在处理";
//        umlRecord.setUml_code(onHandling);
//        umlRecord.setUml_intro(onHandling);
//        umlRecord.setUser_input(inputFilter(umlRecord.getUser_input()));
        return umlRecordMapper.updateById(umlRecord);
    }

    @PostMapping("")
    public UmlRecord postUmlRecord(UmlRecord umlRecord) {
        System.out.println("postUmlRecord");
        String onHandling = "正在处理";
        System.out.println(umlRecord);
        // 插入数据，以便能看到查询结果
        umlRecord.setGpt_response(onHandling);
        umlRecord.setUml_code(onHandling);
        umlRecord.setUml_intro(onHandling);
        umlRecord.setCreate_time(TimeFormat.getNowTime());
        umlRecordMapper.insert(umlRecord);
        // turn the quote symbol to slash
        umlRecord.setUser_input(inputFilter(umlRecord.getUser_input()));

        return startRequest(umlRecord);
    }

    // 开始请求数据
    private UmlRecord startRequest(UmlRecord umlRecord) {

        // 询问GPT并分隔， 得到三段文字
        AIUmlGenerator aiUmlGenerator = new AIUmlGenerator();
//        System.out.println("userInput: " + umlRecord.getUser_input());
        if (umlRecord.getUser_input().equals(" ") || umlRecord.getUser_input() == null) {
            return umlRecord;
        }
        Dictionary<String, String> responseDict = aiUmlGenerator.askGPT(umlRecord.getUser_input());
        aiUmlGenerator.echoDict(responseDict);
        // 更新GPT response数据
        umlRecord.setGpt_response(responseDict.get("gptResponse"));
        umlRecord.setUml_code(responseDict.get("umlCode"));
        umlRecord.setUml_intro(responseDict.get("umlIntro"));

        // 根据UmlCode 进行生成图片
        return startRunUmlCode(umlRecord);
    }

    // input filter
    public String inputFilter(String input) {
        System.out.println("inputFilter: " + input);
        if (input == null) {
            return "";
        }
        return input.replace("\"", "\\\"");
    }

}
