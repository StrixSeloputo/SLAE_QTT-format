package v01_01_02;

public class Pair<T1, T2> {
	public Pair() {}
	public Pair(T1 first, T2 second)
	{
		_1 = first; _2 = second;
	}
	
	public T1 _1()
	{
		return _1;
	}
	public T2 _2()
	{
		return _2;
	}
	
	private T1 _1;
	private T2 _2;
}
