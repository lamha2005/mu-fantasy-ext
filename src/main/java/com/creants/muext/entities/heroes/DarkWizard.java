package com.creants.muext.entities.heroes;

import com.creants.muext.entities.HeroClass;
import com.creants.muext.entities.HeroClassType;

/**
 * @author LamHM
 *
 */
public class DarkWizard extends HeroClass {

	@Override
	public void init() {
		this.id = HeroClassType.DARK_WIZARD.id;
		this.name = HeroClassType.DARK_WIZARD.name;

	}

}
