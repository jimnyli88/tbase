package cn.com.ut.core.common.jdbc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Pageable;

import cn.com.ut.core.common.constant.EnumConstant;

/**
 * @author wuxiaohua
 * @since 2018/7/16
 */
public class PageInfoBuilder {

    private EntityManager entityManager;
    private StringBuilder selectNames = new StringBuilder();
    private String countName;
    private StringBuilder querySQL = new StringBuilder(" SELECT ");
    private StringBuilder whereSQL = new StringBuilder();
    private StringBuilder countSQL = new StringBuilder(" SELECT ");
    private StringBuilder orderBySQL = new StringBuilder();
    private StringBuilder groupBySQL = new StringBuilder();
    private StringBuilder fromSQL = new StringBuilder();
    private List<WhereCondition> whereConditions = new ArrayList<>();
    private List<SortCondition> sortConditions = new ArrayList<>();
    private List<QueryParameter> queryParameters = new ArrayList<>();
    private boolean withWhere = false;
    private boolean withOrderBy = false;
    private WhereJoin whereJoin;
    private boolean nativeQuery = false;

    public boolean isNativeQuery() {

        return nativeQuery;
    }

    public void setNativeQuery(boolean nativeQuery) {

        this.nativeQuery = nativeQuery;
    }

    public WhereJoin getWhereJoin() {

        return whereJoin;
    }

    public void setWhereJoin(WhereJoin whereJoin) {

        this.whereJoin = whereJoin;
    }

    private PageInfoBuilder(EntityManager entityManager) {

        this.entityManager = entityManager;
    }

    private PageInfoBuilder() {

    }

    public static PageInfoBuilder builder(EntityManager entityManager) {

        return new PageInfoBuilder(entityManager);
    }

    private String paramName(String paramName) {

        return paramName.replaceAll("\\.", "");
    }

    public PageInfo query(Pageable pageable, Class<?> voClazz) {

        for (WhereCondition whereCondition : whereConditions) {

            if (whereCondition.getValue() == null) {
                continue;
            }

            String name = whereCondition.getName();
            String paramName = paramName(name);
            Object[] value = whereCondition.getValue();

            switch (whereCondition.getWhereCase()) {

                // 等于
                case EQ:
                    where(name + " = :" + paramName);
                    queryParameters.add(QueryParameter.build(paramName, value[0]));
                    break;

                // 不等于
                case NE:
                    where(name + " <> :" + paramName);
                    queryParameters.add(QueryParameter.build(paramName, value[0]));
                    break;

                // 大于
                case GT:
                    where(name + " > :" + paramName);
                    queryParameters.add(QueryParameter.build(paramName, value[0]));
                    break;

                // 小于
                case LT:

                    where(name + " < :" + paramName);
                    queryParameters.add(QueryParameter.build(paramName, value[0]));
                    break;

                // 小于等于
                case LE:

                    where(name + " <= :" + paramName);
                    queryParameters.add(QueryParameter.build(paramName, value[0]));
                    break;

                // 大于等于
                case GE:

                    where(name + " >= :" + paramName);
                    queryParameters.add(QueryParameter.build(paramName, value[0]));
                    break;

                // IN 范围
                case IN:

                    StringBuilder sqlIn = new StringBuilder();
                    for (int index = 0; index < value.length; index++) {
                        if (index == 0) {
                            sqlIn.append(":").append(paramName + index);
                        } else {
                            sqlIn.append(", :").append(paramName + index);
                        }
                        queryParameters.add(QueryParameter.build(paramName + index, value[index]));
                    }

                    where(name + " IN (" + sqlIn.toString() + ")");
                    break;

                // BETWEEN 区间
                case BETWEEN:

                    for (int index = 0; index < value.length; index++) {
                        queryParameters.add(QueryParameter.build(paramName + index, value[index]));
                    }
                    where("(" + name + " BETWEEN :" + paramName + "0 AND :" + paramName + "1)");
                    break;

                // IS NULL
                case NULL:

                    where(name + " IS NULL");
                    break;

                // IS NOT NULL
                case NOTNULL:

                    where(name + " IS NOT NULL");
                    break;

                // 模糊匹配，只适用于varchar类型的字段
                case LIKE:

                    where("upper(" + name + ") LIKE CONCAT('%', :" + paramName + ", '%')");
                    queryParameters
                            .add(QueryParameter.build(paramName, value[0].toString().toUpperCase()));
                    break;

                // 左匹配
                case LLIKE:

                    where("upper(" + name + ") LIKE CONCAT('%', :" + paramName + ")");
                    queryParameters
                            .add(QueryParameter.build(paramName, value[0].toString().toUpperCase()));
                    break;

                // 右匹配
                case RLIKE:

                    where("upper(" + name + ") LIKE CONCAT(:" + paramName + ", '%')");
                    queryParameters
                            .add(QueryParameter.build(paramName, value[0].toString().toUpperCase()));
                    break;

                // other
                default:

                    break;

            }
        }

        if (getWhereJoin() != null) {
            where("(" + getWhereJoin().getJoin() + ")");
            if (!getWhereJoin().getQueryParameters().isEmpty()) {
                queryParameters.addAll(getWhereJoin().getQueryParameters());
            }
        }

        for (SortCondition sortCondition : sortConditions) {
            if (sortCondition.getOrderBy() == null
                    || EnumConstant.OrderBy.ASC.name().equals(sortCondition.getOrderBy().name())) {
                orderBy(sortCondition.getName() + " " + EnumConstant.OrderBy.ASC.name());
            } else {
                orderBy(sortCondition.getName() + " " + EnumConstant.OrderBy.DESC.name());
            }
        }

        querySQL.append(selectNames).append(fromSQL).append(whereSQL).append(groupBySQL)
                .append(orderBySQL);

        Query query = null;
        TypedQuery<?> typedQuery = null;
        if (isNativeQuery()) {
            query = this.entityManager.createNativeQuery(querySQL.toString());
            query.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(voClazz));
        } else {
            typedQuery = this.entityManager.createQuery(querySQL.toString(), voClazz);
        }

