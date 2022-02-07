package com.github.bourbon.springframework.boot.context.properties.source;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.*;

import java.util.List;
import java.util.function.Function;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/20 15:39
 */
public final class ConfigurationPropertyName implements Comparable<ConfigurationPropertyName> {

    public static final ConfigurationPropertyName EMPTY = new ConfigurationPropertyName(Elements.EMPTY);

    private final Elements elements;

    private final CharSequence[] uniformElements;

    private String string;

    private int hashCode;

    private ConfigurationPropertyName(Elements elements) {
        this.elements = elements;
        this.uniformElements = new CharSequence[elements.getSize()];
    }

    public boolean isEmpty() {
        return elements.getSize() == 0;
    }

    public boolean isLastElementIndexed() {
        return BooleanUtils.defaultIfPredicate(getNumberOfElements(), s -> s > 0, s -> isIndexed(s - 1), false);
    }

    public boolean hasIndexedElement() {
        for (int i = 0; i < getNumberOfElements(); i++) {
            if (isIndexed(i)) {
                return true;
            }
        }
        return false;
    }

    boolean isIndexed(int elementIndex) {
        return elements.getType(elementIndex).isIndexed();
    }

    public boolean isNumericIndex(int elementIndex) {
        return elements.getType(elementIndex) == ElementType.NUMERICALLY_INDEXED;
    }

    public String getLastElement(Form form) {
        return BooleanUtils.defaultIfPredicate(getNumberOfElements(), s -> s != 0, s -> getElement(s - 1, form), StringConstants.EMPTY);
    }

    public String getElement(int elementIndex, Form form) {
        CharSequence element = elements.get(elementIndex);
        ElementType type = elements.getType(elementIndex);
        if (type.isIndexed()) {
            return element.toString();
        }
        if (form == Form.ORIGINAL) {
            if (type != ElementType.NON_UNIFORM) {
                return element.toString();
            }
            return convertToOriginalForm(element).toString();
        }
        if (form == Form.DASHED) {
            if (type == ElementType.UNIFORM || type == ElementType.DASHED) {
                return element.toString();
            }
            return convertToDashedElement(element).toString();
        }
        CharSequence uniformElement = uniformElements[elementIndex];
        if (uniformElement == null) {
            uniformElement = BooleanUtils.defaultIfFalse(type != ElementType.UNIFORM, () -> convertToUniformElement(element), element);
            uniformElements[elementIndex] = uniformElement.toString();
        }
        return uniformElement.toString();
    }

    private CharSequence convertToOriginalForm(CharSequence element) {
        return convertElement(element, false, (ch, i) -> ch == CharConstants.UNDERLINE || ElementsParser.isValidChar(Character.toLowerCase(ch), i));
    }

    private CharSequence convertToDashedElement(CharSequence element) {
        return convertElement(element, true, ElementsParser::isValidChar);
    }

    private CharSequence convertToUniformElement(CharSequence element) {
        return convertElement(element, true, (ch, i) -> ElementsParser.isAlphaNumeric(ch));
    }

    private CharSequence convertElement(CharSequence element, boolean lowercase, ElementCharPredicate filter) {
        StringBuilder result = new StringBuilder(element.length());
        for (int i = 0; i < element.length(); i++) {
            char ch = lowercase ? Character.toLowerCase(element.charAt(i)) : element.charAt(i);
            if (filter.test(ch, i)) {
                result.append(ch);
            }
        }
        return result;
    }

    public int getNumberOfElements() {
        return elements.getSize();
    }

    public ConfigurationPropertyName append(String suffix) {
        return BooleanUtils.defaultIfPredicate(suffix, CharSequenceUtils::isNotEmpty, s -> new ConfigurationPropertyName(elements.append(probablySingleElementOf(s))), this);
    }

    public ConfigurationPropertyName append(ConfigurationPropertyName suffix) {
        return ObjectUtils.defaultIfNullElseFunction(suffix, s -> new ConfigurationPropertyName(elements.append(s.elements)), this);
    }

