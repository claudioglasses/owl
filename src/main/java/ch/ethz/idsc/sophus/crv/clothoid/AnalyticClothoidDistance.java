// code by gjoel
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.math.MidpointInterface;
import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.AbsSquared;

// TODO GJOEL is this class still needed? can it be used for testing?
public enum AnalyticClothoidDistance implements TensorMetric, TensorNorm {
  LR1(ClothoidLR1Midpoint.INSTANCE), //
  LR3(ClothoidLR3Midpoint.INSTANCE), //
  ;
  // ---
  private final MidpointInterface midpointInterface;

  AnalyticClothoidDistance(MidpointInterface midpointInterface) {
    this.midpointInterface = midpointInterface;
  }

  /** @param p element in SE2 of the form {px, py, p_heading}
   * @param q element in SE2 of the form {qx, qy, q_heading}
   * @return length of clothoid between p and q when projected to R^2 */
  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    Scalar pa = p.Get(2);
    Scalar qa = q.Get(2);
    if (pa.equals(qa)) {
      Tensor midpoint = midpointInterface.midpoint(p, q);
      if (pa.equals(midpoint.Get(2)))
        return Norm._2.between(p.extract(0, 2), q.extract(0, 2));
      Scalar half_dist = distance(p, midpoint); // == distance(midpoint, q)
      return half_dist.add(half_dist); // 2 * half_dist
    }
    // TODO GJOEL investigate "direction"
    ClothoidTerminalRatio clothoidTerminalRatio = ClothoidTerminalRatios.planar(p, q);
    Scalar half_num = qa.subtract(pa);
    Scalar num = half_num.add(half_num); // 2 * half_num
    Scalar den = AbsSquared.FUNCTION.apply(clothoidTerminalRatio.tail()).subtract(AbsSquared.FUNCTION.apply(clothoidTerminalRatio.head()));
    Scalar a_squared = num.divide(den).abs();
    return a_squared.multiply(clothoidTerminalRatio.difference().abs());
  }

  @Override // from TensorNorm
  public Scalar norm(Tensor xya) {
    return distance(xya.map(Scalar::zero), xya);
  }
}
