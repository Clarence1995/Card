package com.tecsun.card.entity.beandao.pub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RedisDic {
    private Long dictionId; // 数据库字典ID,获取它是为了前端更好的遍历
    private String name;      // 字典中文名称
    private String status;    // 状态 0:可用 1:不可用
    private String groupId;   // 所属组,如CARD_TYPE
    private String code;      // 字典值,可以是数值、也可以为字符串
    private String del;       // 是否可删 0:可删除 1:不可删除
}
