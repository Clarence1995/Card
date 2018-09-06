package com.tecsun.card.entity.beandao.visualdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UserDAO {
    private String title;
    private boolean expanded = true;
    private List<TableDAO> children;
}
