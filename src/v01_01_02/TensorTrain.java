package v01_01_02;

import java.util.ArrayList;

import org.ejml.alg.dense.decomposition.qr.QRDecompositionHouseholderTran_D64;
import org.ejml.data.DenseMatrix64F;

public class TensorTrain {
	public static TensorTrain zero(int []r, int []n)
	{
		int d = r.length;
		TensorTrain Z = new TensorTrain(); 
		Z._r = r.clone();
		Z._n = n.clone();
		Z._cores = new ArrayList<Tensor>();
		Z._cores.add(new Tensor(new int[]{n[0],r[0]}));
		for (int k = 1; k < d-1; k++)
			Z._cores.add(new Tensor(new int[]{ r[k-1],n[k],r[k]}));
		Z._cores.add(new Tensor(new int[]{ r[d-1],n[d-1]}));
		return Z;
	}
	public TensorTrain() {}
	public TensorTrain(ArrayList<Tensor> ttfList)
	{
		ttfList.forEach(core -> _cores.add(core));
		int d = _cores.size();
		_r = new int[d];
		for (int i = 0; i < d; i++)
		{
			_r[i] = _cores.get(i).rk();
		}
	}
//	public TensorTrain(ArrayList<Tensor> ttfList, int... r)
//	{
//		ttfList.forEach(core -> _cores.add(core));
//		int d = _cores.size();
//		System.arraycopy(r,0,_r,0,d);
//		_n = r.clone();
//	}
	public TensorTrain(ArrayList<Tensor> ttfList, int[]r, int[]n)
	{
		ttfList.forEach(core -> _cores.add(core));
		int d = _cores.size();
		System.arraycopy(r,0,_r,0,d);
		_n = n.clone();
	}
	public TensorTrain(Tensor T)
	{
		Triple<ArrayList<Tensor>,int[], int[]> ttrknk = TensorDecomposition.TTdecompotion(T, 1);
		_cores = ttrknk._1(); _r = ttrknk._2(); _n = ttrknk._3();
	}
	public TensorTrain(Tensor T, double epsilon)
	{
		Triple<ArrayList<Tensor>,int[], int[]> ttrknk = TensorDecomposition.TTdecompotion(T, epsilon);
		_cores = ttrknk._1(); _r = ttrknk._2(); _n = ttrknk._3();
	}

//	private TensorTrain(int d)
//	{
//		_r = new int[d];
//		_cores = new ArrayList<Tensor>(d);
//	}

	protected TensorTrain clone()
	{
		return new TensorTrain(_cores, _r, _n);
	}
	
	public int dim()
	{
		return _r.length;
	}
	public int []rk()
	{
		return _r;
	}
	public int[]n()
	{
		return _n;
	}
	public int n(int i)
	{
		return _n[i];
	}
	private double FrobenuisNorm() {
		double res = 0d;
		for (Tensor T : _cores)
			res += T.FrobeniusNorm();
		return res;
	}
	public double dist(TensorTrain _u) {
		double dist = 0d;
		
		return dist;
	}
	public Tensor core(int k)
	{
		return _cores.get(k);
	}
	public void setCore(int k, Tensor T)
	{
		int d = dim();
		if (k<0 || k>=d)
			return;
		if (k==0)
		{
			if (T.dim()!=2 || T._order[0]<_n[0] || T._order[1]<_r[0])
				return;
		}
		if (k==d-1)
		{
			if (T.dim()!=2 || T._order[0]<_r[d-2] || T._order[1]<_n[d-1])
				return;
		}
		if (T.dim()!=3 || T._order[0]<_r[k-1] || T._order[1]<_n[k] || T._order[1]<_r[k])
			return;
		_cores.set(k, T);
	}
	public double get(int d, int... ind)
	{
		if (d != dim())
			return Double.NaN;
		double res = 0d;
		int []index = new int[d];
		int []alpha = new int[d-1];
		System.arraycopy(ind,0,index,0,d);
		for (; alpha != null;)
		{
			double g = _cores.get(0).get(2, index[0], alpha[0]);
			for (int j = 1; j < d-1; j++)
				g *= _cores.get(j).get(3, alpha[j-1], index[j], alpha[j]);
			g *= _cores.get(d-1).get(2, alpha[d-2], index[d-1]);
			res += g;
			alpha = Utility.iterNext(alpha, _r);
		}
		return res;
	}
	public TensorTrain sum(TensorTrain T)
	{
		int d = dim();
		if (d != T.dim())
			return null;
		int []r = new int[d], n = new int[d];
		ArrayList<Tensor> cores = new ArrayList<Tensor>();
		Tensor	A = _cores.get(0),
				B = T._cores.get(0);
		cores.add(A.unionRow(B));
		r[0] = Math.max(_r[0], T._r[0]);
		n[0] = Math.max(_n[0], T._n[0]);
		for (int i = 1; i < d-1; i++)
		{
			A = _cores.get(i); B = T._cores.get(i);
			cores.add(A.unionDiag(B));
			r[i] = _r[i]+T._r[i];
			n[i] = Math.max(_n[i], T._n[i]);
		}
		A = _cores.get(d-1); B = T._cores.get(d-1);
		cores.add(A.unionCol(B));
		r[d-1] = Math.max(_r[d-1], T._r[d-1]);
		n[d-1] = Math.max(_n[d-1], T._n[d-1]);

		return new TensorTrain(cores, r, n);
	}
	public TensorTrain sub(TensorTrain T)
	{
		return this.sum(T.kmul(-1));
	}
	public TensorTrain kmul(double k)
	{
		ArrayList<Tensor> cores = new ArrayList<Tensor>();
		cores.add(_cores.get(0).kmul(k));
		for (int i = 1; i < dim(); i++)
			cores.add(_cores.get(i).kmul(k));
		return new TensorTrain(cores, _r, _n);
	}

