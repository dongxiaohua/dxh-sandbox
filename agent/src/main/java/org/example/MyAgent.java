package org.example;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author: dongxiaohua
 * @date: 2024-09-09 22:37:22
 */
public class MyAgent {

  public static void premain(String agentArgs, Instrumentation instrumentation) {
    System.out.println(agentArgs);
    MyTransformer transformer = new MyTransformer();
    instrumentation.addTransformer(transformer);
  }

  public static void agentmain(String agentArgs, Instrumentation instrumentation)
    throws ClassNotFoundException, UnmodifiableClassException {
    // 打印agentArgs，这个参数通过-javaagent:xx.jar=arg传进来
    System.out.println(agentArgs);
    MyTransformer transformer = new MyTransformer();
    // 添加自定义的transformer对象，转换字节码
    instrumentation.addTransformer(transformer, true);
    instrumentation.retransformClasses(Class.forName("DemoUtil"));
  }
}

class MyTransformer implements ClassFileTransformer {

  private static final Map<String,String> map;

  static {
    map = JSONObject.parseObject("{\"print2\":\"testMethod\"}", Map.class);
  }

  @Override
  public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                          ProtectionDomain protectionDomain, byte[] classfileBuffer)
    throws IllegalClassFormatException {
    // 只对指定的类进行增强，这里就增强一下自己写的A这个类
    List<String> methodList = Lists.newArrayList("print2");
    CtClass ctClass = null;
    if (className.endsWith("DemoUtil")) {
      try {
        // 使用Javassist获取类定义
        ClassPool classPool = ClassPool.getDefault();
        classPool.insertClassPath(new LoaderClassPath(loader));
        ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
        // 遍历类的所有方法，进行增强
        for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
          //                    if ("print2".equals(ctMethod.getName())) {
          if (methodList.contains(ctMethod.getName())) {
            String m = ctMethod.getName();
            ctMethod.insertBefore(
              "org.example.PrintUtil.print(\"" + map.get(m) + " : start \");");
            ctMethod.insertAfter("org.example.PrintUtil.print(\"" + m + " : end \");");
          }

          // 如果添加不是基础类型的变量：ctMethod.addLocalVariable("str", classPool.get("java.lang.String"));
          //                    ctMethod.addLocalVariable("startTime", CtClass.longType);
          //                    ctMethod.addLocalVariable("endTime", CtClass.longType);
          //                    ctMethod.addLocalVariable("duration", CtClass.longType);
          //                    // 在方法的开头插入计时逻辑
          //                    ctMethod.insertBefore("startTime = System.currentTimeMillis();");
          //                    // 在方法的结尾插入计时逻辑
          //                    ctMethod.insertAfter("endTime = System.currentTimeMillis();");
          //                    ctMethod.insertAfter("duration = endTime - startTime;");
          //                    ctMethod.insertAfter("System.out.println(\"Method execution time: \" + duration + \"ms\");");
          //                    ctMethod.insertAfter("System.out.println(\"Method execution time: 1111 ms\");");

        }
        // 返回增强后的类字节码
        return ctClass.toBytecode();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (Objects.nonNull(ctClass)) {
          ctClass.detach();
        }
      }
    }

    // 对于其他类，不进行增强，直接返回原始的类字节码
    return classfileBuffer;
  }

  //    String monitorMethodName = "monitor_" + methodName;
  //    CtMethod monitorMethod = CtNewMethod.copy(method, monitorMethodName, clazz, null);
  //    String monitorCode = "{ com.sankuai.meituan.waimai.sc.utils.MonitorCounterUtil.countMethod(\"" + methodName + "\");" +
  //        " return $proceed($$);" + "}";
  //                    monitorMethod.setBody(monitorCode);
  //                    clazz.addMethod(monitorMethod);
  //                    method.setName("original_" + methodName);
}
