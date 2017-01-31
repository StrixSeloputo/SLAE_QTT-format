package v01_01_02;

import org.ejml.data.DenseMatrix64F;

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
		TensorTrain x = TensorTrain.zero(_A.rk(), _A.n());		
		for (int i = 1; i < nit; i++)
		{
			TensorTrain res = _A.vmul(x).sub(_f).round(e);
			int[]max = res.argMaxAbs();
			int[]J = new int[max.length-1];
			for (int j = 1; j < max.length; j++)
				J[j-1] = max[j];
			DenseMatrix64F B = preconditioner(J);
			DenseMatrix64F R1 = Utility.solve(B, res.core(0).toDenseMatrix64F());
			res.setCore(0, new Tensor(R1));
			x = x.sum(res);
			x = x.round(e);
		}
		return new Pair<TensorTrain,Double>(x, x.dist(_u));
	}
	
	private DenseMatrix64F preconditioner(int[]J)
	{
		int d = _A.dim(),
			m = _A.n(0),
			n = _A.n(1);
		DenseMatrix64F M = new DenseMatrix64F(m,n);
		int []I = new int[d];
		for (int i = 2; i < d; i++)
			I[i] = J[i-2];
		for (int i = 0; i < m; i++)
		{
			I[0] = i;
			for (int j = 0; j < n; j++)
			{
				I[1] = j;
				M.set(i, j, _A.get(d, I));
			}
		}
		return M;
	}
	
	private TensorTrain _A, _u, _f;
}
