package com.chn.mybatis.gen.trans;

import com.chn.mybatis.gen.def.ColumnMetadata;
import com.chn.mybatis.gen.utils.SqlTypeUtils;

public class ColumnTrans extends Trans {

	private String alias;
	private ColumnMetadata meta;
	private TableTrans trans;

	public ColumnTrans(ColumnMetadata meta) {
		this.meta = meta;
		this.alias = this.createColumnAlias();
		this.trans = TableTrans.find(meta.getTableName());
	}

	public String getUpperStartFieldName() {
		return underscoreToCamelCase(meta.getColumnName());
	}

	public String getLowerStartFieldName() {
		String camelCase = underscoreToCamelCase(meta.getColumnName());
		return camelCase.substring(0, 1).toLowerCase() + camelCase.substring(1);
	}

	public TableTrans getTableTrans() {
		return trans;
	}

	public void setTableTrans(TableTrans trans) {
		this.trans = trans;
	}

	public String getJdbcType() {
		return meta.getTypeName();
	}

	public String getRemarks() {
		return meta.getRemarks();
	}

	public String getJavaType() {
		return SqlTypeUtils.decodeToJavaType(meta.getDataType());
	}

	public String getName() {
		return meta.getColumnName();
	}

	public String getTableName() {
		return meta.getTableName();
	}

	public String getAlias() {
		return alias;
	}

}
