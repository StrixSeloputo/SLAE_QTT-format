# SLAE_QTT-format
Solving system of linear algebraic equation

There are simple logic:
(*  source code is placed in *.java files in src-directory)
*  all tensor operations that are needed to task are implemented in Tesor class;
*  all operations on TT-format - in TensorTrain class;
*  solution algorithm with its subprocedures, which are in "disser.pdf" to describe algorithm, is implemented in SolveSystem class;
*  algorithm operates only with TT-format, that's why You should use TensorDecomposition to transfor Tensor to TensorTrain or 
      TensorTrain-constructor if You have not tensor in TT-format;
*  Pair, Triple and Utility are helped classes;
*  there is used external library EJML (http://ejml.org/wiki/index.php?title=Main_Page), You can download it here (http://ejml.org/wiki/index.php?title=Download)