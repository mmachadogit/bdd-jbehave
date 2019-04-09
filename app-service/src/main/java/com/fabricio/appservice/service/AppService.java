package com.fabricio.appservice.service;

import com.fabricio.appservice.model.AppModel;
import com.fabricio.appservice.service.validation.AppValidation;
import org.springframework.stereotype.Service;

@Service
public class AppService {

  public AppModel createApp(AppModel appModel){
    AppValidation.appValidation(appModel);
    return appModel;
  }
}
