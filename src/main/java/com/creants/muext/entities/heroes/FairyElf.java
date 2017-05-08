package com.creants.muext.entities.heroes;

import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.HeroClassType;

/**
 * @author LamHM
 *
 */
public class FairyElf extends HeroClass {

	@Override
	public void init() {
		this.id = HeroClassType.FAIRY_ELF.id;
		this.name = HeroClassType.FAIRY_ELF.name;
	}

}
