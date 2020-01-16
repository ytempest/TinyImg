package com.ytempest.tinyimg;

import com.sun.istack.internal.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author heqidu
 * @since 2020/1/16
 */
public class OptionsHandler {

    private final Map<String, Boolean> supportOptions = new HashMap<String, Boolean>();


    public OptionsHandler(Map<String, Boolean> supportOptions) {
        this.supportOptions.putAll(supportOptions);
    }

    @NotNull
    public Map<String, String> handleOptions(String[] args) throws IllegalArgumentException {
        Map<String, String> optionMap = new HashMap<>();
        if (Utils.getSize(args) == 0) {
            return optionMap;
        }

        for (int index = 0; index < args.length; index++) {
            String option = args[index];
            if (supportOptions.containsKey(option)) {
                String argument = "";
                // 选项有参数
                if (supportOptions.get(option)) {
                    // 选项为最后一个入参，没有为选项设置参数
                    if (index == args.length - 1) {
                        throw new IllegalArgumentException("Can not found the argument for : " + option);
                    }

                    argument = args[index + 1];
                    // 当前选项没有设置参数
                    if (supportOptions.containsKey(argument)) {
                        throw new IllegalArgumentException("Please set argument for : " + option);
                    }

                    // 参数异常
                    if (Utils.isEmpty(argument)) {
                        throw new IllegalArgumentException("Invalid argument for : " + option);
                    }

                    // 跳过参数
                    index++;
                }
                optionMap.put(option, argument);

            } else {
                throw new IllegalArgumentException("Invalid option : " + option);
            }
        }
        return optionMap;
    }
}
