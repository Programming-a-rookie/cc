package org.sinosoft.webservice.service;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://sinosoft.org/webservice/service")
public interface CustomerUserService {

    @WebMethod
    String selectCustomerUser(@WebParam(name = "custInfoParam", targetNamespace = "http://sinosoft.org/webservice/service")String custInfoParam);

}
