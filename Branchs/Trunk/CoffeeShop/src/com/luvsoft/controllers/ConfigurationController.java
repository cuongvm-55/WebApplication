package com.luvsoft.controllers;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.luvsoft.entities.Configuration;
import com.luvsoft.facades.ConfigurationFacade;

public class ConfigurationController {
    private static ConfigurationFacade confFacade = new ConfigurationFacade();

    public ConfigurationController(){
    }

    public Configuration getConfiguration(){
        List<Configuration> confList = new ArrayList<Configuration>();
        confFacade.findAll(confList);

        // If no stored configuration
        // Save default config to db
        if( confList.size() <= 0 ){
            Configuration conf = new Configuration();
            ObjectId id = new ObjectId();
            conf.setId(id.toString());
            conf.setSupPincode(Configuration.DEFAULT_SUP_PINCODE);
            conf.setReportOutputDir(Configuration.DEFAULT_REPORT_OUTPUT_DIR);
            if( !confFacade.save(conf) ){
                System.out.println("Fail to save default configuration!");
            }

            return conf;
        }
        return confList.get(0); // first found configuration
    }

    /**
     * Login
     * @param pincode
     * @return
     */
    public boolean loginReq(String pincode){
        return pincode.length() == Configuration.PINCODE_LENGTH
                && pincode.equals(getConfiguration().getSupPincode());
    }
    
    public boolean updateConfiguration(Configuration conf){
        return confFacade.update(conf.getId(), conf);
    }
}
