package com.rparnp.bank.mapper;

import com.rparnp.bank.entity.BalanceEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Mapper
@Repository
public interface BalanceMapper {

    @Select("SELECT * FROM balance WHERE account_id = #{uuid}")
    List<BalanceEntity> getByAccountId(UUID uuid);

    @Insert("INSERT INTO balance(account_id, currency) VALUES (#{accountId}, #{currency})")
    void insert(BalanceEntity account);
}
