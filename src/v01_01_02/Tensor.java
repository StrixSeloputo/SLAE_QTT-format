package v01_01_02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.ejml.data.DenseMatrix64F;

public class Tensor {
	public Tensor() {}
	public Tensor(int []ord)
	{
		_order=ord.clone(); _data = new double[size(_order.length)];
	}
	public Tensor(int []ord, double... data)
	{
		_order = ord.clone();
		final int length = size(dim());
        _data = new double[ length ];
        set(ord, data);
	}
	public Tensor(DenseMatrix64F M)
	{
		_order = new int[]{ M.numRows, M.numCols };
		_data = M.getData().clone();
	}
	public Tensor(Scanner sc)
	{
		// first string - _order[]
		// other - double from data
		String orderStr = sc.nextLine();
		Scanner orderScan = new Scanner(orderStr);
		List<Integer> orderList = new ArrayList<Integer>(); 
		for (; orderScan.hasNextInt(); orderList.add(orderScan.nextInt()));
		orderScan.close();
		_order = new int[orderList.size()];
		for (int i = 0; i < orderList.size(); i++)
			_order[i] = orderList.get(i);
		
		List<Double> dataList = new ArrayList<Double>();
		for(; sc.hasNextDouble(); dataList.add(sc.nextDouble()));
		sc.close();
		_data = new double[dataList.size()];
		for (int i = 0; i < dataList.size(); i++)
			_data[i] = dataList.get(i);
	}
	
	@Override
	protected Tensor clone()
	{
		return new Tensor(_order, _data);
	}
	
	int rk()
	{
		if (dim() == 0)
			return 0;
		int r = _order[0];
		for (int i = 1; i < dim(); i++)
			r = Math.min(r, _order[i]);
		return r;
	}
	public int dim()
	{
		return _order.length;
	}
	public int length()
	{
		return _data.length;
	}
	public int size()
	{
		return _data.length;
	}
	public int n(int i)
	{
		return _order[i];
	}
	public Tensor reshape(int d, int... ord)
	{
		int []order = new int[d];
		System.arraycopy(ord,0,order,0,d);
		return new Tensor(order, _data);
	}
	public double FrobeniusNorm()
	{
		List<Double> quardsList = new ArrayList<Double>();
		for (double x : _data)
			quardsList.add(x*x);
		Collections.sort(quardsList);
		double res = 0;
		for (double q : quardsList)
			res += q;
		return res;
	}
	public DenseMatrix64F unfolding(int k)
	{
		int rows = size(k), 
			cols = length()/rows;
		return new DenseMatrix64F(rows, cols, true, _data);
	}
	public DenseMatrix64F toDenseMatrix64F()
	{
		if (dim()!=2)
			return null;
		return new DenseMatrix64F(_order[0], _order[1], true, _data);
	}
	
	private int size(int k)
	{
		int res = 1;
		for (int i = 0; i < k; i++)
			res *= _order[i];
		return res;
	}
	
	public void set(int []ord, /*boolean rowMajor,*/ double ...data)
    {
        if( size() < data.length )
            throw new IllegalArgumentException("The length of this matrix's data array is too small.");
        System.arraycopy(data,0,_data,0,size());
    }
	public Tensor kmul(double k)
	{
		double []data = _data.clone();
		for (int i = 0; i < size(); i++)
			data[i] *= k;
		return new Tensor(_order, data);
	}
	public Tensor mul(double... vector)
	{
		int m = _order[0],
			l = _order[1],
			n = _order[2];
		if (dim()!=3 || vector.length!=l)
			return null;
		double []v = new double[l];
		System.arraycopy(vector,0,v,0,l);
		int []order = new int[]{ m,n };
		double []data = new double[m*n];
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				for (int k = 0; k < l; k++)
					data[i*n+j] += _data[(i*m+k)*l+j]*v[k];
		return new Tensor(order, data);
	}
	
	Tensor unionRow(Tensor T)
	{
		if (dim() != 2)
			return null;
		int m = Math.max(_order[0], T._order[0]),
			n = _order[1]+T._order[1];
		int []ord = new int[] { m,n };
		int size = m*n;
		double []data = new double[size];
		for (int k = 0; k <	m; k++)
		{
			for (int l = 0; l < _order[1]; l++)
				data[k*m+l] = (k < _order[0]) ? _data[k*_order[0]+l] : 0;
			for (int l = 0; l < T._order[1]; l++)
				data[k*m+l+_order[1]] = (k < T._order[0]) ? T._data[k*T._order[0]+l+_order[1]] : 0;
		}
		return new Tensor(ord, data);
	}
	Tensor unionDiag(Tensor T) 
	{
		if (dim()!=3 || _order[0]!=T._order[0])
			return null;
		int m = _order[1]+T._order[1],
			n = _order[2]+T._order[2];
		int []ord = new int[] { _order[0],m,n };
		int size = m*n;
		double []data = new double[size];
		for (int i = 0; i < _order[0]; i++)
		{
			for (int k = 0; k <	_order[1]; k++)
				for (int l = 0; l < _order[2]; l++)
					data[k*m+l] = _data[k*_order[1]+l];
			for (int k = 0; k <	T._order[1]; k++)
				for (int l = 0; l < T._order[2]; l++)
					data[(k+_order[1])*m+l+_order[2]] = T._data[(k+_order[1])*T._order[1]+l+_order[2]];
		}
		return new Tensor(ord, data);
	}
	Tensor unionCol(Tensor T)
	{
		if (dim() != 2)
			return null;
		int m = _order[0]+T._order[0],
			n = Math.max(_order[1],T._order[1]);
		int []ord = new int[] { m,n };
		int size = m*n;
		double []data = new double[size];
		for (int k = 0; k <	_order[0]; k++)
			for (int l = 0; l < _order[1]; l++)
				data[k*m+l] = _data[k*_order[0]+l];
		for (int k = 0; k <	T._order[0]; k++)
			for (int l = 0; l < T._order[1]; l++)
				data[(k+_order[0])*m+l+_order[1]] = T._data[(k+_order[0])*T._order[0]+l+_order[1]];
		return new Tensor(ord, data);
	}
	
	int		[]_order;
	double	[]_data;
}
