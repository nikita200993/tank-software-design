package ru.mipt.bit.platformer.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;

@Documented
@Nonnull
@Retention(RetentionPolicy.RUNTIME)
@TypeQualifierDefault(ElementType.FIELD)
public @interface FieldsAreNonNullByDefault {
}
