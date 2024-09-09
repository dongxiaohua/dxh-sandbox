package org.example.callback;

/**
 * @author: dongxiaohua
 * @date: 2024-09-09 22:59:08
 */
public class MyWork {
  private ICallBack callBack;

  public void setCallBack(ICallBack callBack) {
    this.callBack = callBack;
  }

  public void doWork(){
    System.out.println(">>> start and do word");

    if (callBack != null) {
      callBack.onCallBack("k1", "old", "new");
    }
  }

  public void doWord(String key, ICallBack iCallBack) {
    System.out.println(">>> start doWork");
    if (iCallBack != null) {
      iCallBack.onCallBack(key, "aaa", "bbb");
    }
  }
}
