package org.example.listener;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.Objects;

/**
 * @author: dongxiaohua
 * @date: 2024-09-09 22:53:18
 */
public class MyModel {

  /**
   * 根据key添加监听器
   */
  private Multimap<String, IChangeLister> listeners = HashMultimap.create();

  public void addLister(String key, IChangeLister lister) {
    listeners.put(key, lister);
  }

  public JSONObject get(String key){
    System.out.println(">>> get value");
    executeLister(key);
    return null;
  }

  private void executeLister(String key) {
    if (Objects.isNull(listeners.get(key))) {
      return;
    }

    System.out.println(">>> 监听器逻辑开始");
    Collection<IChangeLister> iChangeListers = listeners.get(key);

    iChangeListers.forEach(it-> {
      it.onChange(key, null, null);
    });
  }
}
