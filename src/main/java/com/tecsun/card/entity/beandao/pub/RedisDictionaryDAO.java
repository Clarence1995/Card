package com.tecsun.card.entity.beandao.pub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RedisDictionaryDAO {
    private Long id;
    private String groupId;
    private String code;
    private String name;
    private List<RedisDic> children;
}