    public ConfigurationPropertyName getParent() {
        return BooleanUtils.defaultIfPredicate(getNumberOfElements(), n -> n > 1, n -> chop(n - 1), EMPTY);
    }

    public ConfigurationPropertyName chop(int size) {
        return BooleanUtils.defaultIfFalse(size < getNumberOfElements(), () -> new ConfigurationPropertyName(elements.chop(size)), this);
    }

    public ConfigurationPropertyName subName(int offset) {
        if (offset == 0) {
            return this;
        }
        if (offset == getNumberOfElements()) {
            return EMPTY;
        }
        if (offset < 0 || offset > getNumberOfElements()) {
            throw new IndexOutOfBoundsException("Offset: " + offset + ", NumberOfElements: " + getNumberOfElements());
        }
        return new ConfigurationPropertyName(elements.subElements(offset));
    }

    public boolean isParentOf(ConfigurationPropertyName name) {
        Assert.notNull(name, "Name must not be null");
        if (getNumberOfElements() != name.getNumberOfElements() - 1) {
            return false;
        }
        return isAncestorOf(name);
    }

    public boolean isAncestorOf(ConfigurationPropertyName name) {
        Assert.notNull(name, "Name must not be null");
        if (getNumberOfElements() >= name.getNumberOfElements()) {
            return false;
        }
        return elementsEqual(name);
    }

    @Override
    public int compareTo(ConfigurationPropertyName o) {
        return compare(this, o);
    }

