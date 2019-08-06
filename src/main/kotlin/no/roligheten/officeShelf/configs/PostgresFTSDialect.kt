package no.roligheten.officeShelf.configs

import org.hibernate.dialect.PostgreSQL95Dialect
import org.hibernate.dialect.function.StandardSQLFunction
import org.hibernate.type.DoubleType
import org.hibernate.type.ObjectType
import org.hibernate.QueryException
import org.hibernate.engine.spi.SessionFactoryImplementor
import org.hibernate.dialect.function.SQLFunction
import org.hibernate.engine.spi.Mapping
import org.hibernate.type.BooleanType
import org.hibernate.type.Type


class PgFullTextDialect: PostgreSQL95Dialect() {
    init {
        registerFunction("fts", PgFullTextFunction())
        registerFunction("ts_rank", StandardSQLFunction("ts_rank", DoubleType.INSTANCE))
        registerFunction("to_tsquery", StandardSQLFunction("to_tsquery", ObjectType.INSTANCE))
    }
}

class PgFullTextFunction : SQLFunction {

    @Throws(QueryException::class)
    override fun getReturnType(columnType: Type, mapping: Mapping): Type {
        return BooleanType()
    }

    override fun hasArguments(): Boolean {
        return true
    }

    override fun hasParenthesesIfNoArguments(): Boolean {
        return false
    }

    @Throws(QueryException::class)
    override fun render(type: Type, args: List<*>, factory: SessionFactoryImplementor): String {
        val searchString = args[0] as String
        return "$FTS_VECTOR_FIELD @@ to_tsquery($searchString)"
    }

    companion object {
        val FTS_VECTOR_FIELD = "search_text"
    }
}