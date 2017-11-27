package com.patternservices.datatype;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.patternservices.rest.serializers.PatternTypeEnumSerializer;

@JsonSerialize(using = PatternTypeEnumSerializer.class)

public enum ApplicationType {
    
 VGTA("VGTA","1"), COTS("COTS","2"), SAP("SAP","3"), VCOM("VCOM","4"), BI("BI","5"), EXTERNAL("EXTERNAL","6");

 private String appName;
 private String appId;

 private ApplicationType(String appName, String appId) {
     this.appName = appName;
     this.appId = appId;
     
 }

public String getAppName() {
    return appName;
}

public void setAppName(String appName) {
    this.appName = appName;
}

public String getAppId() {
    return appId;
}

public void setAppId(String appId) {
    this.appId = appId;
}
 
 
 
 
 
} 
