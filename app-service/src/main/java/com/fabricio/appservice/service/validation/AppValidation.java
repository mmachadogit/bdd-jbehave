package com.fabricio.appservice.service.validation;

import com.fabricio.appservice.model.AppModel;
import com.fabricio.appservice.service.excpetion.AppErrorCode;
import com.fabricio.appservice.service.excpetion.ErrorMessages;
import com.fabricio.appservice.service.excpetion.InvalidAppCreateException;
import org.springframework.util.StringUtils;

public class AppValidation {

  public static void appValidation(AppModel appModel){

    if (StringUtils.isEmpty(appModel.getAppId())) {
      throw new InvalidAppCreateException(AppErrorCode.VALIDATION_ERROR_CREATE_APP,
          String.format(ErrorMessages.REQUIRED_ATTRIBUTE, "appId"));
    }

    if (StringUtils.isEmpty(appModel.getName())) {
      throw new InvalidAppCreateException(AppErrorCode.VALIDATION_ERROR_CREATE_APP,
          String.format(ErrorMessages.REQUIRED_ATTRIBUTE, "name"));
    }

    if (StringUtils.isEmpty(appModel.getDescription())) {
      throw new InvalidAppCreateException(AppErrorCode.VALIDATION_ERROR_CREATE_APP,
          String.format(ErrorMessages.REQUIRED_ATTRIBUTE, "description"));
    }
  }
}
