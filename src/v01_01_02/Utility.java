package v01_01_02;

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
}