        if (pageable != null) {
            if (groupBySQL == null || groupBySQL.length() == 0) {
                countSQL.append(countName).append(fromSQL).append(whereSQL);
            } else {
                countSQL.append(countName).append(" FROM (SELECT COUNT(*) AS ROWCOUNT ").append(fromSQL.append(whereSQL).append(groupBySQL)).append(") AS T1 ");
            }
            if (isNativeQuery()) {
                query.setFirstResult((int) pageable.getOffset());
                query.setMaxResults(pageable.getPageSize());
            } else {
                typedQuery.setFirstResult((int) pageable.getOffset());
                typedQuery.setMaxResults(pageable.getPageSize());
            }
        }

        PageInfo pageInfo = PageInfo.build();

        if (isNativeQuery()) {

            for (QueryParameter e : queryParameters) {
                query.setParameter(e.getName(), e.getValue());
            }
            pageInfo.setVoList(query.getResultList());
        } else {

            for (QueryParameter e : queryParameters) {
                typedQuery.setParameter(e.getName(), e.getValue());
            }
            pageInfo.setVoList(typedQuery.getResultList());
        }

        if (pageable != null) {
            pageInfo.setPageno(pageable.getPageNumber() + 1);
            pageInfo.setPagesize(pageable.getPageSize());

            if (isNativeQuery()) {
                Query queryCount = this.entityManager.createNativeQuery(countSQL.toString());
                queryParameters.forEach(e -> queryCount.setParameter(e.getName(), e.getValue()));
                // 分组查询时如果无记录会返回空，getSingleResult方法会抛异常
                try {
                    pageInfo.setRecords(((BigInteger) queryCount.getSingleResult()).longValue());
                } catch (javax.persistence.NoResultException ex) {
                    pageInfo.setRecords(0L);
                }
            } else {
                TypedQuery<Long> typedQueryCount = this.entityManager
                        .createQuery(countSQL.toString(), Long.class);
                queryParameters
                        .forEach(e -> typedQueryCount.setParameter(e.getName(), e.getValue()));
                // 分组查询时如果无记录会返回空，getSingleResult方法会抛异常
                try {
                    pageInfo.setRecords(typedQueryCount.getSingleResult().longValue());
                } catch (javax.persistence.NoResultException ex) {
                    pageInfo.setRecords(0L);
                }
            }
        }

