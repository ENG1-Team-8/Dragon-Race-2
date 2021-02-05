/*******************************************************************************
 * Copyright 2015 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.tomgrill.gdxtesting;

import static org.junit.Assert.assertEquals;

import com.hardgforgif.dragonboatracing.core.Map;
import com.hardgforgif.dragonboatracing.core.Boat;

import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(GdxTestRunner.class)
public class UnitTests {

	@Test
	public void oneEqualsOne() {
		assertEquals(1, 1);
	}

	@Test
	public void test() {
		Boat boat = new Boat(100f, 100f, 100f, 100f, 1, null);
		assertEquals(100f, boat.robustness, 0);
	}

	@Test
	public void mapTest(){
		Map map = new Map("Map1/Map1.tmx", 100);
	}

}
