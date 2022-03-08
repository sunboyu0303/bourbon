package com.github.bourbon.base.lambda;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/19 18:44
 */
public class StreamTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {
        List<Integer> list = Arrays.asList(7, 6, 9, 3, 8, 2, 1);
        // 匹配第一个
        Optional<Integer> findFirst = list.stream().filter(x -> x > 6).findFirst();
        // 匹配任意（适用于并行流）
        Optional<Integer> findAny = list.parallelStream().filter(x -> x > 6).findAny();
        // 是否包含符合特定条件的元素
        boolean anyMatch = list.stream().anyMatch(x -> x < 6);
        logger.info("匹配第一个值：{}", findFirst.get());
        logger.info("匹配任意一个值：{}", findAny.get());
        logger.info("是否存在大于6的值：{}", anyMatch);

        List<Person> personList = new ArrayList<>();
        personList.add(new Person("Tom", 8900, 23, "male", "New York"));
        personList.add(new Person("Jack", 7000, 25, "male", "Washington"));
        personList.add(new Person("Lily", 7800, 21, "female", "Washington"));
        personList.add(new Person("Anni", 8200, 24, "female", "New York"));
        personList.add(new Person("Owen", 9500, 25, "male", "New York"));
        personList.add(new Person("Alisa", 7900, 26, "female", "New York"));

        logger.info("高于8000的员工姓名：{}", personList.stream().filter(x -> x.getSalary() > 8000).map(Person::getName).collect(Collectors.toList()));
        logger.info("最长的字符串：{}", Arrays.asList("adnm", "admmt", "pot", "xbangd", "weoujgsd").stream().max(Comparator.comparing(String::length)).get());

