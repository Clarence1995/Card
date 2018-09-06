package com.tecsun.card.entity.beandao.visualdata;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class ColumnDAO {
    private String title;
    @JsonIgnore
    private String tableName;
    @JsonIgnore
    private String columnName;
    @JsonIgnore
    private String dataType;
    @JsonIgnore
    private String length;
    @JsonIgnore
    private String nullable;
    private List children;
    @JsonIgnore
    private String comments;

    public List getChildren() {
        List<Object> children = new ArrayList<>();
        JSONObject              jsonObject = new JSONObject();
        List<Object>            list       = new ArrayList<>();

//        HashMap<String, Object> hashMap    = new HashMap<>();
//        hashMap.put("title", "DATATYPE: " + dataType);
//        hashMap.put("title", "DATALENGTH: " + length);
//        hashMap.put("title", "NULLABLE: " + nullable);
        if (null == comments || "".equals(comments)) {
            this.comments = "不存在";
        }
//        hashMap.put("title", comments);

//        list.add(hashMap);
//        jsonObject.put("children", list);
        jsonObject.put("title", comments);
        System.out.println("JSONOBJCet" + jsonObject.toJSONString());
        children.add(jsonObject);
        return children;
    }
}
