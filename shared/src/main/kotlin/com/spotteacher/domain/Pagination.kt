package com.spotteacher.domain

import com.spotteacher.util.StringUtil.camelToSnakeCase
import kotlin.reflect.KProperty1

enum class SortOrder {
    ASC, DESC
}

/**
 * ページングのための対象カラムとソート順、ページ毎の最後の値を保持するクラス
 * this class is for target column and sort order for pagination, and hold last value for each page
 * @param column: KProperty1<T, U> T クラスに定義されたカラム名
 * @param lastValue: U last column value
 * @param order: SortOrder order
 * @param transform: (U) -> R transform column value to primitive value
 */
data class ColumnValue<T, U, R>(
    val column: KProperty1<T, U>,
    val lastValue: U,
    val order: SortOrder = SortOrder.ASC,
    val transform: (U) -> R,
) {
    /**
     * カラムのプリミティブ値を取得する
     * get primitive value of column
     * DBのカラムに渡す値の変換などに使用する
     */
    fun getPrimitiveValue(): R = transform(lastValue)

    fun getDbColumnName(): String = column.name.camelToSnakeCase()
}

/**
 * ページネーション値クラス
 * @param limit: Int ページングの取得数
 * @param cursorColumns: Array<out ColumnValue<T, *, *>> ページングするカラム群 this is columns to paginate
 */
class Pagination<T>(
    val limit: Int,
    vararg val cursorColumns: ColumnValue<T, *, *>,
)