    private int compare(ConfigurationPropertyName n1, ConfigurationPropertyName n2) {
        int l1 = n1.getNumberOfElements();
        int l2 = n2.getNumberOfElements();
        int i1 = 0;
        int i2 = 0;
        while (i1 < l1 || i2 < l2) {
            try {
                ElementType type1 = (i1 < l1) ? n1.elements.getType(i1) : null;
                ElementType type2 = (i2 < l2) ? n2.elements.getType(i2) : null;
                String e1 = (i1 < l1) ? n1.getElement(i1++, Form.UNIFORM) : null;
                String e2 = (i2 < l2) ? n2.getElement(i2++, Form.UNIFORM) : null;
                int result = compare(e1, type1, e2, type2);
                if (result != 0) {
                    return result;
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
        return 0;
    }

    private int compare(String e1, ElementType type1, String e2, ElementType type2) {
        if (e1 == null) {
            return -1;
        }
        if (e2 == null) {
            return 1;
        }
        int result = Boolean.compare(type2.isIndexed(), type1.isIndexed());
        if (result != 0) {
            return result;
        }
        if (type1 == ElementType.NUMERICALLY_INDEXED && type2 == ElementType.NUMERICALLY_INDEXED) {
            return Long.compare(Long.parseLong(e1), Long.parseLong(e2));
        }
        return e1.compareTo(e2);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        ConfigurationPropertyName other = (ConfigurationPropertyName) obj;
        if (getNumberOfElements() != other.getNumberOfElements()) {
            return false;
        }
        if (elements.canShortcutWithSource(ElementType.UNIFORM) && other.elements.canShortcutWithSource(ElementType.UNIFORM)) {
            return toString().equals(other.toString());
        }
        return elementsEqual(other);
    }

    private boolean elementsEqual(ConfigurationPropertyName name) {
        for (int i = elements.getSize() - 1; i >= 0; i--) {
            if (elementDiffers(elements, name.elements, i)) {
                return false;
            }
        }
        return true;
    }

    private boolean elementDiffers(Elements e1, Elements e2, int i) {
        ElementType type1 = e1.getType(i);
        ElementType type2 = e2.getType(i);
        if (type1.allowsFastEqualityCheck() && type2.allowsFastEqualityCheck()) {
            return !fastElementEquals(e1, e2, i);
        }
        if (type1.allowsDashIgnoringEqualityCheck() && type2.allowsDashIgnoringEqualityCheck()) {
            return !dashIgnoringElementEquals(e1, e2, i);
        }
        return !defaultElementEquals(e1, e2, i);
    }

    private boolean fastElementEquals(Elements e1, Elements e2, int i) {
        int length1 = e1.getLength(i);
        int length2 = e2.getLength(i);
        if (length1 == length2) {
            int i1 = 0;
            while (length1-- != 0) {
                char ch1 = e1.charAt(i, i1);
                char ch2 = e2.charAt(i, i1);
                if (ch1 != ch2) {
                    return false;
                }
                i1++;
            }
            return true;
        }
        return false;
    }

    private boolean dashIgnoringElementEquals(Elements e1, Elements e2, int i) {
        int l1 = e1.getLength(i);
        int l2 = e2.getLength(i);
        int i1 = 0;
        int i2 = 0;
        while (i1 < l1) {
            if (i2 >= l2) {
                return false;
            }
            char ch1 = e1.charAt(i, i1);
            char ch2 = e2.charAt(i, i2);
            if (ch1 == CharConstants.HYPHEN) {
                i1++;
            } else if (ch2 == CharConstants.HYPHEN) {
                i2++;
            } else if (ch1 != ch2) {
                return false;
            } else {
                i1++;
                i2++;
            }
        }
        if (i2 < l2) {
            if (e2.getType(i).isIndexed()) {
                return false;
            }
            do {
                char ch2 = e2.charAt(i, i2++);
                if (ch2 != CharConstants.HYPHEN) {
                    return false;
                }
            }
            while (i2 < l2);
        }
        return true;
    }

    private boolean defaultElementEquals(Elements e1, Elements e2, int i) {
        int l1 = e1.getLength(i);
        int l2 = e2.getLength(i);
        boolean indexed1 = e1.getType(i).isIndexed();
        boolean indexed2 = e2.getType(i).isIndexed();
        int i1 = 0;
        int i2 = 0;
        while (i1 < l1) {
            if (i2 >= l2) {
                return false;
            }
            char ch1 = indexed1 ? e1.charAt(i, i1) : Character.toLowerCase(e1.charAt(i, i1));
            char ch2 = indexed2 ? e2.charAt(i, i2) : Character.toLowerCase(e2.charAt(i, i2));
            if (!indexed1 && !ElementsParser.isAlphaNumeric(ch1)) {
                i1++;
            } else if (!indexed2 && !ElementsParser.isAlphaNumeric(ch2)) {
                i2++;
            } else if (ch1 != ch2) {
                return false;
            } else {
                i1++;
                i2++;
            }
        }
        if (i2 < l2) {
            if (indexed2) {
                return false;
            }
            do {
                char ch2 = Character.toLowerCase(e2.charAt(i, i2++));
                if (ElementsParser.isAlphaNumeric(ch2)) {
                    return false;
                }
            }
            while (i2 < l2);
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = this.hashCode;
        if (hashCode == 0 && elements.getSize() != 0) {
            for (int elementIndex = 0; elementIndex < elements.getSize(); elementIndex++) {
                int elementHashCode = 0;
                boolean indexed = elements.getType(elementIndex).isIndexed();
                int length = elements.getLength(elementIndex);
                for (int i = 0; i < length; i++) {
                    char ch = elements.charAt(elementIndex, i);
                    if (!indexed) {
                        ch = Character.toLowerCase(ch);
                    }
                    if (ElementsParser.isAlphaNumeric(ch)) {
                        elementHashCode = 31 * elementHashCode + ch;
                    }
                }
                hashCode = 31 * hashCode + elementHashCode;
            }
            this.hashCode = hashCode;
        }
        return hashCode;
    }

    @Override
    public String toString() {
        if (string == null) {
            string = buildToString();
        }
        return string;
    }

    private String buildToString() {
        if (elements.canShortcutWithSource(ElementType.UNIFORM, ElementType.DASHED)) {
            return elements.getSource().toString();
        }
        int size = getNumberOfElements();
        StringBuilder result = new StringBuilder(size * 8);
        for (int i = 0; i < size; i++) {
            boolean indexed = isIndexed(i);
            if (result.length() > 0 && !indexed) {
                result.append(CharConstants.DOT);
            }
            if (indexed) {
                result.append(CharConstants.LEFT_BRACKETS);
                result.append(getElement(i, Form.ORIGINAL));
                result.append(CharConstants.RIGHT_BRACKETS);
            } else {
                result.append(getElement(i, Form.DASHED));
            }
        }
        return result.toString();
    }

    public static boolean isValid(CharSequence name) {
        return of(name, true) != null;
    }

    public static ConfigurationPropertyName of(CharSequence name) {
        return of(name, false);
    }

    public static ConfigurationPropertyName ofIfValid(CharSequence name) {
        return of(name, true);
    }

    static ConfigurationPropertyName of(CharSequence name, boolean returnNullIfInvalid) {
        return ObjectUtils.defaultIfNullElseFunction(elementsOf(name, returnNullIfInvalid), ConfigurationPropertyName::new);
    }

    private static Elements probablySingleElementOf(CharSequence name) {
        return elementsOf(name, false, 1);
    }

    private static Elements elementsOf(CharSequence name, boolean returnNullIfInvalid) {
        return elementsOf(name, returnNullIfInvalid, ElementsParser.DEFAULT_CAPACITY);
    }

    private static Elements elementsOf(CharSequence name, boolean returnNullIfInvalid, int parserCapacity) {
        if (name == null) {
            Assert.isTrue(returnNullIfInvalid, "Name must not be null");
            return null;
        }
        if (name.length() == 0) {
            return Elements.EMPTY;
        }
        if (name.charAt(0) == CharConstants.DOT || name.charAt(name.length() - 1) == CharConstants.DOT) {
            if (returnNullIfInvalid) {
                return null;
            }
            throw new InvalidConfigurationPropertyNameException(name, ListUtils.newArrayList(CharConstants.DOT));
        }
        Elements elements = new ElementsParser(name, CharConstants.DOT, parserCapacity).parse();
        for (int i = 0; i < elements.getSize(); i++) {
            if (elements.getType(i) == ElementType.NON_UNIFORM) {
                if (returnNullIfInvalid) {
                    return null;
                }
                throw new InvalidConfigurationPropertyNameException(name, getInvalidChars(elements, i));
            }
        }
        return elements;
    }

    private static List<Character> getInvalidChars(Elements elements, int index) {
        List<Character> invalidChars = ListUtils.newArrayList();
        for (int charIndex = 0; charIndex < elements.getLength(index); charIndex++) {
            char ch = elements.charAt(index, charIndex);
            if (!ElementsParser.isValidChar(ch, charIndex)) {
                invalidChars.add(ch);
            }
        }
        return invalidChars;
    }

    public static ConfigurationPropertyName adapt(CharSequence name, char separator) {
        return adapt(name, separator, null);
    }

    static ConfigurationPropertyName adapt(CharSequence name, char separator, Function<CharSequence, CharSequence> function) {
        Assert.notNull(name, "Name must not be null");
        if (name.length() == 0) {
            return EMPTY;
        }
        return BooleanUtils.defaultSupplierIfPredicate(new ElementsParser(name, separator).parse(function), e -> e.getSize() != 0, ConfigurationPropertyName::new, () -> EMPTY);
    }

    public enum Form {
        ORIGINAL, DASHED, UNIFORM
    }

    private static class Elements {

        private static final int[] NO_POSITION = {};

        private static final ElementType[] NO_TYPE = {};

        static final Elements EMPTY = new Elements(StringConstants.EMPTY, 0, NO_POSITION, NO_POSITION, NO_TYPE, null);

        private final CharSequence source;

        private final int size;

        private final int[] start;

        private final int[] end;

        private final ElementType[] type;

        private final CharSequence[] resolved;

        private Elements(CharSequence source, int size, int[] start, int[] end, ElementType[] type, CharSequence[] resolved) {
            super();
            this.source = source;
            this.size = size;
            this.start = start;
            this.end = end;
            this.type = type;
            this.resolved = resolved;
        }

        Elements append(Elements additional) {
            int size = this.size + additional.size;
            ElementType[] type = new ElementType[size];
            System.arraycopy(this.type, 0, type, 0, this.size);
            System.arraycopy(additional.type, 0, type, this.size, additional.size);
            CharSequence[] resolved = newResolved(size);
            for (int i = 0; i < additional.size; i++) {
                resolved[this.size + i] = additional.get(i);
            }
            return new Elements(this.source, size, this.start, this.end, type, resolved);
        }

        Elements chop(int size) {
            return new Elements(source, size, start, end, type, newResolved(size));
        }

        Elements subElements(int offset) {
            int size = this.size - offset;
            CharSequence[] resolved = newResolved(size);
            int[] start = new int[size];
            System.arraycopy(this.start, offset, start, 0, size);
            int[] end = new int[size];
            System.arraycopy(this.end, offset, end, 0, size);
            ElementType[] type = new ElementType[size];
            System.arraycopy(this.type, offset, type, 0, size);
            return new Elements(this.source, size, start, end, type, resolved);
        }

        private CharSequence[] newResolved(int size) {
            CharSequence[] resolved = new CharSequence[size];
            if (this.resolved != null) {
                System.arraycopy(this.resolved, 0, resolved, 0, Math.min(size, this.size));
            }
            return resolved;
        }

        int getSize() {
            return size;
        }

        CharSequence get(int index) {
            if (resolved != null && resolved[index] != null) {
                return resolved[index];
            }
            return source.subSequence(start[index], end[index]);
        }

        int getLength(int index) {
            if (resolved != null && resolved[index] != null) {
                return resolved[index].length();
            }
            return end[index] - start[index];
        }

        char charAt(int index, int charIndex) {
            if (resolved != null && resolved[index] != null) {
                return resolved[index].charAt(charIndex);
            }
            return source.charAt(start[index] + charIndex);
        }

        ElementType getType(int index) {
            return type[index];
        }

        CharSequence getSource() {
            return source;
        }

        boolean canShortcutWithSource(ElementType requiredType) {
            return canShortcutWithSource(requiredType, requiredType);
        }

        boolean canShortcutWithSource(ElementType requiredType, ElementType alternativeType) {
            if (resolved != null) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                ElementType elementType = type[i];
                if (elementType != requiredType && elementType != alternativeType) {
                    return false;
                }
                if (i > 0 && end[i - 1] + 1 != start[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    private static class ElementsParser {

        private static final int DEFAULT_CAPACITY = 6;

        private final CharSequence source;

        private final char separator;

        private int size;

        private int[] start;

        private int[] end;

        private ElementType[] type;

        private CharSequence[] resolved;

        ElementsParser(CharSequence source, char separator) {
            this(source, separator, DEFAULT_CAPACITY);
        }

        ElementsParser(CharSequence source, char separator, int capacity) {
            this.source = source;
            this.separator = separator;
            this.start = new int[capacity];
            this.end = new int[capacity];
            this.type = new ElementType[capacity];
        }

        Elements parse() {
            return parse(null);
        }

        Elements parse(Function<CharSequence, CharSequence> valueProcessor) {
            int length = source.length();
            int openBracketCount = 0;
            int start = 0;
            ElementType type = ElementType.EMPTY;
            for (int i = 0; i < length; i++) {
                char ch = this.source.charAt(i);
                if (ch == CharConstants.LEFT_BRACKETS) {
                    if (openBracketCount == 0) {
                        add(start, i, type, valueProcessor);
                        start = i + 1;
                        type = ElementType.NUMERICALLY_INDEXED;
                    }
                    openBracketCount++;
                } else if (ch == CharConstants.RIGHT_BRACKETS) {
                    openBracketCount--;
                    if (openBracketCount == 0) {
                        add(start, i, type, valueProcessor);
                        start = i + 1;
                        type = ElementType.EMPTY;
                    }
                } else if (!type.isIndexed() && ch == this.separator) {
                    add(start, i, type, valueProcessor);
                    start = i + 1;
                    type = ElementType.EMPTY;
                } else {
                    type = updateType(type, ch, i - start);
                }
            }
            if (openBracketCount != 0) {
                type = ElementType.NON_UNIFORM;
            }
            add(start, length, type, valueProcessor);
            return new Elements(source, this.size, this.start, this.end, this.type, resolved);
        }

        private ElementType updateType(ElementType existingType, char ch, int index) {
            if (existingType.isIndexed()) {
                if (existingType == ElementType.NUMERICALLY_INDEXED && !isNumeric(ch)) {
                    return ElementType.INDEXED;
                }
                return existingType;
            }
            if (existingType == ElementType.EMPTY && isValidChar(ch, index)) {
                return index == 0 ? ElementType.UNIFORM : ElementType.NON_UNIFORM;
            }
            if (existingType == ElementType.UNIFORM && ch == CharConstants.HYPHEN) {
                return ElementType.DASHED;
            }
            if (!isValidChar(ch, index)) {
                if (existingType == ElementType.EMPTY && !isValidChar(Character.toLowerCase(ch), index)) {
                    return ElementType.EMPTY;
                }
                return ElementType.NON_UNIFORM;
            }
            return existingType;
        }

        private void add(int start, int end, ElementType type, Function<CharSequence, CharSequence> valueProcessor) {
            if ((end - start) < 1 || type == ElementType.EMPTY) {
                return;
            }
            if (this.start.length == size) {
                this.start = expand(this.start);
                this.end = expand(this.end);
                this.type = expand(this.type);
                this.resolved = expand(this.resolved);
            }
            if (valueProcessor != null) {
                if (this.resolved == null) {
                    this.resolved = new CharSequence[this.start.length];
                }
                CharSequence resolved = valueProcessor.apply(source.subSequence(start, end));
                Elements resolvedElements = new ElementsParser(resolved, CharConstants.DOT).parse();
                Assert.isTrue(resolvedElements.getSize() == 1, "Resolved element must not contain multiple elements");
                this.resolved[size] = resolvedElements.get(0);
                type = resolvedElements.getType(0);
            }
            this.start[size] = start;
            this.end[size] = end;
            this.type[size] = type;
            this.size++;
        }

        private int[] expand(int[] src) {
            return PrimitiveArrayUtils.expand(src, DEFAULT_CAPACITY);
        }

        private ElementType[] expand(ElementType[] src) {
            return ArrayUtils.expand(src, DEFAULT_CAPACITY);
        }

        private CharSequence[] expand(CharSequence[] src) {
            return ArrayUtils.expand(src, DEFAULT_CAPACITY);
        }

        static boolean isValidChar(char ch, int index) {
            return isAlpha(ch) || isNumeric(ch) || (index != 0 && ch == CharConstants.HYPHEN);
        }

        static boolean isAlphaNumeric(char ch) {
            return isAlpha(ch) || isNumeric(ch);
        }

        private static boolean isAlpha(char ch) {
            return CharUtils.isLetterLower(ch);
        }

        private static boolean isNumeric(char ch) {
            return CharUtils.isNumber(ch);
        }
    }

    private enum ElementType {
        EMPTY(false),
        UNIFORM(false),
        DASHED(false),
        NON_UNIFORM(false),
        INDEXED(true),
        NUMERICALLY_INDEXED(true);

        private final boolean indexed;

        ElementType(boolean indexed) {
            this.indexed = indexed;
        }

        public boolean isIndexed() {
            return indexed;
        }

        public boolean allowsFastEqualityCheck() {
            return this == UNIFORM || this == NUMERICALLY_INDEXED;
        }

        public boolean allowsDashIgnoringEqualityCheck() {
            return allowsFastEqualityCheck() || this == DASHED;
        }
    }

    private interface ElementCharPredicate {

        boolean test(char ch, int index);
    }
}