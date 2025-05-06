package bank.entities;

import lombok.Getter;

import java.time.Month;

@Getter
public enum Limit {
    WEEK,
    MONTH,
    QUARTER,
    YEAR,
    YEAR5,
    YEAR7,
    YEAR10,
    YEAR12,
    YEAR15
}
