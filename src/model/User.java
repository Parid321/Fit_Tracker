package model;

public class User {
  public long userID;
  public String name;
  public String email;
  public Integer age;
  public Double weight;
  public String goal;
  public String passwordHash;

  @Override public String toString() { return name + " (" + email + ")"; }
}