        return pageInfo;
    }

    public PageInfoBuilder parameter(String name, String value) {

        this.queryParameters.add(QueryParameter.build(name, value));
        return this;
    }

    public PageInfoBuilder parameter(Collection<QueryParameter> queryParameters) {

        this.queryParameters.addAll(queryParameters);
        return this;
    }

    public PageInfoBuilder selectNames(Class<?> voClazz, String names) {

        if (names == null) {
            selectNames.append(" * ");
        }
        if (voClazz == null) {
            selectNames.append(" ").append(names).append(" ");
        } else {
            if (isNativeQuery()) {
                selectNames.append(" ").append(names).append(" ");
            } else {
                selectNames.append(" new ").append(voClazz.getName()).append(" (").append(names)
                        .append(" ) ");
            }
        }
        return this;
    }

    public PageInfoBuilder countName(String countName) {

        this.countName = " count(" + countName + ") ";
        return this;
    }

    public PageInfoBuilder from(String primaryTab) {

        fromSQL.append(" FROM ").append(primaryTab).append(" ");
        return this;
    }

    public PageInfoBuilder join(String innerJoinTab, String onExp) {

        fromSQL.append(" inner join ").append(innerJoinTab).append(" on ").append(onExp);
        return this;
    }

    public PageInfoBuilder lefJoin(String leftJoinTab, String onExp) {

        fromSQL.append(" left join ").append(leftJoinTab).append(" on ").append(onExp);
        return this;
    }

    public PageInfoBuilder where(String where) {

        if (withWhere) {
            if (where != null) {
                whereSQL.append(" and ").append(where).append(" ");
            }
        } else {
            if (where != null) {
                whereSQL.append(" where ").append(where).append(" ");
                withWhere = true;
            }
        }
        return this;
    }

    public PageInfoBuilder groupBy(String groupBy) {

        groupBySQL.append(" group by ").append(groupBy).append(" ");
        return this;
    }

    public PageInfoBuilder orderBy(String orderBy) {

        if (withOrderBy) {
            orderBySQL.append(" ,").append(orderBy).append(" ");
        } else {
            orderBySQL.append(" order by ").append(orderBy).append(" ");
            withOrderBy = true;
        }
        return this;
    }

    public PageInfoBuilder appendWhereJoin(String join, List<QueryParameter> queryParameters) {

        WhereJoin whereJoin = new WhereJoin();
        whereJoin.setJoin(join);
        whereJoin.setQueryParameters(queryParameters);
        this.setWhereJoin(whereJoin);
        return this;
    }

    public PageInfoBuilder appendWhereCondition(String name, EnumConstant.WhereCase whereCase,
                                                Object... values) {

        WhereCondition whereCondition = new WhereCondition(name, whereCase, values);
        if (!whereConditions.contains(whereCondition))
            whereConditions.add(whereCondition);
        return this;
    }

    public PageInfoBuilder appendWhereCondition(String name, Object... values) {

        return appendWhereCondition(name, EnumConstant.WhereCase.EQ, values);
    }

    public PageInfoBuilder appendSortCondition(String name, EnumConstant.OrderBy orderBy) {

        SortCondition sortCondition = new SortCondition(name, orderBy);
        if (!sortConditions.contains(sortCondition))
            sortConditions.add(sortCondition);
        return this;
    }

    public PageInfoBuilder appendSortCondition(String name) {

        return appendSortCondition(name, null);
    }

    public PageInfoBuilder appendSortConditions(String... names) {

        if (names != null)
            for (String name : names)
                appendSortCondition(name);

        return this;
    }

    private static class WhereJoin {

        private String join;
        private List<QueryParameter> queryParameters = new ArrayList<>();

        public String getJoin() {

            return join;
        }

        public void setJoin(String join) {

            this.join = join;
        }

        public List<QueryParameter> getQueryParameters() {

            return queryParameters;
        }

        public void setQueryParameters(List<QueryParameter> queryParameters) {

            this.queryParameters = queryParameters;
        }
    }

    private static class WhereCondition {

        private String name;
        private EnumConstant.WhereCase whereCase;
        private Object[] values;

        public WhereCondition(String name, EnumConstant.WhereCase whereCase, Object[] values) {

            this.name = name;
            this.whereCase = whereCase;
            this.values = values;
        }

        public EnumConstant.WhereCase getWhereCase() {

            return whereCase;
        }

        public String getName() {

            return name;
        }

        public Object[] getValue() {

            return values;
        }

        @Override
        public int hashCode() {

            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            WhereCondition other = (WhereCondition) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }

    }

    private static class SortCondition {

        private String name;
        private EnumConstant.OrderBy orderBy;

        public SortCondition(String name, EnumConstant.OrderBy orderBy) {

            this.name = name;
            this.orderBy = orderBy;
        }

        public String getName() {

            return name;
        }

        public EnumConstant.OrderBy getOrderBy() {

            return orderBy;
        }

        @Override
        public int hashCode() {

            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SortCondition other = (SortCondition) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }

    }

}
