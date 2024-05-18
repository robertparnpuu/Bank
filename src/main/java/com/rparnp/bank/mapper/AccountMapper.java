package com.rparnp.bank.mapper;

import com.rparnp.bank.entity.AccountEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Mapper
@Repository
public interface AccountMapper {

    //TODO: Maybe could combine getting an account & it's balances in a single query
    @Select("SELECT * FROM account WHERE account_id = #{uuid}")
    AccountEntity getById(UUID uuid);

    @Insert("INSERT INTO account(customer_id, country) VALUES (#{customerId}, #{country}) RETURNING account_id")
    @Options(useGeneratedKeys = true, keyProperty = "accountId", keyColumn = "account_id")
    void insert(AccountEntity account);
}
