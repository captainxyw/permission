package com.permission.util;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Package:com.permission.util
 * Description:
 *
 * @Date:2020/1/11 22:42
 * @Author:xuyewei
 */

public class StringUtil {

  public static List<Integer> splitToListInt(String str) {
    List<String> strList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
    return strList.stream().map(strItem -> Integer.parseInt(strItem)).collect(Collectors.toList());
  }
}
