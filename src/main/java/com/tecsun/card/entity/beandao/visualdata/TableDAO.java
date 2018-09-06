package com.tecsun.card.entity.beandao.visualdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TableDAO {
    private String title;
    private List<ColumnDAO> children;
    private String comments;

    public String getComments() {
        if (null == comments || "".equals(comments)) {
            this.comments = "不存在";
        }
        return this.comments;
    }
}
