package org.example.callback;

/**
 * @author: dongxiaohua
 * @date: 2024-09-09 22:58:26
 */
public class MyCallback implements ICallBack {
  @Override
  public void onCallBack(String key, String oldValue, String newValue) {
    System.out.println(key + ": " + oldValue + ", " + newValue);
  }
}