        logger.info("自然排序的最大值：{}", Arrays.asList(7, 6, 9, 4, 11, 6).stream().max(Integer::compareTo).get());
        logger.info("自定义排序的最大值：{}", Arrays.asList(7, 6, 9, 4, 11, 6).stream().max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        }).get());

        logger.info("员工工资最大值：{}", personList.stream().max(Comparator.comparingInt(Person::getSalary)).get().getSalary());
        logger.info("list中大于6的元素个数：{}", Arrays.asList(7, 6, 4, 8, 2, 11, 9).stream().filter(x -> x > 6).count());

        logger.info("每个元素大写：{}", Stream.of("abcd", "bcdd", "defde", "fTr").map(String::toUpperCase).collect(Collectors.toList()));
        logger.info("每个元素+3：{}", Arrays.asList(1, 3, 5, 7, 9, 11).stream().map(x -> x + 3).collect(Collectors.toList()));

        // 不改变原来员工集合的方式
        List<Person> personListNew = personList.stream().map(person -> {
            Person personNew = new Person(person.getName(), 0, 0, null, null);
            personNew.setSalary(person.getSalary() + 10000);
            return personNew;
        }).collect(Collectors.toList());
        logger.info("一次改动前：{} --> {}", personList.get(0).getName(), personList.get(0).getSalary());
        logger.info("一次改动后：{} --> {}", personListNew.get(0).getName(), personListNew.get(0).getSalary());

        // 改变原来员工集合的方式
        List<Person> personListNew2 = personList.stream().map(person -> {
            person.setSalary(person.getSalary() + 10000);
            return person;
        }).collect(Collectors.toList());
        logger.info("二次改动前：{} --> {}", personList.get(0).getName(), personListNew.get(0).getSalary());
        logger.info("二次改动后：{} --> {}", personListNew2.get(0).getName(), personListNew.get(0).getSalary());

        logger.info("处理前的集合：{}", Arrays.asList("m-k-l-a", "1-3-5-7"));
        logger.info("处理后的集合：{}", Arrays.asList("m-k-l-a", "1-3-5-7").stream().flatMap(s -> Arrays.stream(s.split("-"))).collect(Collectors.toList()));

        logger.info("list求和：" + Arrays.asList(1, 3, 2, 8, 11, 4).stream().reduce((x, y) -> x + y).get() + ","
                + Arrays.asList(1, 3, 2, 8, 11, 4).stream().reduce(Integer::sum).get() + ","
                + Arrays.asList(1, 3, 2, 8, 11, 4).stream().reduce(0, Integer::sum));
        logger.info("list求积：" + Arrays.asList(1, 3, 2, 8, 11, 4).stream().reduce((x, y) -> x * y).get());
        logger.info("list求最大值：" + Arrays.asList(1, 3, 2, 8, 11, 4).stream().reduce((x, y) -> x > y ? x : y).get() + ","
                + Arrays.asList(1, 3, 2, 8, 11, 4).stream().reduce(0, Integer::max));

        personList = new ArrayList<>();
        personList.add(new Person("Tom", 8900, 23, "male", "New York"));
        personList.add(new Person("Jack", 7000, 25, "male", "Washington"));
        personList.add(new Person("Lily", 7800, 21, "female", "Washington"));
        personList.add(new Person("Anni", 8200, 24, "female", "New York"));
        personList.add(new Person("Owen", 9500, 25, "male", "New York"));
        personList.add(new Person("Alisa", 7900, 26, "female", "New York"));

        logger.info("工资之和：" + personList.stream().map(Person::getSalary).reduce(Integer::sum).get() + ","
                + personList.stream().reduce(0, (sum, p) -> sum += p.getSalary(), (sum1, sum2) -> sum1 + sum2) + ","
                + personList.stream().reduce(0, (sum, p) -> sum += p.getSalary(), Integer::sum));

        logger.info("最高工资：" + personList.stream().reduce(0, (max, p) -> max > p.getSalary() ? max : p.getSalary(), Integer::max) + ","
                + personList.stream().reduce(0, (max, p) -> max > p.getSalary() ? max : p.getSalary(), (max1, max2) -> max1 > max2 ? max1 : max2));

        personList = new ArrayList<>();
        personList.add(new Person("Tom", 8900, 23, "male", "New York"));
        personList.add(new Person("Jack", 7000, 25, "male", "Washington"));
        personList.add(new Person("Lily", 7800, 21, "female", "Washington"));
        personList.add(new Person("Anni", 8200, 24, "female", "New York"));

        logger.info("toList: {}", Arrays.asList(1, 6, 3, 4, 6, 7, 9, 6, 20).stream().filter(x -> x % 2 == 0).collect(Collectors.toList()));
        logger.info("toSet: {}", Arrays.asList(1, 6, 3, 4, 6, 7, 9, 6, 20).stream().filter(x -> x % 2 == 0).collect(Collectors.toSet()));
        logger.info("toMap: {}", personList.stream().filter(p -> p.getSalary() > 8000).collect(Collectors.toMap(Person::getName, p -> p)));

        personList = new ArrayList<>();
        personList.add(new Person("Tom", 8900, 23, "male", "New York"));
        personList.add(new Person("Jack", 7000, 25, "male", "Washington"));
        personList.add(new Person("Lily", 7800, 21, "female", "Washington"));

        logger.info("员工总数：{}", personList.stream().collect(Collectors.counting()));
        logger.info("员工平均工资：{}", personList.stream().collect(Collectors.averagingDouble(Person::getSalary)));
        logger.info("员工最高工资：{}", personList.stream().map(Person::getSalary).collect(Collectors.maxBy(Integer::compare)));
        logger.info("员工工资总和：{}", personList.stream().collect(Collectors.summingInt(Person::getSalary)));
        logger.info("员工工资所有统计：{}", personList.stream().collect(Collectors.summarizingDouble(Person::getSalary)));

        personList = new ArrayList<>();
        personList.add(new Person("Tom", 8900, 23, "male", "New York"));
        personList.add(new Person("Jack", 7000, 25, "male", "Washington"));
        personList.add(new Person("Lily", 7800, 21, "female", "Washington"));
        personList.add(new Person("Anni", 8200, 24, "female", "New York"));
        personList.add(new Person("Owen", 9500, 25, "male", "New York"));
        personList.add(new Person("Alisa", 7900, 26, "female", "New York"));

        logger.info("员工按薪资是否大于8000分组情况：{}", personList.stream().collect(Collectors.partitioningBy(x -> x.getSalary() > 8000)));
        logger.info("员工按性别分组情况：{}", personList.stream().collect(Collectors.groupingBy(Person::getSex)));
        logger.info("员工按性别、地区：{}", personList.stream().collect(Collectors.groupingBy(Person::getSex, Collectors.groupingBy(Person::getArea))));

        personList = new ArrayList<>();
        personList.add(new Person("Tom", 8900, 23, "male", "New York"));
        personList.add(new Person("Jack", 7000, 25, "male", "Washington"));
        personList.add(new Person("Lily", 7800, 21, "female", "Washington"));

        logger.info("所有员工的姓名：{}", personList.stream().map(p -> p.getName()).collect(Collectors.joining(",")));
        logger.info("拼接后的字符串：{}", Arrays.asList("A", "B", "C").stream().collect(Collectors.joining("-")));

        personList = new ArrayList<>();
        personList.add(new Person("Tom", 8900, 23, "male", "New York"));
        personList.add(new Person("Jack", 7000, 25, "male", "Washington"));
        personList.add(new Person("Lily", 7800, 21, "female", "Washington"));

        logger.info("员工扣税薪资总和：{}", personList.stream().collect(Collectors.reducing(0, Person::getSalary, (i, j) -> (i + j - 5000))));
        logger.info("员工薪资总和：{}", personList.stream().map(Person::getSalary).reduce(Integer::sum).get());

        personList = new ArrayList<>();
        personList.add(new Person("Sherry", 9000, 24, "female", "New York"));
        personList.add(new Person("Tom", 8900, 22, "male", "Washington"));
        personList.add(new Person("Jack", 9000, 25, "male", "Washington"));
        personList.add(new Person("Lily", 8800, 26, "male", "New York"));
        personList.add(new Person("Alisa", 9000, 26, "female", "New York"));

        logger.info("按工资升序排序：{}", personList.stream().sorted(Comparator.comparing(Person::getSalary)).map(Person::getName).collect(Collectors.toList()));
        logger.info("按工资降序排序：{}", personList.stream().sorted(Comparator.comparing(Person::getSalary).reversed()).map(Person::getName).collect(Collectors.toList()));
        logger.info("先按工资再按年龄升序排序：{}", personList.stream().sorted(Comparator.comparing(Person::getSalary).thenComparing(Person::getAge)).map(Person::getName).collect(Collectors.toList()));
        logger.info("先按工资再按年龄自定义降序排序：{}", personList.stream().sorted((p1, p2) -> {
            if (p1.getSalary() == p2.getSalary()) {
                return p2.getAge() - p1.getAge();
            } else {
                return p2.getSalary() - p1.getSalary();
            }
        }).map(Person::getName).collect(Collectors.toList()));

        logger.info("流合并：{}", Stream.concat(Stream.of("a", "b", "c", "d"), Stream.of("d", "e", "f", "g")).distinct().collect(Collectors.toList()));
        logger.info("limit：{}", Stream.iterate(1, x -> x + 2).limit(10).collect(Collectors.toList()));
        logger.info("skip：{}", Stream.iterate(1, x -> x + 2).skip(1).limit(5).collect(Collectors.toList()));
    }

    @Test
    public void testSum() {
        byte[][] byteArr = new byte[4][];
        byteArr[0] = new byte[]{0};
        byteArr[1] = new byte[]{1, 2};
        byteArr[2] = new byte[]{3, 4, 5};
        byteArr[3] = new byte[]{6, 7, 8, 9};

        int len = 0;
        for (byte[] bytes : byteArr) {
            len += bytes.length;
        }
        logger.info("len: {}", len);
        logger.info("stream len: {}", Arrays.stream(byteArr).map(bytes -> bytes.length).reduce(Integer::sum).get());
        logger.info("stream summarizingInt len: {}", Arrays.stream(byteArr).collect(Collectors.summarizingInt(bytes -> bytes.length)).getSum());
        logger.info("stream summingInt len: {}", Arrays.stream(byteArr).collect(Collectors.summingInt(bytes -> bytes.length)));
        logger.info("stream mapToInt sum len: {}", Arrays.stream(byteArr).mapToInt(bytes -> bytes.length).sum());
    }

    @AllArgsConstructor
    @Data
    private static class Person {
        private String name; // 姓名
        private int salary; // 薪资
        private int age; // 年龄
        private String sex; //性别
        private String area; // 地区
    }
}