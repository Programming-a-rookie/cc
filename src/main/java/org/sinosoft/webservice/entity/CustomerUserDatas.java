package org.sinosoft.webservice.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class CustomerUserDatas {

    private String id;
    private String complementDate;
    private String pushTime;
    private ArrayList<CustomerUserOptimal> datas;
}
