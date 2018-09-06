package com.tecsun.card.entity.beandao.visualdata;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BossDAO {
    private List<UserDAO> children;
    private String title;
}
