package com.rparnp.bank.handler;

import com.rparnp.bank.enums.CurrencyType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

public class CurrencyTypeHandler extends BaseTypeHandler<CurrencyType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, CurrencyType parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter.name(), Types.OTHER);
    }

    @Override
    public CurrencyType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String currencyStr = rs.getString(columnName);
        return currencyStr != null ? CurrencyType.valueOf(currencyStr) : null;
    }

    @Override
    public CurrencyType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String currencyStr = rs.getString(columnIndex);
        return currencyStr != null ? CurrencyType.valueOf(currencyStr) : null;
    }

    @Override
    public CurrencyType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String currencyStr = cs.getString(columnIndex);
        return currencyStr != null ? CurrencyType.valueOf(currencyStr) : null;
    }
}
