package com.rparnp.bank.mapper;

import com.rparnp.bank.entity.BalanceEntity;
import com.rparnp.bank.enums.CurrencyType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Mapper
@Repository
public interface BalanceMapper {

    @Select("SELECT * FROM balance WHERE account_id = #{uuid}")
    List<BalanceEntity> getByAccountId(UUID uuid);

    @Select("SELECT * FROM balance WHERE account_id = #{uuid} AND currency = #{currency}")
    BalanceEntity getByComposite(UUID uuid, CurrencyType currency);

    @Update("UPDATE balance " +
            "SET amount = #{amount} " +
            "WHERE account_id = #{accountId} AND currency = #{currency}")
    void updateBalance(BalanceEntity balance);

    @Insert("INSERT INTO balance(account_id, currency) VALUES (#{accountId}, #{currency})")
    void insert(BalanceEntity balance);
}
