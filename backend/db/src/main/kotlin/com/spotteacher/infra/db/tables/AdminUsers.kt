/*
 * This file is generated by jOOQ.
 */
package com.spotteacher.infra.db.tables


import com.spotteacher.infra.db.enums.AdminUsersAdminRole
import com.spotteacher.infra.db.tables.records.AdminUsersRecord

import java.time.LocalDateTime

import kotlin.collections.Collection
import kotlin.collections.List

import org.jooq.Condition
import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Identity
import org.jooq.InverseForeignKey
import org.jooq.Name
import org.jooq.PlainSQL
import org.jooq.QueryPart
import org.jooq.Record
import org.jooq.SQL
import org.jooq.Select
import org.jooq.Stringly
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("warnings")
open class AdminUsers(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, AdminUsersRecord>?,
    parentPath: InverseForeignKey<out Record, AdminUsersRecord>?,
    aliased: Table<AdminUsersRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<AdminUsersRecord>(
    alias,
    null,
    path,
    childPath,
    parentPath,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table(),
    where,
) {
    companion object {

        /**
         * The reference instance of <code>admin_users</code>
         */
        val ADMIN_USERS: AdminUsers = AdminUsers()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<AdminUsersRecord> = AdminUsersRecord::class.java

    /**
     * The column <code>admin_users.id</code>.
     */
    val ID: TableField<AdminUsersRecord, Long?> = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "")

    /**
     * The column <code>admin_users.user_id</code>.
     */
    val USER_ID: TableField<AdminUsersRecord, Long?> = createField(DSL.name("user_id"), SQLDataType.BIGINT.nullable(false), this, "")

    /**
     * The column <code>admin_users.admin_role</code>.
     */
    val ADMIN_ROLE: TableField<AdminUsersRecord, AdminUsersAdminRole?> = createField(DSL.name("admin_role"), SQLDataType.VARCHAR(6).nullable(false).defaultValue(DSL.inline("NORMAL", SQLDataType.VARCHAR)).asEnumDataType(AdminUsersAdminRole::class.java), this, "")

    /**
     * The column <code>admin_users.created_at</code>.
     */
    val CREATED_AT: TableField<AdminUsersRecord, LocalDateTime?> = createField(DSL.name("created_at"), SQLDataType.LOCALDATETIME(0).nullable(false).defaultValue(DSL.field(DSL.raw("CURRENT_TIMESTAMP"), SQLDataType.LOCALDATETIME)), this, "")

    /**
     * The column <code>admin_users.updated_at</code>.
     */
    val UPDATED_AT: TableField<AdminUsersRecord, LocalDateTime?> = createField(DSL.name("updated_at"), SQLDataType.LOCALDATETIME(0).nullable(false).defaultValue(DSL.field(DSL.raw("CURRENT_TIMESTAMP"), SQLDataType.LOCALDATETIME)), this, "")

    private constructor(alias: Name, aliased: Table<AdminUsersRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<AdminUsersRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)
    private constructor(alias: Name, aliased: Table<AdminUsersRecord>?, where: Condition?): this(alias, null, null, null, aliased, null, where)

    /**
     * Create an aliased <code>admin_users</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>admin_users</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>admin_users</code> table reference
     */
    constructor(): this(DSL.name("admin_users"), null)
    override fun getIdentity(): Identity<AdminUsersRecord, Long?> = super.getIdentity() as Identity<AdminUsersRecord, Long?>
    override fun getPrimaryKey(): UniqueKey<AdminUsersRecord> = Internal.createUniqueKey(AdminUsers.ADMIN_USERS, DSL.name("KEY_admin_users_PRIMARY"), arrayOf(AdminUsers.ADMIN_USERS.ID), true)
    override fun getUniqueKeys(): List<UniqueKey<AdminUsersRecord>> = listOf(
        Internal.createUniqueKey(AdminUsers.ADMIN_USERS, DSL.name("KEY_admin_users_user_id"), arrayOf(AdminUsers.ADMIN_USERS.USER_ID), true)
    )
    override fun `as`(alias: String): AdminUsers = AdminUsers(DSL.name(alias), this)
    override fun `as`(alias: Name): AdminUsers = AdminUsers(alias, this)
    override fun `as`(alias: Table<*>): AdminUsers = AdminUsers(alias.qualifiedName, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): AdminUsers = AdminUsers(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): AdminUsers = AdminUsers(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): AdminUsers = AdminUsers(name.qualifiedName, null)

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Condition?): AdminUsers = AdminUsers(qualifiedName, if (aliased()) this else null, condition)

    /**
     * Create an inline derived table from this table
     */
    override fun where(conditions: Collection<Condition>): AdminUsers = where(DSL.and(conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(vararg conditions: Condition?): AdminUsers = where(DSL.and(*conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Field<Boolean?>?): AdminUsers = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(condition: SQL): AdminUsers = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String): AdminUsers = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg binds: Any?): AdminUsers = where(DSL.condition(condition, *binds))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg parts: QueryPart): AdminUsers = where(DSL.condition(condition, *parts))

    /**
     * Create an inline derived table from this table
     */
    override fun whereExists(select: Select<*>): AdminUsers = where(DSL.exists(select))

    /**
     * Create an inline derived table from this table
     */
    override fun whereNotExists(select: Select<*>): AdminUsers = where(DSL.notExists(select))
}
