package v01_01_02;

import java.util.ArrayList;

public class TensorTrain {
	public static TensorTrain zero(int []r)
	{
		int d = r.length;
		TensorTrain Z = new TensorTrain(); 
		Z._r = new int[d];
		Z._cores = new ArrayList<Tensor>();
		for (int i = 0; i < d; i++)
			Z._cores.add(new Tensor(r));
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
	public TensorTrain(ArrayList<Tensor> ttfList, int... r)
	{
		ttfList.forEach(core -> _cores.add(core));
		int d = _cores.size();
		System.arraycopy(r,0,_r,0,d);
	}
	public TensorTrain(Tensor T)
	{
		Pair<ArrayList<Tensor>,int[]> ttrk = TensorDecompositon.TTdecompotion(T, 1);
		_cores = ttrk._1(); _r = ttrk._2();
	}
	public TensorTrain(Tensor T, double epsilon)
	{
		Pair<ArrayList<Tensor>,int[]> ttrk = TensorDecompositon.TTdecompotion(T, epsilon);
		_cores = ttrk._1(); _r = ttrk._2();
	}

//	private TensorTrain(int d)
//	{
//		_r = new int[d];
//		_cores = new ArrayList<Tensor>(d);
//	}

	protected TensorTrain clone()
	{
		return new TensorTrain(_cores, _r);
	}
	
	public int dim()
	{
		return _r.length;
	}
	public int []rk()
	{
		return _r;
	}
	public TensorTrain sum(TensorTrain T)
	{
		int d = dim();
		if (d != T.dim())
			return null;
		int []r = new int[d];
		ArrayList<Tensor> cores = new ArrayList<Tensor>();
		Tensor	A = _cores.get(0),
				B = T._cores.get(0);
		cores.add(A.unionRow(B));
		r[0] = Math.max(_r[0], T._r[0]);
		for (int i = 1; i < d-1; i++)
		{
			A = _cores.get(i); B = T._cores.get(i);
			cores.add(A.unionDiag(B));
			r[i] = _r[i]+T._r[i];
		}
		A = _cores.get(d-1); B = T._cores.get(d-1);
		cores.add(A.unionCol(B));
		r[d-1] = Math.max(_r[d-1], T._r[d-1]);

		return new TensorTrain(cores, r);
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
		return new TensorTrain(cores, _r);
	}

	public TensorTrain vmul(TensorTrain x) {
		// TODO Auto-generated method stub
		return null;
	}
	private int []_r;
	private ArrayList<Tensor> _cores;
}
