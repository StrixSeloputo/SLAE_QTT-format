package v01_01_02;

public class SystemSolve {
	public SystemSolve() {}
	public SystemSolve(TensorTrain A, TensorTrain f)
	{
		_A = A; _f = f;
	}
	public SystemSolve(TensorTrain A, TensorTrain f, TensorTrain u)
	{
		_A = A; _f = f; _u = u;
	}
	
	public Pair<TensorTrain,Double> solve(double e, int nit)
	{
		TensorTrain x = TensorTrain.zero(_A.rk());		
		for (int i = 1; i < nit; i++)
		{
			TeansorTrain res = _A.vmul(x).sub(_f).round(e);
		}
		return new Pair<TensorTrain,Double>(x, Double.NaN);
	}
	
	private TensorTrain _A, _u, _f;
}
