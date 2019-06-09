package com.example.testtask.data.mapper;

public interface Mapper<INPUT, OUTPUT> {

    OUTPUT map(INPUT input) throws Throwable;
}
