/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.gapid.glviewer.geo;

import static com.google.gapid.glviewer.vec.MatD.makeScaleTranslationZupToYup;

import com.google.gapid.glviewer.vec.MatD;
import com.google.gapid.glviewer.vec.VecD;

/**
 * Bounding box of a model aligned to the standard cartesian directions.
 */
public class BoundingBox {
  public static final BoundingBox INVALID = new BoundingBox();

  public final double[] min =
      new double[] { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY };
  public final double[] max =
      new double[] { Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY };

  public void add(VecD vec) {
    add(vec.x, vec.y, vec.z);
  }

  public void add(double x, double y, double z) {
    VecD.min(min, x, y, z);
    VecD.max(max, x, y, z);
  }

  public BoundingBox() {
    this(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE,
        Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
  }

  public BoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
    min[0] = minX;
    min[1] = minY;
    min[2] = minZ;
    max[0] = maxX;
    max[1] = maxY;
    max[2] = maxZ;
  }

  /**
   * @return a matrix that will center the model at the origin and scale it to the given size.
   */
  public MatD getCenteringMatrix(double diagonalSize, boolean zUp) {
    VecD minV = VecD.fromArray(min), maxV = VecD.fromArray(max);
    double diagonal = maxV.distance(minV);

    VecD translation = maxV.subtract(minV).multiply(0.5f).add(minV).multiply(-1);
    double scale = (diagonal == 0) ? 1 : diagonalSize / diagonal;

    return zUp ? makeScaleTranslationZupToYup(scale, translation) :
        MatD.makeScaleTranslation(scale, translation);
  }

  public BoundingBox transform(MatD transform) {
    VecD tMin = transform.multiply(VecD.fromArray(min));
    VecD tMax = transform.multiply(VecD.fromArray(max));
    BoundingBox result = new BoundingBox();
    result.add(tMin);
    result.add(tMax);
    return result;
  }
}
