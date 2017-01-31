# SLAE_QTT-format
Solving system of linear algebraic equation in TT-format

There are simple logic:
(source code is placed in *.java files in src-directory - if it need to explain)
*  all tensor operations that are needed to task are implemented in Tesor class;
*  all operations on TT-format - in TensorTrain class;
*  solution algorithm with its subprocedures, which are in Oseledets I.V. Doctor Dissertation to describe algorithm, 
      is implemented in SolveSystem class;
*  algorithm operates only with TT-format, that's why You should use TensorDecomposition to transfor Tensor to TensorTrain or 
      TensorTrain-constructor if You have not tensor in TT-format;
*  Pair, Triple and Utility are helped classes;
*  there is used external library <a href="http://ejml.org/wiki/index.php?title=Main_Page">EJML</a>, You can download it <a href="http://ejml.org/wiki/index.php?title=Download">here</a>

Link to return to <a href="https://github.com/StrixSeloputo/SLAE_QTT-format">github</a>
