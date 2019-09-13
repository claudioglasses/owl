// code by gjoel
package ch.ethz.idsc.owl.bot.se2.rrts;

import ch.ethz.idsc.owl.data.nd.EuclideanNdCenter;
import ch.ethz.idsc.owl.data.nd.NdCenterInterface;
import ch.ethz.idsc.owl.rrts.RrtsNdType;
import ch.ethz.idsc.sophus.math.Extract2D;
import ch.ethz.idsc.tensor.Tensor;

public enum Se2RrtsNdType implements RrtsNdType {
  INSTANCE;
  // ---
  @Override // from RrtsNdType
  public Tensor convert(Tensor tensor) {
    return Extract2D.FUNCTION.apply(tensor);
  }

  @Override // from RrtsNdType
  public NdCenterInterface ndCenterInterfaceBeg(Tensor tensor) {
    return EuclideanNdCenter.of(convert(tensor));
  }

  @Override // from RrtsNdType
  public NdCenterInterface ndCenterInterfaceEnd(Tensor tensor) {
    return EuclideanNdCenter.of(convert(tensor));
  }
}
