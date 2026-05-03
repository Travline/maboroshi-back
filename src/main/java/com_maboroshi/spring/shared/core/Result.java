package com_maboroshi.spring.shared.core;

import java.util.function.Function;

public class Result<T, E> {
  public boolean isSucces;
  private T value;
  private E error;

  public Result(boolean isSucces, T value, E error) {
    this.isSucces = isSucces;
    this.value = value;
    this.error = error;
  }

  public static <U, F> Result<U, F> ok(U value) {
    return new Result<>(true, value, null);
  }

  public static <U, F> Result<U, F> fail(F error) {
    return new Result<>(false, null, error);
  }

  public T getValue() {
    if (!this.isSucces) {
      return null;
    }
    return this.value;
  }

  public E getErrorValue() {
    if (this.isSucces) {
      return null;
    }
    return this.error;
  }

  public <R> R match(Function<T, R> onSucces, Function<E, R> onFailure) {
    return this.isSucces ? onSucces.apply(this.value) : onFailure.apply(this.error);
  }
}
