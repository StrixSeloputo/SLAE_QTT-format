package v01_01_02;

import org.ejml.alg.dense.linsol.svd.SolvePseudoInverseSvd;
import org.ejml.data.DenseMatrix64F;

public class Utility {
//	Double addFloatCorrected(Double a, Double b, Double corr)
//	{
//		return 0d;
//	}
	static int rkS(DenseMatrix64F S)
	{
		// S is matrix of singular values
		int m = S.numRows,
			n = S.numCols;
		int k = Math.min(m, n);
		int r = 0;
		for (int i = 0; i < k; i++)
			if (S.get(i, i) != 0)
				r++;
		return r;
	}
	static DenseMatrix64F matrixMatrixMult(DenseMatrix64F A, DenseMatrix64F B)
	{
		if (A.numCols != B.numRows)
			return null;
		int m = A.numRows,
			k = A.numCols,
			n = B.numCols;
		DenseMatrix64F C = new DenseMatrix64F(m,n);
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				for (int l = 0; l < k; l++)
					C.set(i, j, A.get(i, l)*B.get(l, j));
		return C;
	}
	static DenseMatrix64F transpose(DenseMatrix64F M)
	{
		int m = M.numRows,
			n = M.numCols;
		DenseMatrix64F Mt = new DenseMatrix64F(n, m);
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				Mt.set(j, i, M.get(i, j));
		return Mt;
	}
	static int[] iterNext(int[]curr, int[]sup)
	{
		if (curr.length > sup.length)
			return null;
		int d = curr.length;
		for(int i = d-1; i >= 0; i--)
		{
			if (curr[i]<0 || curr[i]>=sup[i])
				return null;
			curr[i]++;
			if (curr[i] < sup[i])
				return curr;
			curr[i] = 0;
		}
		if (curr[0] > 0)
			return curr;
		return null;
	}
	static DenseMatrix64F solve(DenseMatrix64F A, DenseMatrix64F b) {
		int m = A.numRows,
			k = A.numCols,
			n = b.numCols;
		if (m != b.numRows)
			return null;
		DenseMatrix64F M = new DenseMatrix64F(k, n);
		SolvePseudoInverseSvd solver = new SolvePseudoInverseSvd();
		solver.setA(A);
		solver.solve(b, M);
		return M;
	}
}
