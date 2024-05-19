package com.rparnp.bank.mapper;

import com.rparnp.bank.entity.TransactionEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Mapper
@Repository
public interface TransactionMapper {

    @Select("SELECT * FROM transaction WHERE account_id = #{accountId}")
    List<TransactionEntity> getAll(UUID accountId);

    @Insert("INSERT INTO transaction(account_id, amount, currency, direction, description) " +
            "VALUES (#{accountId}, #{amount}, #{currency}, #{direction}, #{description}) " +
            "RETURNING transaction_id")
    @Options(useGeneratedKeys = true, keyProperty = "transactionId", keyColumn = "transaction_id")
    void create(TransactionEntity transaction);
}
