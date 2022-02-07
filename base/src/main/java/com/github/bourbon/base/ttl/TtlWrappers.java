package com.github.bourbon.base.ttl;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.ttl.spi.TtlEnhanced;
import com.github.bourbon.base.ttl.spi.TtlWrapper;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.function.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 22:55
 */
public final class TtlWrappers {

    @SuppressWarnings("unchecked")
    public static <T> Supplier<T> wrap(Supplier<T> s) {
        return ObjectUtils.defaultIfNullElseFunction(s, supplier -> BooleanUtils.defaultSupplierIfAssignableFrom(
                supplier, TtlEnhanced.class, t -> t, () -> new TtlSupplier<>(supplier)
        ));
    }

    private static class TtlSupplier<T> implements Supplier<T>, TtlWrapper<Supplier<T>>, TtlEnhanced {
        private final Supplier<T> s;
        private final Object captured = TransmittableThreadLocal.Transmitter.capture();

        private TtlSupplier(Supplier<T> s) {
            this.s = s;
        }

        @Override
        public Supplier<T> unwrap() {
            return s;
        }

        @Override
        public T get() {
            Object backup = TransmittableThreadLocal.Transmitter.replay(captured);
            try {
                return s.get();
            } finally {
                TransmittableThreadLocal.Transmitter.restore(backup);
            }
        }

        @Override
        public boolean equals(Object o) {
            return this == o || (o != null && getClass() == o.getClass()) && s.equals(((TtlSupplier) o).s);
        }

        @Override
        public int hashCode() {
            return s.hashCode();
        }

        @Override
        public String toString() {
            return getClass().getName() + StringConstants.SPACE_HYPHEN_SPACE + s;
        }
    }

    public static <T> Consumer<T> wrap(Consumer<T> c) {
        return ObjectUtils.defaultIfNullElseFunction(c, consumer -> BooleanUtils.defaultSupplierIfAssignableFrom(
                consumer, TtlEnhanced.class, t -> t, () -> new TtlConsumer<>(consumer)
        ));
    }

    private static class TtlConsumer<T> implements Consumer<T>, TtlWrapper<Consumer<T>>, TtlEnhanced {
        private final Consumer<T> c;
        private final Object captured = TransmittableThreadLocal.Transmitter.capture();

        private TtlConsumer(Consumer<T> c) {
            this.c = c;
        }

        @Override
        public void accept(T t) {
            Object backup = TransmittableThreadLocal.Transmitter.replay(captured);
            try {
                c.accept(t);
            } finally {
                TransmittableThreadLocal.Transmitter.restore(backup);
            }
        }

        @Override
        public Consumer<T> unwrap() {
            return c;
        }

        @Override
        public boolean equals(Object o) {
            return this == o || (o != null && getClass() == o.getClass()) && c.equals(((TtlConsumer) o).c);
        }

        @Override
        public int hashCode() {
            return c.hashCode();
        }

        @Override
        public String toString() {
            return getClass().getName() + StringConstants.SPACE_HYPHEN_SPACE + c;
        }
    }

    public static <T, U> BiConsumer<T, U> wrap(BiConsumer<T, U> bc) {
        return ObjectUtils.defaultIfNullElseFunction(bc, biConsumer -> BooleanUtils.defaultSupplierIfAssignableFrom(
                biConsumer, TtlEnhanced.class, t -> t, () -> new TtlBiConsumer<>(biConsumer)
        ));
    }

    private static class TtlBiConsumer<T, U> implements BiConsumer<T, U>, TtlWrapper<BiConsumer<T, U>>, TtlEnhanced {
        private final BiConsumer<T, U> bc;
        private final Object captured = TransmittableThreadLocal.Transmitter.capture();

        private TtlBiConsumer(BiConsumer<T, U> bc) {
            this.bc = bc;
        }

        @Override
        public void accept(T t, U u) {
            Object backup = TransmittableThreadLocal.Transmitter.replay(captured);
            try {
                bc.accept(t, u);
            } finally {
                TransmittableThreadLocal.Transmitter.restore(backup);
            }
        }

        @Override
        public BiConsumer<T, U> unwrap() {
            return bc;
        }

        @Override
        public boolean equals(Object o) {
            return this == o || (o != null && getClass() == o.getClass()) && bc.equals(((TtlBiConsumer) o).bc);
        }

        @Override
        public int hashCode() {
            return bc.hashCode();
        }

        @Override
        public String toString() {
            return getClass().getName() + StringConstants.SPACE_HYPHEN_SPACE + bc;
        }
    }

    public static <T, R> Function<T, R> wrap(Function<T, R> f) {
        return ObjectUtils.defaultIfNullElseFunction(f, function -> BooleanUtils.defaultSupplierIfAssignableFrom(
                function, TtlEnhanced.class, t -> t, () -> new TtlFunction<>(function)
        ));
    }

    private static class TtlFunction<T, R> implements Function<T, R>, TtlWrapper<Function<T, R>>, TtlEnhanced {
        private final Function<T, R> f;
        private final Object captured = TransmittableThreadLocal.Transmitter.capture();

        private TtlFunction(Function<T, R> f) {
            this.f = f;
        }

        @Override
        public R apply(T t) {
            Object backup = TransmittableThreadLocal.Transmitter.replay(captured);
            try {
                return f.apply(t);
            } finally {
                TransmittableThreadLocal.Transmitter.restore(backup);
            }
        }

        @Override
        public Function<T, R> unwrap() {
            return f;
        }

        @Override
        public boolean equals(Object o) {
            return this == o || (o != null && getClass() == o.getClass()) && f.equals(((TtlFunction) o).f);
        }

        @Override
        public int hashCode() {
            return f.hashCode();
        }

        @Override
        public String toString() {
            return getClass().getName() + StringConstants.SPACE_HYPHEN_SPACE + f;
        }
    }

    public static <T, U, R> BiFunction<T, U, R> wrap(BiFunction<T, U, R> bf) {
        return ObjectUtils.defaultIfNullElseFunction(bf, biFunction -> BooleanUtils.defaultSupplierIfAssignableFrom(
                biFunction, TtlEnhanced.class, t -> t, () -> new TtlBiFunction<>(biFunction)
        ));
    }

    private static class TtlBiFunction<T, U, R> implements BiFunction<T, U, R>, TtlWrapper<BiFunction<T, U, R>>, TtlEnhanced {
        private final BiFunction<T, U, R> bf;
        private final Object captured = TransmittableThreadLocal.Transmitter.capture();

        private TtlBiFunction(BiFunction<T, U, R> bf) {
            this.bf = bf;
        }

        @Override
        public R apply(T t, U u) {
            Object backup = TransmittableThreadLocal.Transmitter.replay(captured);
            try {
                return bf.apply(t, u);
            } finally {
                TransmittableThreadLocal.Transmitter.restore(backup);
            }
        }

        @Override
        public BiFunction<T, U, R> unwrap() {
            return bf;
        }

        @Override
        public boolean equals(Object o) {
            return this == o || (o != null && getClass() == o.getClass()) && bf.equals(((TtlBiFunction) o).bf);
        }

        @Override
        public int hashCode() {
            return bf.hashCode();
        }

        @Override
        public String toString() {
            return getClass().getName() + StringConstants.SPACE_HYPHEN_SPACE + bf;
        }
    }

    private TtlWrappers() {
    }
}