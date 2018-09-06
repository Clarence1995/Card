package com.tecsun.card.entity.vo.echarts;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VDDoughuntVO {
    private List<String> legendData;
    private List<CollectDataBean> seriesData;
}
