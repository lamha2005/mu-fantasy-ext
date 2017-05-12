package com.creants.muext.entities.heroes;

import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.HeroClassType;

/**
 * @author LamHM
 *
 */
public class DarkKnight extends HeroClass {

	@Override
	public void init() {
		id = HeroClassType.DARK_KNIGHT.id;
		name = HeroClassType.DARK_KNIGHT.name;
	}

}
