package v01_01_02;

public class Triple<S1, S2, S3> {
	public Triple() {}
	public Triple(S1 first, S2 second, S3 third)
	{
		_1=first; _2=second; _3=third;
	}
	
	public S1 _1()
	{
		return _1;
	}
	public S2 _2()
	{
		return _2;
	}
	public S3 _3()
	{
		return _3;
	}

	private S1 _1;
	private S2 _2;
	private S3 _3;
}
