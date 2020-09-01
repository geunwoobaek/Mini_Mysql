package pratice;

public class type<T1 extends Comparable<T1>,T2 extends Comparable<T2>> {
private T1 key;
private T1 key2;
private T2 value;

public type(T1 key,T2 value)
{
    this.key=key;
    this.value=value;
}
public type() {
}
public T1 getKey()
{
    return key;
}
public T2 getValue()
{
    return value;
}
public void compare(T1 other_key)
{
    if(key.compareTo(other_key)==1) System.out.println("hello");
}
}