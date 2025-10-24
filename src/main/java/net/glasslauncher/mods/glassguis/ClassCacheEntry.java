package net.glasslauncher.mods.glassguis;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public record ClassCacheEntry(Field[] fields, Method[] methods) {

}
