package org.example.listener;

import com.alibaba.fastjson.JSONObject;

/**
 * @author: dongxiaohua
 * @date: 2024-09-09 22:52:04
 */
public interface IChangeLister {

  void onChange(String key, JSONObject oldValue, JSONObject newValue);
}
