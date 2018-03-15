package com.proper.enterprise.platform.core.utils.sort

import spock.lang.Specification

class BeanComparatorSpec extends Specification {

    private class Bean implements Comparable<Bean> {
        int a
        String b
        double c

        Bean bean

        Bean(int a = 0, String b = '', double c = 0.0, Bean bean = null) {
            this.a = a
            this.b = b
            this.c = c
            this.bean = bean
        }

        @Override
        String toString() {
            "$a, $b, $c $bean\r\n"
        }

        @Override
        int compareTo(Bean o) {
            c <=> o.c
        }

        boolean equals(o) {
            if (o instanceof Bean) {
                if (getClass() != o.class) {
                    return false
                }

                Bean bean1 = (Bean) o

                if (a != bean1.a) {
                    return false
                }
                if (Double.compare(bean1.c, c) != 0) {
                    return false
                }
                if (b != bean1.b) {
                    return false
                }
                if (bean != bean1.bean) {
                    return false
                }
                if (Double.compare(c, bean1.getC()) == 0) {
                    return true
                }
                return false
            }
            return false
        }

        int hashCode() {
            int result
            long temp
            result = a
            result = 31 * result + (b != null ? b.hashCode() : 0)
            temp = c != +0.0d ? Double.doubleToLongBits(c) : 0L
            result = 31 * result + (int) (temp ^ (temp >>> 32))
            result = 31 * result + (bean != null ? bean.hashCode() : 0)
            return result
        }
    }

    def "testEq"() {
        Bean a = new Bean(2, '2', 2)
        Bean b = new Bean(2, '2', 2)
        expect:
        assert a.equals(b)
    }

    def "Order by one attribute"() {
        def list = [
            new Bean(2, '何'),
            new Bean(1, '张'),
            new Bean(3, '陈')
        ]

        def l1 = (List) list.clone()
        def l2 = (List) list.clone()

        Collections.sort(l1, new BeanComparator('a'))
        Collections.sort(l2, new BeanComparator(['b': BeanComparator.Order.DESC]))

        expect:
        l1.a == [1, 2, 3]
        l2.b == ['张', '何', '陈']
    }

    def "Order by multi attributes"() {
        def list = [
            new Bean(2, '2', 2),
            new Bean(2, '2', 1),
            new Bean(2, '2', 3),
            new Bean(2, '1', 2),
            new Bean(2, '1', 1),
            new Bean(2, '1', 3),
            new Bean(2, '3', 2),
            new Bean(2, '3', 1),
            new Bean(2, '3', 3),
            new Bean(1, '2', 2),
            new Bean(1, '2', 1),
            new Bean(1, '2', 3),
            new Bean(1, '1', 2),
            new Bean(1, '1', 1),
            new Bean(1, '1', 3),
            new Bean(1, '3', 2),
            new Bean(1, '3', 1),
            new Bean(1, '3', 3),
            new Bean(3, '2', 2),
            new Bean(3, '2', 1),
            new Bean(3, '2', 3),
            new Bean(3, '1', 2),
            new Bean(3, '1', 1),
            new Bean(3, '1', 3),
            new Bean(3, '3', 2),
            new Bean(3, '3', 1),
            new Bean(3, '3', 3)
        ]

        Collections.sort(list, new BeanComparator([
            'a': BeanComparator.Order.ASC,
            'b': BeanComparator.Order.DESC,
            'c': BeanComparator.Order.ASC
        ]))

        expect:
        [list[3].a, list[3].b, list[3].c] == [1, '2', 1]
        [list[10].a, list[10].b, list[10].c] == [2, '3', 2]
        [list[21].a, list[21].b, list[21].c] == [3, '2', 1]
    }

    def nested = [
        new Bean(2, '何', 0, new Bean(1, '2', 1.5)),
        new Bean(1, '张', 0, new Bean(1, '2', 2.3)),
        new Bean(3, '陈', 0, new Bean(1, '2', 1.499))
    ]

    def "Order by nested attributes"() {
        Collections.sort(nested, new BeanComparator('c', 'bean.c'))

        expect:
        nested[2].bean.c == 2.3d
    }

    def "Order by comparable bean"() {
        Collections.sort(nested, new BeanComparator('c', 'bean'))

        expect:
        nested[2].bean.c == 2.3d
    }

    def "Do nothing when attribute is not a string or a number"() {
        def clone = nested.clone()
        Collections.sort(nested, new BeanComparator('bean'))

        expect:
        clone == nested
    }

    def "Cover enum"() {
        expect:
        BeanComparator.Order.ASC == BeanComparator.Order.valueOf('ASC')
    }

}
