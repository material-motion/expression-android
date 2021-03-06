/*
 * Copyright (C) 2016 The Material Motion Authors. All Rights Reserved.
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
package com.google.android.material.motion.expression;

import com.google.android.material.motion.runtime.Plan;

/**
 * A function object that represents an arbitrary unit of work which generates
 * {@link Plan Plans}.
 *
 * <p>
 * Clients are not allowed to create a new instance of {@link Work}. It is used internally to pass
 * Plans along the {@link Expression} chain.
 *
 * <p>
 * Clients should instead implement a {@link Modifier} for {@link Term#modify(Modifier)} or
 * {@link Initializer} for {@link Term#Term(Language, Initializer, Plan...)}, which become
 * wrapped in a Work instance behind the scenes.
 */
public abstract class Work {

  Work() {}

  abstract Plan[] work();
}
