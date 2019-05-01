// code by ob
package ch.ethz.idsc.sophus.filter;

import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

import ch.ethz.idsc.sophus.math.NavigableMapUnaryOperator;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Max;
import ch.ethz.idsc.tensor.red.Min;
import ch.ethz.idsc.tensor.sca.Sign;

// TODO OB extract "fixed radius" functionality to separate class, or extract "fixed Radius" to separate class
public class NonuniformFixedRadiusGeodesicCenterFilterNEW implements NavigableMapUnaryOperator {
  /** @param nonuniformGeodesicCenter
   * @param radius
   * @return
   * @throws Exception given if nonuniformFixedRadiusGeodesicCenterNEW is null */
  public static NonuniformFixedRadiusGeodesicCenterFilterNEW of(NonuniformFixedRadiusGeodesicCenterNEW nonuniformFixedRadiusGeodesicCenterNEW, Scalar radius) {
    return new NonuniformFixedRadiusGeodesicCenterFilterNEW(Objects.requireNonNull(nonuniformFixedRadiusGeodesicCenterNEW), radius);
  }

  // ---
  private final NonuniformFixedRadiusGeodesicCenterNEW nonuniformFixedRadiusGeodesicCenterNEW;
  private Scalar radius;

  private NonuniformFixedRadiusGeodesicCenterFilterNEW(NonuniformFixedRadiusGeodesicCenterNEW nonuniformFixedRadiusGeodesicCenterNEW, Scalar radius) {
    this.nonuniformFixedRadiusGeodesicCenterNEW = nonuniformFixedRadiusGeodesicCenterNEW;
    this.radius = Sign.requirePositive(radius);
  }

  @Override
  public NavigableMap<Scalar, Tensor> apply(NavigableMap<Scalar, Tensor> navigableMap) {
    NavigableMap<Scalar, Tensor> resultMap = new TreeMap<>();
    for (Scalar key : navigableMap.keySet()) {
      Scalar loKey = key;
      Scalar hiKey = key;
      for (int index = 0; index < radius.number().intValue(); ++index) {
        if (loKey.equals(navigableMap.firstKey()) || hiKey.equals(navigableMap.lastKey()))
          break;
        loKey = Max.of(navigableMap.lowerKey(loKey), navigableMap.firstKey());
        hiKey = Min.of(navigableMap.higherKey(hiKey), navigableMap.lastKey());
      }
      NavigableMap<Scalar, Tensor> subMap = navigableMap.subMap(loKey, true, hiKey, true);
      resultMap.put(key, nonuniformFixedRadiusGeodesicCenterNEW.apply(subMap, key, radius));
    }
    return resultMap;
  }
}
