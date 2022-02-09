package org.sinosoft.webservice.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerUserOptimal {

    private String lastUserid;//匹配坐席id(ts_user 表id)
    private String custCodeori;//电销原始客户编号
}