	public TensorTrain vmul(TensorTrain x) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public TensorTrain round(double e)
	{
		int d = dim();
		TensorTrain B = clone();
		B._r = new int[d];
		double delta = e/Math.sqrt(d-1)*FrobenuisNorm();
		for(int k = d-1; k > 0; k--)
		{
			QRDecompositionHouseholderTran_D64 QRdecompositor = new QRDecompositionHouseholderTran_D64();
			Tensor Gk = _cores.get(k);
			if (!QRdecompositor.decompose(Gk.unfolding(0)))
				return B;
			DenseMatrix64F	QkMatrix = null, 
							RkMatrix = null;
			QkMatrix = QRdecompositor.getQ(QkMatrix, true);
			RkMatrix = QRdecompositor.getR(RkMatrix, true);
			Tensor	Qk = new Tensor(Utility.transpose(QkMatrix)),
					Rk = new Tensor(Utility.transpose(RkMatrix));
			B._cores.set(k-1, B._cores.get(k-1).tensorTensorMul(Rk));
			int []qk_ord = { Gk._order[1], Gk._order[2], Qk.length()/(Gk._order[1]*Gk._order[2]) };
			B._cores.set(k, Qk.reshape(3, qk_ord));
		}
		for (int k = 0; k < d-1; k++)
		{
			Tensor Gk = B._cores.get(k);			
			DenseMatrix64F reshapedGkMatrix = Gk.unfolding(2);
			//usvT
			Triple<DenseMatrix64F, DenseMatrix64F, DenseMatrix64F> USVT = TensorDecomposition.SVD(reshapedGkMatrix);
			DenseMatrix64F	U = USVT._1(),
							S = USVT._2(),
							VT = USVT._3();
			B._r[k] = Utility.rkS(S);
			B._cores.set(k, new Tensor(U).reshape(3, _r[k-1], _n[k], B._r[k]));
			Tensor SVT = new Tensor(Utility.matrixMatrixMult(S, VT)).reshape(2, B._cores.get(k)._order[1], B._cores.get(k)._order[2]);
			B._cores.set(k+1, SVT.tensorTensorMul(B._cores.get(k+1)));
		}
		return B;
	}	
	public int[] argMaxAbs() {
		int d = dim();
		int []res = null;
		int []arg = new int[d];
		double val = Double.MIN_VALUE;
		for (; arg != null;)
		{
			double valc = get(d, arg);
			if (valc >= val)
			{
				val = valc;
				res = arg.clone();
			}
			arg = Utility.iterNext(arg, _n);
		}
		return res;
	}
	
	private int []	_r,
					_n;
	private ArrayList<Tensor> _cores;
}
