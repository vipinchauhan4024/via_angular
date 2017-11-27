package com.patternservices.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.patternservices.service.PatternDataService;
import com.patternservices.service.impl.PatternDataServiceImpl;
import com.patternservices.util.SysLogger;

@Path("/json/patterndata")
public class PatternDataJSONService {

    private PatternDataService patternDataService = new PatternDataServiceImpl();

    @GET
    @Path("/fetchAllApplicationType")
    @Produces(MediaType.APPLICATION_JSON)
    public String getApplicarionDataInJSON() {
        return patternDataService.fetchAllAppTypes();
    }

  
    @POST
    @Path("/submitIntegrationRequest")
    @Produces(MediaType.TEXT_PLAIN) @Consumes("application/x-www-form-urlencoded")
    public String submitData(@FormParam("appType") String appType, @FormParam("msgSize") String msgSize, @FormParam("volPerHour") String volPerHour,
            @FormParam("depType") String depType, @FormParam("encType") String encType, @FormParam("recType") String recType,
            @FormParam("suppType") String suppType, @FormParam("suprType") String suprType
            ,@FormParam("formatType") String formatType, @FormParam("routeType") String routeType) {

        

        SysLogger.logMethod("RESFUL Method SubmitData",
                            "All values are read. appType is : " + appType + " msgSize is :" + msgSize + " volPerHour is :" + volPerHour
                                    + " depType is :" + depType + " encType is :" + encType + " recType is :" + recType
                                    + " suppType is :" + suppType + " suprType is :" + suprType
                                    + " formatType is :" + formatType + " routeType is :" + routeType);

        
        String supp = appType;
        String supr = recType;
        String val;
       
        if((supp=="1" || supp== "3" || supp=="4" || supp== "5" )&&(supr=="1" || supr== "3" || supr=="4" || supr== "5"))
        {
        val=  appType + ":"+ msgSize + ":"+ volPerHour + ":"+ depType +":"+ encType +":" + recType +":"+ formatType  +":"+ routeType;
        
        }
        else if(supp=="1" || supp== "3" || supp=="4" || supp== "5" )
            {
            supr= suprType;
            val=  appType + ":"+ msgSize + ":"+ volPerHour + ":"+ depType +":"+ encType +":" + recType +":"+ formatType  +":"+ routeType;
            }
        
            
             else if(supr=="1" || supr== "3" || supr=="4" || supr== "5")
            {
                 supp= suppType;
                 val=  appType + ":"+ msgSize + ":"+ volPerHour + ":"+ depType +":"+ encType +":" + recType +":"+ formatType  +":"+ routeType;

            
            }
         else
             {
             supr = suprType;
             supp= suppType;
             val=  appType + ":"+ msgSize + ":"+ volPerHour + ":"+ depType +":"+ encType +":" + recType +":"+ formatType  +":"+ routeType;
               

           }
      System.out.println("VALUE:" + val);
        
        
        
        
              
        String pattern = appType.isEmpty()?"0":"1" + ":" +  (msgSize.isEmpty()?"0":"1") + ":" + (volPerHour.isEmpty()?"0":"1") + ":" 
             + (depType.isEmpty()?"0":"1") + ":" +  (encType.isEmpty()?"0":"1") + ":" +  (recType.isEmpty()?"0":"1")
             + ":" + (suppType.isEmpty()?"0":"1") + ":" +  (suprType.isEmpty()?"0":"1")
             + ":" + (formatType.isEmpty()?"0":"1")  + ":" + (routeType.isEmpty()?"0":"1");
        
        String patternDesign = patternDataService.submitPatternData(val);
        
         return patternDesign;

    }

   
}
