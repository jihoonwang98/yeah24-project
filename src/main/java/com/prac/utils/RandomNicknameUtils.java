package com.prac.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RandomNicknameUtils {

    private Random random = new Random(System.nanoTime());
    private List<String> modifiers = Arrays.asList("두려운", "순진한", "지능이_높은", "기분_좋은", "흥겨운", "화난", "신랄한", "지루한", "짜증난");
    private List<String> animals = Arrays.asList("강아지", "고양이", "곰", "물고기", "쥐", "소", "원숭이", "펭귄", "물개", "사자", "호랑이");


    public String getNickname() {

        String modifier = modifiers.get(random.nextInt(modifiers.size()));
        String animal = animals.get(random.nextInt(animals.size()));
        String uuid = UUID.randomUUID().toString().replace("-", "");

        return modifier + "_" + animal + "_" + uuid;
    }
}
