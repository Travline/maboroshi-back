package com_maboroshi.maboroshi_spring.shared.core;

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

  public T getValue() throws Exception {
    if (!this.isSucces) {
      throw new Exception("Invalid operation: Cannot get this value, the result is error");
    }
    return this.value;
  }

  public E getErrorValue() throws Exception {
    if (this.isSucces) {
      throw new Exception("Invalid operation: Cannot get this value, the result is succesful");
    }
    return this.error;
  }
}
