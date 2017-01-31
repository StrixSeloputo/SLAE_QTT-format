package v01_01_02;

public class Main {
//	A(i, i', j1, ..., jm) = sum_from{k=1}_to{m}(2^k*y_j(k+1))
//				 |  1	0|	 |  1	0|	 |  1	0|			|   1				0|	 |		1		|
//	B = [0	1] x |y_1	1| x |2*y_2	1| x |4*y_3	1| x ... x	|2^(m-2)*y_(m-1)	1| x |2^(m-1)k*y_(m)|
//	
//	=>
//	
//	A = A0 x I  x I  x ... x I  +
//		A1 x D1 x I  x ... x I  +
//		A2 x I  x D2 x ... x I  +
//				...				+
//		Am x I  x I  x ... x Dm 
//	
	
//	Tensor getA0(int M)
//	{
////		double []a0 = new double[2*M];
////		int []d = { M, 2 };
////		for (int i = 0; i < M; i++)
////		{
////			a0[i*M] = 0; a0[i*M+1] = 1;
////		}
////		return new Tensor(d, a0);
//		return new Tensor(new int[]{1,2}, 0,1);
//	}
//	Tensor getAm(int M)
//	{
////		double []aM = new double [2*M];
////		int []d = { 2, 2 };
////		return new Tensor(d, aM);
//		return new Tensor(new int[]{2,1}, 1, (1<<(M-1)));
//	}
//	
//	Tensor getAi(int i, int yi)
//	{
//		return new Tensor(new int[]{ 2,2 });
//	}
//	

	
	TensorTrain getIcomponent(int i)
	{
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
