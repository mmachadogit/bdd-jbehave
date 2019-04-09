package com.fabricio.appservice.model;


import com.fabricio.appservice.service.excpetion.AppErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorModel {

  private String code;
  private String message;

  public ErrorModel(AppErrorCode appErrorCode, String messsage) {
    this.code = appErrorCode.name();
    this.message = messsage;
  }
}
