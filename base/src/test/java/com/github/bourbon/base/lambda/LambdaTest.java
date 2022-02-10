package com.github.bourbon.base.lambda;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/19 12:19
 */
public class LambdaTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {
        List<Dish> dishes = ListUtils.newArrayList(
                new Dish("红烧肉", 1000, 1),
                new Dish("奶油蛋糕", 800, 2),
                new Dish("巧克力", 700, 2),
                new Dish("糖醋排骨", 600, 1),
                new Dish("披萨", 500, 3),
                new Dish("轻食", 400, 3),
                new Dish("蔬菜沙拉", 300, 3),
                new Dish("无糖可乐", 100, 4)
        );
        Assert.assertEquals(CollectionUtils.toString(beforeJava7(dishes)), CollectionUtils.toString(afterJava7(dishes)));
        Assert.assertEquals(beforeJava8(dishes).toString(), afterJava8(dishes).toString());

        logger.info("map min: {}", dishes.stream().map(Dish::getCalories).min(Integer::compare));
        logger.info("map max: {}", dishes.stream().map(Dish::getCalories).max(Integer::compare));
        logger.info("mapToInt min: {}", dishes.stream().mapToInt(Dish::getCalories).min());
        logger.info("mapToInt max: {}", dishes.stream().mapToInt(Dish::getCalories).max());
        logger.info("map minBy: {}", dishes.stream().map(Dish::getCalories).collect(Collectors.minBy(Integer::compareTo)));
        logger.info("map maxBy: {}", dishes.stream().map(Dish::getCalories).collect(Collectors.maxBy(Integer::compareTo)));
        logger.info("map reduce min: {}", dishes.stream().map(Dish::getCalories).reduce(Integer::min));
        logger.info("map reduce max: {}", dishes.stream().map(Dish::getCalories).reduce(Integer::max));

        List<Integer> numbers = Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6);
        logger.info("findFirst {}", numbers.stream().findFirst().orElse(null));
        logger.info("findAny {}", numbers.stream().findAny().orElse(null));

        logger.info("limit collect {}", numbers.stream().limit(3).collect(Collectors.toList()));
        logger.info("skip collect {}", numbers.stream().skip(3).collect(Collectors.toList()));

        logger.info("size {}", numbers.size());
        logger.info("distinct count {}", numbers.stream().distinct().count());
        logger.info("counting: {}", Stream.of(1, 2, 3, 4, 5).collect(Collectors.counting()));

        logger.info("max {}", numbers.stream().max(Integer::compare));
        logger.info("min {}", numbers.stream().min(Integer::compare));

        logger.info("allMatch greater zero {}", numbers.stream().allMatch(i -> i > 0));
        logger.info("allMatch greater five {}", numbers.stream().allMatch(i -> i > 5));
        logger.info("anyMatch greater zero {}", numbers.stream().anyMatch(i -> i > 0));
        logger.info("anyMatch greater five {}", numbers.stream().anyMatch(i -> i > 5));
        logger.info("noneMatch greater zero {}", numbers.stream().noneMatch(i -> i > 0));
        logger.info("noneMatch greater six {}", numbers.stream().noneMatch(i -> i > 6));

        logger.info("iterate limit collect {}", Stream.iterate(0, n -> n + 2).limit(5).collect(Collectors.toList()));
        logger.info("generate limit collect {}", Stream.generate(Math::random).limit(5).collect(Collectors.toList()));

        logger.info("int set: {}", Arrays.stream(ArrayUtils.of(
                SetUtils.newHashSet(1, 2, 3),
                SetUtils.newHashSet(3, 4, 5),
                SetUtils.newHashSet(5, 6, 7),
                SetUtils.newHashSet(7, 8, 9)
        )).flatMap(Collection::stream).collect(Collectors.toSet()));

        int[] intArr = new int[]{1, 2, 3, 4, 5};
        logger.info("reduce: {}", Arrays.stream(intArr).reduce(0, Integer::sum));
        logger.info("partitioningBy: {}", Stream.of(1, 2, 3, 4, 5).collect(Collectors.partitioningBy(i -> i < 3)));
        logger.info("summarizingInt: {}", dishes.stream().collect(Collectors.summarizingInt(Dish::getCalories)));
        logger.info("mapToInt sum: {}", dishes.stream().mapToInt(Dish::getCalories).sum());

        logger.info("averagingInt: {}", dishes.stream().collect(Collectors.averagingInt(Dish::getCalories)));

        logger.info("joining: {}", dishes.stream().map(Dish::getName).collect(Collectors.joining(StringConstants.COMMA_SPACE)));
    }

    private List<String> beforeJava7(List<Dish> dishes) {
        List<Dish> lowerCaloriesDishes = ListUtils.newArrayList();
        for (Dish dish : dishes) {
            if (dish.calories < 400) {
                lowerCaloriesDishes.add(dish);
            }
        }
        lowerCaloriesDishes.sort(Comparator.comparingInt(o -> o.calories));
        List<String> lowerCaloriesDishNames = ListUtils.newArrayList();
        for (Dish dish : lowerCaloriesDishes) {
            lowerCaloriesDishNames.add(dish.name);
        }
        return lowerCaloriesDishNames;
    }

    private List<String> afterJava7(List<Dish> dishes) {
        return dishes.stream()
                .filter(d -> d.calories < 400)
                .sorted(Comparator.comparingInt(o -> o.calories))
                .map(Dish::getName)
                .collect(Collectors.toList());
    }

    private Map<Integer, List<Dish>> beforeJava8(List<Dish> dishes) {
        Map<Integer, List<Dish>> map = MapUtils.newHashMap();
        for (Dish dish : dishes) {
            map.computeIfAbsent(dish.getType(), o -> ListUtils.newArrayList()).add(dish);
        }
        return map;
    }

    private Map<Integer, List<Dish>> afterJava8(List<Dish> dishes) {
        return dishes.stream().collect(Collectors.groupingBy(Dish::getType));
    }

    @AllArgsConstructor
    @Data
    private static class Dish {
        private String name;
        private int calories;
        private int type;
    }
}