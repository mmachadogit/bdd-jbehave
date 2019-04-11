package exception;


import model.ErrorModel;
import utils.JSONUtil;


public class ApiException extends RuntimeException {
    private int errorCode;
    private ErrorModel error;

    public ApiException(int errorCode, String message) {
      super(message);
      this.errorCode = errorCode;
      this.error = JSONUtil.deserialize(message, ErrorModel.class);
    }

    public int getErrorCode() {
      return errorCode;
    }

    public ErrorModel getError() {
      return error;
    }
}

