package com.zsgwsjj.im.util;

import java.util.Random;

/**
 * @author : jiang
 * @time : 2018/4/27 17:01
 */
public class IdGene {

    public static long geneId() {
        Random random = new Random();
        return random.nextInt(9999999) + 10000000;
    }
}
