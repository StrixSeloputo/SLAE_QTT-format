package v01_01_02;

import java.util.ArrayList;
import org.ejml.data.DenseMatrix64F;
import org.ejml.alg.dense.decomposition.svd.SvdImplicitQrDecompose_D64;

public class TensorDecompositon {

	public static Pair<ArrayList<Tensor>, int[]> TTdecompotion(Tensor A, double epsilon)
	{
		int d = A.dim(),
			N = A.size();
		double delta = epsilon/Math.sqrt(d-1d)*A.FrobeniusNorm();
		Tensor C = A.clone();
		int []r = new int[d];
		r[0] = 1;
		ArrayList<Tensor> ttfList = new ArrayList<Tensor>();
		
		for (int k = 1; k < d; k++)
		{
			DenseMatrix64F reshapedCMatrix = C.reshape(2, r[k-1]*A.n(k), N/(r[k-1]*A.n(k))).toDenseMatrix64F();
			//usvT
			Triple<DenseMatrix64F, DenseMatrix64F, DenseMatrix64F> USVT = SVD(reshapedCMatrix);
			DenseMatrix64F	U = USVT._1(),
							S = USVT._2(),
							VT = USVT._3();
			r[k] = Utility.rkS(S);
			ttfList.add(new Tensor(U).reshape(3, r[k-1], A.n(k), r[k]));
			C = new Tensor(Utility.matrixMatrixMult(S, VT));
			N = (N*r[k])/(A.n(k)*r[k-1]);
		}
		ttfList.add(C);
		return new Pair<ArrayList<Tensor>,int[]> (ttfList, r);
	}
	
	public static Triple<DenseMatrix64F, DenseMatrix64F, DenseMatrix64F> SVD(DenseMatrix64F  C)
	{
		SvdImplicitQrDecompose_D64 SVD_decompositor = new SvdImplicitQrDecompose_D64(true, true, true, false);
		int m = C.numRows, n = C.numCols;
		boolean decomposed = SVD_decompositor.decompose(C);
//		SVD_decompositor.
		if (!decomposed)
			throw new RuntimeException("Smthg goes wrong");
		boolean transpose = C.numCols > C.numRows;
		DenseMatrix64F	U = new DenseMatrix64F(m, m), 
						S = new DenseMatrix64F(m, n), 
						V = new DenseMatrix64F(n, n);
		U = SVD_decompositor.getU(U, transpose);
		S = SVD_decompositor.getW(S);
		V = SVD_decompositor.getV(V, transpose);
		return new Triple<DenseMatrix64F, DenseMatrix64F, DenseMatrix64F>(U, S, V);
	}
}
