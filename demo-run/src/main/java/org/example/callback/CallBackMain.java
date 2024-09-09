package org.example.callback;

/**
 * @author: dongxiaohua
 * @date: 2024-09-09 23:01:36
 */
public class CallBackMain {

  public static void main(String[] args) {
    MyWork work = new MyWork();

//    MyCallback myCallback = new MyCallback();
//
//    work.setCallBack(myCallback);
//    work.doWord();

    work.setCallBack(new ICallBack() {
      @Override
      public void onCallBack(String key, String oldValue, String newValue) {
        System.out.println("The callback: " + key + "-" + oldValue + "-" + newValue);
      }
    });

    work.doWork();

    System.out.println();


    work.doWord("the", ((key, oldValue, newValue) -> {
      System.out.println("old: " + oldValue);
      System.out.println("new: " + newValue);
    }));

    System.out.println();

    work.doWord("then", new MyCallback());
  }
}
