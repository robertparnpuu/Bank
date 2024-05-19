package com.rparnp.bank.handler;

import com.rparnp.bank.enums.DirectionType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

public class DirectionTypeHandler extends BaseTypeHandler<DirectionType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DirectionType parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter.name(), Types.OTHER);
    }

    @Override
    public DirectionType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String directionStr = rs.getString(columnName);
        return directionStr != null ? DirectionType.valueOf(directionStr) : null;
    }

    @Override
    public DirectionType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String directionStr = rs.getString(columnIndex);
        return directionStr != null ? DirectionType.valueOf(directionStr) : null;
    }

    @Override
    public DirectionType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String directionStr = cs.getString(columnIndex);
        return directionStr != null ? DirectionType.valueOf(directionStr) : null;
    }
}
