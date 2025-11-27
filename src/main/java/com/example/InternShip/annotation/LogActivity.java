package com.example.InternShip.annotation;

import com.example.InternShip.entity.Log.Action;
import com.example.InternShip.entity.Log.Model;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogActivity {
    Action action();
    Model affected();
    String description();
}