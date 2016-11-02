package com.ninethree.palychannelbusiness.bean;

import java.io.Serializable;

public class Result implements Serializable {

    private static final long serialVersionUID = -233466677690184829L;

    private Integer ResultNum;
    private String RetMsg;
    private Integer Ret_Value;
    private String Ret_Msg;
    private boolean result;
    private String message;

    public Result() {

    }

    public Integer getResultNum() {
        return ResultNum;
    }

    public void setResultNum(Integer resultNum) {
        ResultNum = resultNum;
    }

    public String getRetMsg() {
        return RetMsg;
    }

    public void setRetMsg(String retMsg) {
        RetMsg = retMsg;
    }

    public Integer getRet_Value() {
        return Ret_Value;
    }

    public void setRet_Value(Integer ret_Value) {
        Ret_Value = ret_Value;
    }

    public String getRet_Msg() {
        return Ret_Msg;
    }

    public void setRet_Msg(String ret_Msg) {
        Ret_Msg = ret_Msg;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Table [ResultNum=" + ResultNum + ", RetMsg=" + RetMsg + "]";
    }

}
