package cn.rzpt.service.publish.inter;

public interface MessageCallBack {

    void onSuccess(String result);

    void onError(Exception e);

}
