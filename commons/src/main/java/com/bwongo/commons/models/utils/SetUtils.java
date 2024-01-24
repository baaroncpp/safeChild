package com.bwongo.commons.models.utils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author bkaaron
 * @created 17/06/2022
 * @project kabangali
 */
public class SetUtils {
    public static final String GRANTS_SEPARATOR = ",";

    public static Set getSetFromStringWithSeparator(String stringForSet){
        return Arrays.asList(stringForSet.trim().split(GRANTS_SEPARATOR))
                .stream().collect(Collectors.toSet());
    }
}
