package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorModel {

  private String code;
  private String message;

  public ErrorModel(String appErrorCode, String messsage) {
    this.code = appErrorCode;
    this.message = messsage;
  }
}
