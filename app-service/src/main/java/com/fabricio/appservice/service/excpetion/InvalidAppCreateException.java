package com.fabricio.appservice.service.excpetion;

import com.fabricio.appservice.model.ErrorModel;

public class InvalidAppCreateException extends RuntimeException {

  private final ErrorModel errorModel;

  public InvalidAppCreateException(AppErrorCode appErrorCode, String message) {
    super();
    this.errorModel = new ErrorModel(appErrorCode, message);
  }

  public ErrorModel getError() {
    return errorModel;
  }
}
